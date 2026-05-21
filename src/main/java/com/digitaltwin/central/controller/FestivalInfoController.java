package com.digitaltwin.central.controller;

import com.digitaltwin.central.dto.FestivalInfoRequestDto;
import com.digitaltwin.central.dto.FestivalInfoResponseDto;
import com.digitaltwin.central.service.FestivalInfoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/festival")
public class FestivalInfoController {

    private final FestivalInfoService festivalInfoService;

    public FestivalInfoController(FestivalInfoService festivalInfoService) {
        this.festivalInfoService = festivalInfoService;
    }

    @GetMapping("/info")
    public FestivalInfoResponseDto getInfo() {
        return festivalInfoService.getInfo();
    }

    @PutMapping("/info")
    public FestivalInfoResponseDto saveInfo(@Valid @RequestBody FestivalInfoRequestDto dto) {
        return festivalInfoService.saveInfo(dto);
    }
}
