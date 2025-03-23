package com.banco.cadastro.br.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DadoPadraoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DadoPadraoDTO.class);
        DadoPadraoDTO dadoPadraoDTO1 = new DadoPadraoDTO();
        dadoPadraoDTO1.setIdDado(1L);
        dadoPadraoDTO1.setClassePadraoId(1L);
        DadoPadraoDTO dadoPadraoDTO2 = new DadoPadraoDTO();
        assertThat(dadoPadraoDTO1).isNotEqualTo(dadoPadraoDTO2);
        dadoPadraoDTO2.setIdDado(dadoPadraoDTO1.getIdDado());
        dadoPadraoDTO2.setClassePadraoId(dadoPadraoDTO1.getClassePadraoId());
        assertThat(dadoPadraoDTO1).isEqualTo(dadoPadraoDTO2);
        dadoPadraoDTO2.setIdDado(2L);
        dadoPadraoDTO2.setClassePadraoId(2L);
        assertThat(dadoPadraoDTO1).isNotEqualTo(dadoPadraoDTO2);
        dadoPadraoDTO1.setIdDado(null);
        dadoPadraoDTO1.setClassePadraoId(null);
        assertThat(dadoPadraoDTO1).isNotEqualTo(dadoPadraoDTO2);
    }
}
