package com.digitaltwin.central;

import com.digitaltwin.central.dto.AlertRequestDto;
import com.digitaltwin.central.model.Artist;
import com.digitaltwin.central.model.LineupEvent;
import com.digitaltwin.central.model.Stage;
import com.digitaltwin.central.repository.AlertRepository;
import com.digitaltwin.central.repository.ArtistRepository;
import com.digitaltwin.central.repository.FestivalInfoRepository;
import com.digitaltwin.central.repository.LineupEventRepository;
import com.digitaltwin.central.repository.NotificationAttemptRepository;
import com.digitaltwin.central.repository.StageRepository;
import com.digitaltwin.central.repository.WebhookSubscriberRepository;
import com.digitaltwin.central.service.AlertService;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CentralServerApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private StageRepository stageRepository;

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private LineupEventRepository lineupEventRepository;

	@Autowired
	private FestivalInfoRepository festivalInfoRepository;

	@Autowired
	private AlertRepository alertRepository;

	@Autowired
	private WebhookSubscriberRepository webhookSubscriberRepository;

	@Autowired
	private NotificationAttemptRepository notificationAttemptRepository;

	@Autowired
	private AlertService alertService;

	@BeforeEach
	void setUp() {
		notificationAttemptRepository.deleteAll();
		webhookSubscriberRepository.deleteAll();
		alertRepository.deleteAll();
		lineupEventRepository.deleteAll();
		stageRepository.deleteAll();
		festivalInfoRepository.deleteAll();
		artistRepository.deleteAll();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void stageAndTelemetryEndpointsWork() throws Exception {
		String createdStage = mockMvc.perform(post("/api/stages")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Main Stage",
								  "capacity": 1000,
								  "zoneCode": "A1"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").value("Main Stage"))
				.andExpect(jsonPath("$.capacity").value(1000))
				.andExpect(jsonPath("$.currentCrowd").value(0))
				.andExpect(jsonPath("$.overcrowded").value(false))
				.andExpect(jsonPath("$.zoneCode").value("A1"))
				.andReturn()
				.getResponse()
				.getContentAsString();

		String stageId = createdStage.replaceAll(".*\"id\":(\\d+).*", "$1");

		mockMvc.perform(get("/api/stages"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id").value(Integer.parseInt(stageId)));

		mockMvc.perform(post("/api/telemetry")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "stageId": %s,
								  "currentCrowd": 1200
								}
								""".formatted(stageId)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.currentCrowd").value(1200))
				.andExpect(jsonPath("$.overcrowded").value(true));
	}

	@Test
	void festivalInfoEndpointReturnsFestivalAndStageCoordinates() throws Exception {
		mockMvc.perform(post("/api/stages")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "Main Stage",
								  "capacity": 1000,
								  "zoneCode": "A1",
								  "latitude": 44.4396,
								  "longitude": 26.0963
								}
								"""))
				.andExpect(status().isOk());

		mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/api/festival/info")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "name": "ADA Festival",
								  "latitude": 44.438,
								  "longitude": 26.097,
								  "description": "Digital twin test festival"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("ADA Festival"))
				.andExpect(jsonPath("$.latitude").value(44.438))
				.andExpect(jsonPath("$.longitude").value(26.097));

		mockMvc.perform(get("/api/festival/info"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("ADA Festival"))
				.andExpect(jsonPath("$.stages", hasSize(1)))
				.andExpect(jsonPath("$.stages[0].name").value("Main Stage"))
				.andExpect(jsonPath("$.stages[0].zoneCode").value("A1"))
				.andExpect(jsonPath("$.stages[0].latitude").value(44.4396))
				.andExpect(jsonPath("$.stages[0].longitude").value(26.0963));
	}

	@Test
	void artistEndpointsReturnListAndDetails() throws Exception {
		Artist artist = artistRepository.save(new Artist(
				"Aria Nova",
				"Pop",
				"High-energy pop artist known for immersive festival performances.",
				"Romania",
				"https://example.com/aria-nova.jpg"
		));

		artistRepository.save(new Artist(
				"DJ Pulsewave",
				"Electronic",
				"Electronic producer blending melodic techno with festival bass.",
				"Netherlands",
				null
		));

		mockMvc.perform(get("/api/artists"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name").value("Aria Nova"))
				.andExpect(jsonPath("$[0].genre").value("Pop"));

		mockMvc.perform(get("/api/artists/{id}", artist.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(artist.getId()))
				.andExpect(jsonPath("$.name").value("Aria Nova"))
				.andExpect(jsonPath("$.genre").value("Pop"))
				.andExpect(jsonPath("$.bio").value("High-energy pop artist known for immersive festival performances."))
				.andExpect(jsonPath("$.country").value("Romania"))
				.andExpect(jsonPath("$.imageUrl").value("https://example.com/aria-nova.jpg"));
	}

	@Test
	void lineupEndpointReturnsEventsOrderedByStartTime() throws Exception {
		Stage mainStage = stageRepository.save(new Stage("Main Stage", 1000, 0, false, "A1"));
		Stage electronicStage = stageRepository.save(new Stage("Electronic Stage", 2000, 0, false, "E1"));

		Artist artist = artistRepository.save(new Artist(
				"Aria Nova",
				"Pop",
				"High-energy pop artist known for immersive festival performances.",
				"Romania",
				null
		));

		Artist dj = artistRepository.save(new Artist(
				"DJ Pulsewave",
				"Electronic",
				"Electronic producer blending melodic techno with festival bass.",
				"Netherlands",
				null
		));

		lineupEventRepository.save(new LineupEvent(
				dj,
				electronicStage,
				OffsetDateTime.parse("2026-07-10T22:00:00+03:00"),
				OffsetDateTime.parse("2026-07-10T23:30:00+03:00"),
				"Late Electronic Set",
				"SCHEDULED"
		));

		lineupEventRepository.save(new LineupEvent(
				artist,
				mainStage,
				OffsetDateTime.parse("2026-07-10T18:00:00+03:00"),
				OffsetDateTime.parse("2026-07-10T19:15:00+03:00"),
				"Opening Pop Set",
				"SCHEDULED"
		));

		mockMvc.perform(get("/api/lineup"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].artistName").value("Aria Nova"))
				.andExpect(jsonPath("$[0].artistGenre").value("Pop"))
				.andExpect(jsonPath("$[0].stageName").value("Main Stage"))
				.andExpect(jsonPath("$[0].stageZoneCode").value("A1"))
				.andExpect(jsonPath("$[0].startsAt").value("2026-07-10T18:00:00+03:00"))
				.andExpect(jsonPath("$[0].endsAt").value("2026-07-10T19:15:00+03:00"))
				.andExpect(jsonPath("$[0].title").value("Opening Pop Set"))
				.andExpect(jsonPath("$[0].status").value("SCHEDULED"))
				.andExpect(jsonPath("$[1].artistName").value("DJ Pulsewave"));
	}

	@Test
	void webhooksReceiveSignedAlertPayloads() throws Exception {
		LinkedBlockingQueue<ReceivedWebhook> received = new LinkedBlockingQueue<>();
		HttpServer server = startWebhookServer(received);

		try {
			String webhookUrl = "http://127.0.0.1:" + server.getAddress().getPort() + "/hook";
			mockMvc.perform(post("/api/webhooks")
							.contentType(MediaType.APPLICATION_JSON)
							.content("""
									{
									  "url": "%s",
									  "secret": "shared-secret"
									}
									""".formatted(webhookUrl)))
					.andExpect(status().isCreated());

			AlertRequestDto dto = new AlertRequestDto();
			dto.setType("TEST");
			dto.setSeverity("LOW");
			dto.setMessage("Webhook delivery check");
			alertService.createAlert(dto);

			ReceivedWebhook webhook = received.poll(5, TimeUnit.SECONDS);
			assertThat(webhook)
					.as("recorded attempts: %s", notificationAttemptRepository.findAll().stream()
							.map(attempt -> attempt.getStatus() + " " + attempt.getLastResponse())
							.toList())
					.isNotNull();
			assertThat(webhook.body()).contains("\"type\":\"TEST\"");
			assertThat(webhook.body()).contains("\"createdAt\":");
			assertThat(webhook.signature()).startsWith("sha256=");
			assertThat(notificationAttemptRepository.findAll())
					.singleElement()
					.satisfies(attempt -> assertThat(attempt.getStatus()).isEqualTo("200"));
		} finally {
			server.stop(0);
		}
	}

	private HttpServer startWebhookServer(LinkedBlockingQueue<ReceivedWebhook> received) throws IOException {
		HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		server.createContext("/hook", exchange -> {
			String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
			String signature = exchange.getRequestHeaders().getFirst("X-Webhook-Signature");
			received.add(new ReceivedWebhook(body, signature));
			exchange.sendResponseHeaders(200, 0);
			exchange.close();
		});
		server.start();
		return server;
	}

	private record ReceivedWebhook(String body, String signature) {
	}
}
