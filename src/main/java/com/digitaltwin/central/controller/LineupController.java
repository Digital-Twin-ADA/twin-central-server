package com.digitaltwin.central.controller;

import com.digitaltwin.central.dto.LineupEventResponseDto;
import com.digitaltwin.central.service.LineupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lineup")
public class LineupController {

    private final LineupService lineupService;

    public LineupController(LineupService lineupService) {
        this.lineupService = lineupService;
    }

    @GetMapping
    public List<LineupEventResponseDto> getLineup() {
        return lineupService.getLineup();
    }
}
