package com.digitaltwin.central.service;

import com.digitaltwin.central.dto.LineupEventResponseDto;
import com.digitaltwin.central.model.LineupEvent;
import com.digitaltwin.central.repository.LineupEventRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LineupService {

    private final LineupEventRepository lineupEventRepository;

    public LineupService(LineupEventRepository lineupEventRepository) {
        this.lineupEventRepository = lineupEventRepository;
    }

    public List<LineupEventResponseDto> getLineup() {
        return lineupEventRepository.findAllByOrderByStartsAtAsc().stream()
                .map(this::toResponse)
                .toList();
    }

    private LineupEventResponseDto toResponse(LineupEvent event) {
        return new LineupEventResponseDto(
                event.getId(),
                event.getArtist().getId(),
                event.getArtist().getName(),
                event.getArtist().getGenre(),
                event.getStage().getId(),
                event.getStage().getName(),
                event.getStage().getZoneCode(),
                event.getStartsAt(),
                event.getEndsAt(),
                event.getTitle(),
                event.getStatus()
        );
    }
}
