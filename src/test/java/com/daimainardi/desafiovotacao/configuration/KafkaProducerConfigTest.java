package com.daimainardi.desafiovotacao.configuration;

import com.daimainardi.desafiovotacao.response.PayloadDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KafkaProducerConfigTest {

    private final KafkaProducerConfig kafkaProducerConfig = new KafkaProducerConfig();

    @Test
    @DisplayName("should create ProducerFactory")
    void shouldCreateProducerFactory() {
        ProducerFactory<String, PayloadDTO> producerFactory = kafkaProducerConfig.producerFactory();
        assertNotNull(producerFactory, "ProducerFactory should not be null");
    }
    @Test
    @DisplayName("should create KafkaTemplate")
    void shouldCreateKafkaTemplate() {
        KafkaTemplate<String, PayloadDTO> kafkaTemplate = kafkaProducerConfig.kafkaTemplate();
        assertNotNull(kafkaTemplate, "KafkaTemplate should not be null");
    }
}
