package com.digitaltwin.central;

import com.digitaltwin.central.repository.StageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
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

	@BeforeEach
	void setUp() {
		stageRepository.deleteAll();
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

}
