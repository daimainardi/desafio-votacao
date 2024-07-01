package com.daimainardi.desafiovotacao.controller;

import com.daimainardi.desafiovotacao.entity.AgendaEntity;
import com.daimainardi.desafiovotacao.mapper.AgendaMapper;
import com.daimainardi.desafiovotacao.repository.AgendaRepository;
import com.daimainardi.desafiovotacao.stub.StubBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AgendaControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AgendaRepository agendaRepository;

    @Test
    @DisplayName("Deve salvar uma nova agenda")
    void shouldCreateNewAgenda() throws Exception {
        BDDMockito.given(agendaRepository.save(any(AgendaEntity.class)))
                .willReturn(AgendaMapper.mapRequestToEntity(StubBuilder.agendaRequestDTO()));
        mockMvc.perform(post("/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(StubBuilder.agendaRequestDTO())))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Aumento de salário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value("Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos"));
    }
    @Test
    @DisplayName("Deve retornar uma lista de agendas")
    void shouldFindAllAgenda() throws Exception {
        List<AgendaEntity> agendaEntityList = List.of(StubBuilder.agendaEntity());
        BDDMockito.given(agendaRepository.findAll()).willReturn(agendaEntityList);
        mockMvc.perform(get("/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(StubBuilder.agendaResponseDTO())))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("Aumento de salário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description")
                        .value("Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));

    }
}
