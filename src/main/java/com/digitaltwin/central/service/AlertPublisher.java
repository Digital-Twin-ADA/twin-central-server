package com.digitaltwin.central.service;

import com.digitaltwin.central.dto.AlertResponseDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class AlertPublisher {

    private final SimpMessagingTemplate template;

    public AlertPublisher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void publish(AlertResponseDto dto) {
        template.convertAndSend("/topic/alerts", dto);
    }
}
