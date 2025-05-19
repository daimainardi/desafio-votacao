package com.daimainardi.desafiovotacao.event;

import com.daimainardi.desafiovotacao.enumeration.SessionStatus;
import com.daimainardi.desafiovotacao.response.PayloadDTO;
import com.daimainardi.desafiovotacao.response.SessionResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageProducer {

    private final KafkaTemplate<String, PayloadDTO> kafkaTemplate;

    public void sendSessionEvent(SessionResponseDTO responseDTO, SessionStatus sessionStatus) {
        PayloadDTO payloadDTO = new PayloadDTO(
                responseDTO.id(),
                responseDTO.agendaTitle(),
                responseDTO.durationMinutes(),
                sessionStatus);
        sendMessage("Session-topic", payloadDTO);
    }

    public void sendMessage(String topic, PayloadDTO payloadDTO) {
        try {
            kafkaTemplate.send(topic, payloadDTO);
            log.info("Message sent to topic {}: {}", topic, payloadDTO);
        } catch (Exception e) {
            log.error("Error sending message to topic: {}", payloadDTO, e.getCause());
            throw e;
        }
    }
}
