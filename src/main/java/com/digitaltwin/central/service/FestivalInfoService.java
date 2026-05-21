package com.digitaltwin.central.service;

import com.digitaltwin.central.dto.FestivalInfoRequestDto;
import com.digitaltwin.central.dto.FestivalInfoResponseDto;
import com.digitaltwin.central.dto.StageResponseDto;
import com.digitaltwin.central.model.FestivalInfo;
import com.digitaltwin.central.repository.FestivalInfoRepository;
import com.digitaltwin.central.repository.StageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FestivalInfoService {

    private final FestivalInfoRepository festivalInfoRepository;
    private final StageRepository stageRepository;

    public FestivalInfoService(FestivalInfoRepository festivalInfoRepository, StageRepository stageRepository) {
        this.festivalInfoRepository = festivalInfoRepository;
        this.stageRepository = stageRepository;
    }

    public FestivalInfoResponseDto getInfo() {
        FestivalInfo festivalInfo = festivalInfoRepository.findAll().stream()
                .findFirst()
                .orElseGet(this::createDefaultFestivalInfo);

        return toResponse(festivalInfo);
    }

    public FestivalInfoResponseDto saveInfo(FestivalInfoRequestDto dto) {
        FestivalInfo festivalInfo = festivalInfoRepository.findAll().stream()
                .findFirst()
                .orElseGet(FestivalInfo::new);

        festivalInfo.setName(dto.getName());
        festivalInfo.setLatitude(dto.getLatitude());
        festivalInfo.setLongitude(dto.getLongitude());
        festivalInfo.setDescription(dto.getDescription());

        return toResponse(festivalInfoRepository.save(festivalInfo));
    }

    private FestivalInfo createDefaultFestivalInfo() {
        FestivalInfo festivalInfo = new FestivalInfo();
        festivalInfo.setName("Digital Twin Festival");
        return festivalInfoRepository.save(festivalInfo);
    }

    private FestivalInfoResponseDto toResponse(FestivalInfo festivalInfo) {
        List<StageResponseDto> stages = stageRepository.findAll().stream()
                .map(stage -> new StageResponseDto(
                        stage.getId(),
                        stage.getName(),
                        stage.getCapacity(),
                        stage.getCurrentCrowd(),
                        stage.isOvercrowded(),
                        stage.getZoneCode(),
                        stage.getLatitude(),
                        stage.getLongitude()
                ))
                .toList();

        return new FestivalInfoResponseDto(
                festivalInfo.getId(),
                festivalInfo.getName(),
                festivalInfo.getLatitude(),
                festivalInfo.getLongitude(),
                festivalInfo.getDescription(),
                stages
        );
    }
}
