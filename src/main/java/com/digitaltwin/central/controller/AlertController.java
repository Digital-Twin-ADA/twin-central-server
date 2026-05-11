package com.digitaltwin.central.controller;

import com.digitaltwin.central.dto.AlertRequestDto;
import com.digitaltwin.central.dto.AlertResponseDto;
import com.digitaltwin.central.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public org.springframework.data.domain.Page<AlertResponseDto> getAll(
            @RequestParam(required = false) Long stageId,
            @RequestParam(required = false) Boolean resolved,
            @RequestParam(required = false) String severity,
            org.springframework.data.domain.Pageable pageable
    ) {
        return alertService.findAlerts(stageId, resolved, severity, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(alertService.getAlertById(id));
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody AlertRequestDto dto) {
        AlertResponseDto created = alertService.createAlert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PostMapping("/{id}/resolve")
    public ResponseEntity<?> resolve(@PathVariable Long id) {
        alertService.resolveAlert(id);
        return ResponseEntity.ok().build();
    }
}
