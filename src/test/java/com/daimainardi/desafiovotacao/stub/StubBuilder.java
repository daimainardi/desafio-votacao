package com.daimainardi.desafiovotacao.stub;

import com.daimainardi.desafiovotacao.entity.AgendaEntity;
import com.daimainardi.desafiovotacao.entity.SessionEntity;
import com.daimainardi.desafiovotacao.entity.VoteEntity;
import com.daimainardi.desafiovotacao.request.AgendaRequestDTO;
import com.daimainardi.desafiovotacao.request.SessionRequestDTO;
import com.daimainardi.desafiovotacao.request.VoteRequestDTO;
import com.daimainardi.desafiovotacao.response.AgendaResponseDTO;
import com.daimainardi.desafiovotacao.response.VoteResultDTO;

import java.time.LocalDateTime;

public class StubBuilder {

    public static AgendaRequestDTO agendaRequestDTO() {
        return new AgendaRequestDTO("Aumento de salário",
                "Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos");
    }

    public static AgendaResponseDTO agendaResponseDTO() {
        return new AgendaResponseDTO("123456", "Aumento de salário",
                "Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos");
    }

    public static AgendaEntity agendaEntity() {
        return new AgendaEntity("123456", "Aumento de salário",
                "Aumento de 5% do salário para os desenvolvedores de software com mais de 5 anos",
                LocalDateTime.now());
    }

    public static SessionRequestDTO sessionRequestDTO() {
        return new SessionRequestDTO("123456", 30);
    }

    public static SessionEntity sessionEntity() {
        return new SessionEntity("78910", "123456", LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(sessionRequestDTO().durationMinutes()));
    }

    public static VoteRequestDTO voteRequestDTO() {
        return new VoteRequestDTO("84488737005", "78910", "sim");
    }


    public static VoteEntity voteEntityYes(){
        return new VoteEntity("1234", "84488737005", "78910", "SIM");
    }
    public static VoteEntity voteEntityNo(){
        return new VoteEntity("123", "00762345098", "78910", "NAO");
    }
}
