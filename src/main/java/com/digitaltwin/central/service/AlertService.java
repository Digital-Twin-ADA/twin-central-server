package com.digitaltwin.central.service;

import com.digitaltwin.central.dto.AlertRequestDto;
import com.digitaltwin.central.dto.AlertResponseDto;
import com.digitaltwin.central.model.Alert;
import com.digitaltwin.central.model.Stage;
import com.digitaltwin.central.repository.AlertRepository;
import com.digitaltwin.central.repository.StageRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class AlertService {

    private final AlertRepository alertRepository;
    private final StageRepository stageRepository;
    private final com.digitaltwin.central.service.AlertPublisher alertPublisher;
    private final com.digitaltwin.central.service.WebhookService webhookService;

    public AlertService(AlertRepository alertRepository, StageRepository stageRepository, com.digitaltwin.central.service.AlertPublisher alertPublisher, com.digitaltwin.central.service.WebhookService webhookService) {
        this.alertRepository = alertRepository;
        this.stageRepository = stageRepository;
        this.alertPublisher = alertPublisher;
        this.webhookService = webhookService;
    }

    public List<AlertResponseDto> getAllAlerts() {
        return alertRepository.findAll().stream()
                .map(a -> new AlertResponseDto(
                        a.getId(),
                        a.getStage() != null ? a.getStage().getId() : null,
                        a.getType(),
                        a.getMessage(),
                        a.getSeverity(),
                        a.getCreatedAt(),
                        a.isResolved(),
                        a.getResolvedAt()
                ))
                .toList();
    }

    public org.springframework.data.domain.Page<AlertResponseDto> findAlerts(Long stageId, Boolean resolved, String severity, org.springframework.data.domain.Pageable pageable) {
        org.springframework.data.domain.Page<Alert> page = alertRepository.findAll((root, query, cb) -> {
            java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
            if (stageId != null) {
                predicates.add(cb.equal(root.get("stage").get("id"), stageId));
            }
            if (resolved != null) {
                predicates.add(cb.equal(root.get("resolved"), resolved));
            }
            if (severity != null && !severity.isBlank()) {
                predicates.add(cb.equal(root.get("severity"), severity));
            }
            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        }, pageable);

        return page.map(a -> new AlertResponseDto(
                a.getId(),
                a.getStage() != null ? a.getStage().getId() : null,
                a.getType(),
                a.getMessage(),
                a.getSeverity(),
                a.getCreatedAt(),
                a.isResolved(),
                a.getResolvedAt()
        ));
    }

    public AlertResponseDto getAlertById(Long id) {
        Alert a = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));

        return new AlertResponseDto(
                a.getId(),
                a.getStage() != null ? a.getStage().getId() : null,
                a.getType(),
                a.getMessage(),
                a.getSeverity(),
                a.getCreatedAt(),
                a.isResolved(),
                a.getResolvedAt()
        );
    }

    public AlertResponseDto createAlert(AlertRequestDto dto) {
        Alert alert = new Alert();

        if (dto.getStageId() != null) {
            Stage stage = stageRepository.findById(dto.getStageId()).orElse(null);
            alert.setStage(stage);
        }

        alert.setType(dto.getType());
        alert.setMessage(dto.getMessage());
        alert.setSeverity(dto.getSeverity());
        alert.setCreatedAt(OffsetDateTime.now());
        alert.setResolved(false);

        Alert saved = alertRepository.save(alert);

        AlertResponseDto response = new AlertResponseDto(
                saved.getId(),
                saved.getStage() != null ? saved.getStage().getId() : null,
                saved.getType(),
                saved.getMessage(),
                saved.getSeverity(),
                saved.getCreatedAt(),
                saved.isResolved(),
                saved.getResolvedAt()
        );

        // publish to websocket topic
        try {
            alertPublisher.publish(response);
        } catch (Exception ignored) {
            // non-fatal if messaging isn't available
        }

        // notify webhook subscribers (async)
        try {
            webhookService.notifySubscribers(response);
        } catch (Exception ignored) {
        }

        return response;
    }

    public void resolveAlert(Long id) {
        Alert a = alertRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Alert not found with id: " + id));
        a.setResolved(true);
        a.setResolvedAt(OffsetDateTime.now());
        Alert saved = alertRepository.save(a);

        // publish update
        AlertResponseDto updatedDto = new AlertResponseDto(
                saved.getId(),
                saved.getStage() != null ? saved.getStage().getId() : null,
                saved.getType(),
                saved.getMessage(),
                saved.getSeverity(),
                saved.getCreatedAt(),
                saved.isResolved(),
                saved.getResolvedAt()
        );
        try {
            alertPublisher.publish(updatedDto);
        } catch (Exception ignored) {
        }

        try {
            webhookService.notifySubscribers(updatedDto);
        } catch (Exception ignored) {
        }
    }
}
