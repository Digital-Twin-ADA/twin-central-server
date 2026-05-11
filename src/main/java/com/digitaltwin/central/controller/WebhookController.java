package com.digitaltwin.central.controller;

import com.digitaltwin.central.model.WebhookSubscriber;
import com.digitaltwin.central.repository.NotificationAttemptRepository;
import com.digitaltwin.central.repository.WebhookSubscriberRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/webhooks")
public class WebhookController {

    private final WebhookSubscriberRepository repo;
    private final NotificationAttemptRepository attemptRepository;

    public WebhookController(WebhookSubscriberRepository repo, NotificationAttemptRepository attemptRepository) {
        this.repo = repo;
        this.attemptRepository = attemptRepository;
    }

    @GetMapping
    public List<WebhookSubscriber> list() {
        return repo.findAll();
    }

    public static class CreateReq {
        @NotBlank
        public String url;
        public String secret;
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateReq req) {
        WebhookSubscriber s = new WebhookSubscriber();
        s.setUrl(req.url);
        s.setSecret(req.secret);
        s.setActive(true);
        WebhookSubscriber saved = repo.save(s);
        return ResponseEntity.created(URI.create("/api/webhooks/" + saved.getId())).body(saved);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id) {
        if (!repo.existsById(id)) return ResponseEntity.notFound().build();
        attemptRepository.deleteBySubscriberId(id);
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
