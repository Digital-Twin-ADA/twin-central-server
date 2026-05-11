package com.digitaltwin.central.controller;

import com.digitaltwin.central.dto.TelemetryRequest;
import com.digitaltwin.central.dto.AlertRequestDto;
import com.digitaltwin.central.model.Stage;
import com.digitaltwin.central.service.AlertService;
import com.digitaltwin.central.repository.StageRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/telemetry")
public class TelemetryController {
    private final StageRepository stageRepository;
    private final AlertService alertService;

    public TelemetryController(StageRepository stageRepository, AlertService alertService)
    {
        this.stageRepository = stageRepository;
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<?> receiveTelemetry(@Valid @RequestBody TelemetryRequest request)
    {
        Optional<Stage> optionalStage = stageRepository.findById(request.getStageId());

        if (optionalStage.isEmpty())
        {
            return ResponseEntity.badRequest().body("Stage not found with id: " + request.getStageId());
        }

        Stage stage = optionalStage.get();
        boolean wasOvercrowded = stage.isOvercrowded();

        stage.setCurrentCrowd(request.getCurrentCrowd());
        boolean nowOvercrowded = stage.getCurrentCrowd() >= stage.getCapacity();
        stage.setOvercrowded(nowOvercrowded);

        stageRepository.save(stage);

        // If newly overcrowded, create an alert
        if (nowOvercrowded && !wasOvercrowded) {
            AlertRequestDto dto = new AlertRequestDto();
            dto.setStageId(stage.getId());
            dto.setType("OVER_CROWD");
            dto.setMessage("Stage " + stage.getName() + " is overcrowded: " + stage.getCurrentCrowd() + "/" + stage.getCapacity());
            dto.setSeverity("HIGH");
            alertService.createAlert(dto);
        }

        return ResponseEntity.ok(stage);
    }
}
