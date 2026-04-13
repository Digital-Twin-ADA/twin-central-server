package com.digitaltwin.central.service;

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

    public List<Stage> getAllStages() {
        return stageRepository.findAll();
    }

    public Stage createStage(Stage stage) {
        return stageRepository.save(stage);
    }

    public Optional<Stage> getStageById(Long id) {
        return stageRepository.findById(id);
    }
}