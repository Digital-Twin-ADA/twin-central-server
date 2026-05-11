package com.digitaltwin.central.controller;

import com.digitaltwin.central.model.Stage;
import com.digitaltwin.central.service.StageService;
import com.digitaltwin.central.dto.StageRequestDto;
import com.digitaltwin.central.dto.StageResponseDto;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stages")
public class StageController {

    private final StageService stageService;

    public StageController(StageService stageService) {
        this.stageService = stageService;
    }

    @GetMapping
    public List<StageResponseDto> getAllStages() {
        return stageService.getAllStages();
    }

    @GetMapping("/{id}")
    public StageResponseDto getStageById(@PathVariable Long id) {
        return stageService.getStageById(id);
    }

    @PostMapping
    public StageResponseDto createStage(@RequestBody StageRequestDto dto) {
        return stageService.createStage(dto);
    }
}