package com.digitaltwin.central.service;

import com.digitaltwin.central.dto.AlertResponseDto;
import com.digitaltwin.central.model.NotificationAttempt;
import com.digitaltwin.central.model.WebhookSubscriber;
import com.digitaltwin.central.repository.NotificationAttemptRepository;
import com.digitaltwin.central.repository.WebhookSubscriberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.HexFormat;
import java.util.List;
import java.util.concurrent.*;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Service
public class WebhookService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final int MAX_ATTEMPTS = 3;

    private final WebhookSubscriberRepository subscriberRepository;
    private final NotificationAttemptRepository attemptRepository;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private ScheduledExecutorService scheduler;

    public WebhookService(WebhookSubscriberRepository subscriberRepository, NotificationAttemptRepository attemptRepository, ObjectMapper objectMapper) {
        this.subscriberRepository = subscriberRepository;
        this.attemptRepository = attemptRepository;
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).build();
    }

    @PostConstruct
    public void init() {
        this.scheduler = Executors.newScheduledThreadPool(1);
    }

    @PreDestroy
    public void shutdown() {
        if (scheduler != null) scheduler.shutdownNow();
    }

    public void notifySubscribers(AlertResponseDto alert) {
        List<WebhookSubscriber> subscribers = subscriberRepository.findByActiveTrue();
        for (WebhookSubscriber s : subscribers) {
            sendWithRetries(alert, s, 0);
        }
    }

    private void sendWithRetries(AlertResponseDto alert, WebhookSubscriber s, int attempt) {
        try {
            String body = objectMapper.writeValueAsString(alert);
            HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                    .uri(URI.create(s.getUrl()))
                    .timeout(REQUEST_TIMEOUT)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body));

            if (s.getSecret() != null && !s.getSecret().isBlank()) {
                requestBuilder.header("X-Webhook-Signature", hmacSha256(body, s.getSecret()));
            }

            HttpRequest req = requestBuilder.build();

            CompletableFuture<HttpResponse<String>> future = httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString());
            future.whenComplete((resp, ex) -> {
                NotificationAttempt na = new NotificationAttempt();
                na.setAlertId(alert.getId());
                na.setSubscriberId(s.getId());
                na.setAttempts(attempt + 1);
                na.setLastAttemptAt(OffsetDateTime.now());
                if (ex != null) {
                    na.setStatus("FAILED");
                    na.setLastResponse(ex.getMessage());
                    attemptRepository.save(na);
                    scheduleRetry(alert, s, attempt + 1);
                } else {
                    na.setStatus(String.valueOf(resp.statusCode()));
                    na.setLastResponse(resp.body());
                    attemptRepository.save(na);
                    if (resp.statusCode() >= 500) {
                        scheduleRetry(alert, s, attempt + 1);
                    }
                }
            });
        } catch (Exception e) {
            // record failed attempt and possibly schedule retry
            NotificationAttempt na = new NotificationAttempt();
            na.setAlertId(alert.getId());
            na.setSubscriberId(s.getId());
            na.setAttempts(attempt + 1);
            na.setLastAttemptAt(OffsetDateTime.now());
            na.setStatus("FAILED");
            na.setLastResponse(e.getMessage());
            attemptRepository.save(na);
            scheduleRetry(alert, s, attempt + 1);
        }
    }

    private void scheduleRetry(AlertResponseDto alert, WebhookSubscriber s, int nextAttempt) {
        if (nextAttempt >= MAX_ATTEMPTS) return;
        long delaySeconds = (long) Math.pow(2, nextAttempt) * 5; // exponential backoff
        scheduler.schedule(() -> sendWithRetries(alert, s, nextAttempt), delaySeconds, TimeUnit.SECONDS);
    }

    private String hmacSha256(String body, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return "sha256=" + HexFormat.of().formatHex(mac.doFinal(body.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Could not sign webhook payload", e);
        }
    }
}
