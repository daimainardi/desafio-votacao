package com.daimainardi.desafiovotacao.service;

import com.daimainardi.desafiovotacao.entity.AgendaEntity;
import com.daimainardi.desafiovotacao.exception.AgendaNotFoundException;
import com.daimainardi.desafiovotacao.mapper.AgendaMapper;
import com.daimainardi.desafiovotacao.repository.AgendaRepository;
import com.daimainardi.desafiovotacao.response.AgendaResponseDTO;
import com.daimainardi.desafiovotacao.stub.StubBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AgendaServiceTest {
    @Mock
    private AgendaRepository agendaRepository;
    @InjectMocks
    private AgendaService agendaService;

    @Test
    @DisplayName("Deve cadastrar nova agenda, não deve dar exceção")
    void shouldRegisterAgenda() {
        BDDMockito.given(agendaRepository.save(any(AgendaEntity.class)))
                .willReturn(AgendaMapper.mapRequestToEntity(StubBuilder.agendaRequestDTO()));
        AgendaResponseDTO agenda = agendaService.createAgenda(StubBuilder.agendaRequestDTO());
        Assertions.assertEquals("Aumento de salário", agenda.title());
        Assertions.assertEquals("Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos", agenda.description());
        BDDMockito.times(1);
        BDDMockito.verify(agendaRepository).save(any(AgendaEntity.class));
    }

    @Test
    @DisplayName("Deve lançar excessão DuplicateKeyException quando tentar cadastrar nome de pauta ja existente")
    void shouldThrowExceptionWhenTryingToRegisterAnExistingAgendaName() {
        BDDMockito.given(agendaRepository.save(any(AgendaEntity.class))).willThrow(DuplicateKeyException.class);
        Assertions.assertThrows(DuplicateKeyException.class, () -> agendaService.createAgenda(StubBuilder.agendaRequestDTO()));
    }

    @Test
    @DisplayName("Deve encontrar agenda com Id existente")
    void shouldFindAgendaById() {
        BDDMockito.given(agendaRepository.findById(StubBuilder.agendaEntity().id()))
                .willReturn(Optional.of(StubBuilder.agendaEntity()));
        Assertions.assertDoesNotThrow(() -> agendaService.findAgendaById(StubBuilder.agendaEntity().id()));
        AgendaEntity agenda = agendaService.findAgendaById(StubBuilder.agendaEntity().id());
        Assertions.assertEquals("Aumento de salário", agenda.title());
        Assertions.assertEquals("Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos", agenda.description());
        BDDMockito.times(1);
        BDDMockito.verify(agendaRepository).findById(StubBuilder.agendaEntity().id());
    }

    @Test
    @DisplayName("Não deve encontrar agenda, Id inexistente, AgendaNotFoundException")
    void shouldNotFindAgendaByIdNotFound() {
        BDDMockito.given(agendaRepository.findById(StubBuilder.agendaEntity().id()))
                .willThrow(new AgendaNotFoundException("Agenda not found", HttpStatus.NOT_FOUND));
        AgendaNotFoundException exception = Assertions.assertThrows(AgendaNotFoundException.class,
                () -> agendaService.findAgendaById(StubBuilder.agendaEntity().id()));
        Assertions.assertEquals("Agenda not found", exception.getMessage());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Deve retornar uma lista de AgendaResponseDTO")
    void shouldFindAllAgenda() {
        List<AgendaEntity> agendaEntityList = List.of(StubBuilder.agendaEntity());
        BDDMockito.given(agendaRepository.findAll()).willReturn(agendaEntityList);
        List<AgendaResponseDTO> agendaResponseDTOList = agendaService.findAll();
        BDDMockito.verify(agendaRepository, BDDMockito.times(1)).findAll();
        Assertions.assertNotNull(agendaResponseDTOList);
        Assertions.assertEquals("Aumento de salário", agendaResponseDTOList.getFirst().title());
        Assertions.assertEquals(1, agendaResponseDTOList.size());
    }
}
