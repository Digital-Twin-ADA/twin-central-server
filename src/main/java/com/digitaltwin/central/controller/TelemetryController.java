package com.digitaltwin.central.controller;

import com.digitaltwin.central.dto.TelemetryRequest;
import com.digitaltwin.central.model.Stage;
import com.digitaltwin.central.repository.StageRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {
    private final StageRepository stageRepository;

    public TelemetryController(StageRepository stageRepository)
    {
        this.stageRepository = stageRepository;
    }

    @PostMapping
    public ResponseEntity<?> receiveTelemetry(@RequestBody TelemetryRequest request)
    {
        Optional<Stage> optionalStage = stageRepository.findById(request.getStageId());

        if (optionalStage.isEmpty())
        {
            return ResponseEntity.badRequest().body("Stage not found with id: " + request.getStageId());
        }

        Stage stage = optionalStage.get();
        stage.setCurrentCrowd(request.getCurrentCrowd());
        stage.setOvercrowded(stage.getCurrentCrowd() >= stage.getCapacity());

        stageRepository.save(stage);

        return ResponseEntity.ok(stage);
    }
}
