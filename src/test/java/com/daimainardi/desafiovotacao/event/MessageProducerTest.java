package com.daimainardi.desafiovotacao.event;

import com.daimainardi.desafiovotacao.enumeration.SessionStatus;
import com.daimainardi.desafiovotacao.response.PayloadDTO;
import com.daimainardi.desafiovotacao.response.SessionResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class MessageProducerTest {

    @InjectMocks
    private MessageProducer messageProducer;

    @Mock
    private KafkaTemplate<String, PayloadDTO> kafkaTemplate;

    @Test
    @DisplayName("should send session event with correct parameters")
    void shouldSendSessionEvent() {
        SessionResponseDTO responseDTO = new SessionResponseDTO("1", "Test Agenda", 30);
        SessionStatus sessionStatus = SessionStatus.CREATED;

        messageProducer.sendSessionEvent(responseDTO, sessionStatus);

        Mockito.verify(kafkaTemplate, Mockito.times(1))
                .send("Session-topic", any(PayloadDTO.class));
        Assertions.assertEquals("1", responseDTO.id());
        Assertions.assertEquals("Test Agenda", responseDTO.agendaTitle());
        Assertions.assertEquals(30, responseDTO.durationMinutes());
        Assertions.assertEquals(SessionStatus.CREATED, sessionStatus);
    }

    @Test
    @DisplayName("should send message with correct parameters")
    void shouldSendMessage() {
        PayloadDTO payloadDTO = new PayloadDTO("1", "Test Agenda", 30, SessionStatus.CREATED);

        messageProducer.sendMessage("Session-topic", payloadDTO);

        Mockito.verify(kafkaTemplate, Mockito.times(1))
                .send("Session-topic", payloadDTO);
    }

    @Test
    @DisplayName("should throw exception when sending message fails")
    void sendMessageThrowsExceptionTest() {
        PayloadDTO payload = new PayloadDTO("2", "Errored Agenda", 20, SessionStatus.CREATED);
        Mockito.doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(anyString(), any(PayloadDTO.class));

        Assertions.assertThrows(RuntimeException.class, () -> messageProducer.sendMessage("Session-topic", payload));

        Mockito.verify(kafkaTemplate, Mockito.times(1)).send("Session-topic", payload);
    }
}