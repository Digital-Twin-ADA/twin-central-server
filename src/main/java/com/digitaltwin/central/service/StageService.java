package com.digitaltwin.central.service;

import com.digitaltwin.central.dto.StageRequestDto;
import com.digitaltwin.central.dto.StageResponseDto;
import com.digitaltwin.central.model.Stage;
import com.digitaltwin.central.repository.StageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StageService {

    private final StageRepository stageRepository;

    public StageService(StageRepository stageRepository) {
        this.stageRepository = stageRepository;
    }

    // public List<Stage> getAllStages() {
    //     return stageRepository.findAll();
    // }

    public List<StageResponseDto> getAllStages() {
        return stageRepository.findAll().stream()
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
    }

    // public Stage createStage(Stage stage) {
    //     return stageRepository.save(stage);
    // }

    public StageResponseDto createStage(StageRequestDto dto) {

        Stage stage = new Stage();
        stage.setName(dto.getName());
        stage.setCapacity(dto.getCapacity());
        stage.setZoneCode(dto.getZoneCode());
        stage.setLatitude(dto.getLatitude());
        stage.setLongitude(dto.getLongitude());

        stage.setCurrentCrowd(0);
        stage.setOvercrowded(false);

        Stage saved = stageRepository.save(stage);

        return new StageResponseDto(
                saved.getId(),
                saved.getName(),
                saved.getCapacity(),
                saved.getCurrentCrowd(),
                saved.isOvercrowded(),
                saved.getZoneCode(),
                saved.getLatitude(),
                saved.getLongitude()
        );
    }

    // public Optional<Stage> getStageById(Long id) {
    //     return stageRepository.findById(id);
    // }

    public StageResponseDto getStageById(long id)
    {
        Stage stage = stageRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Stage not found with id: " + id));

        return new StageResponseDto(
            stage.getId(),
            stage.getName(),
            stage.getCapacity(),
            stage.getCurrentCrowd(),
            stage.isOvercrowded(),
            stage.getZoneCode(),
            stage.getLatitude(),
            stage.getLongitude()
        );
    }
}
