package com.daimainardi.desafiovotacao.controller;

import com.daimainardi.desafiovotacao.repository.AgendaRepository;
import com.daimainardi.desafiovotacao.request.AgendaRequestDTO;
import com.daimainardi.desafiovotacao.stub.StubBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
class AgendaControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AgendaRepository agendaRepository;

    @BeforeEach
    void setUp() {
        agendaRepository.deleteAll();

        agendaRepository.save(StubBuilder.agendaEntity());
    }

    @Test
    @DisplayName("Deve salvar uma nova agenda")
    void shouldCreateNewAgenda() throws Exception {

        AgendaRequestDTO agendaRequestDTO = new AgendaRequestDTO("Expandir para o Sudeste",
                "Criar uma filial em Belo Horizonte, Minas Gerais");

        mockMvc.perform(post("/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(agendaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Expandir para o Sudeste"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description")
                        .value("Criar uma filial em Belo Horizonte, Minas Gerais"));
    }

    @Test
    @DisplayName("Deve retornar uma DuplicateKeyException quando tentar cadastrar uma agenda com o mesmo título")
    void shouldNotCreateNewAgenda() throws Exception {

        mockMvc.perform(post("/agendas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(StubBuilder.agendaRequestDTO())))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value(DuplicateKeyException.class.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Duplicated fields"));
    }

    @Test
    @DisplayName("Deve retornar uma lista de agendas")
    void shouldFindAllAgenda() throws Exception {

        mockMvc.perform(get("/agendas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title").value("Aumento de salário"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].description")
                        .value("Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos"));
    }
}
