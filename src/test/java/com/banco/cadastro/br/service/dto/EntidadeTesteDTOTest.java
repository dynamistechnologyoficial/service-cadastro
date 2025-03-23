package com.banco.cadastro.br.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntidadeTesteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntidadeTesteDTO.class);
        EntidadeTesteDTO entidadeTesteDTO1 = new EntidadeTesteDTO();
        entidadeTesteDTO1.setId(1L);
        EntidadeTesteDTO entidadeTesteDTO2 = new EntidadeTesteDTO();
        assertThat(entidadeTesteDTO1).isNotEqualTo(entidadeTesteDTO2);
        entidadeTesteDTO2.setId(entidadeTesteDTO1.getId());
        assertThat(entidadeTesteDTO1).isEqualTo(entidadeTesteDTO2);
        entidadeTesteDTO2.setId(2L);
        assertThat(entidadeTesteDTO1).isNotEqualTo(entidadeTesteDTO2);
        entidadeTesteDTO1.setId(null);
        assertThat(entidadeTesteDTO1).isNotEqualTo(entidadeTesteDTO2);
    }
}
