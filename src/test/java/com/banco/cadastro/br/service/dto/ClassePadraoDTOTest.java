package com.banco.cadastro.br.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ClassePadraoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassePadraoDTO.class);
        ClassePadraoDTO classePadraoDTO1 = new ClassePadraoDTO();
        classePadraoDTO1.setId(1L);
        ClassePadraoDTO classePadraoDTO2 = new ClassePadraoDTO();
        assertThat(classePadraoDTO1).isNotEqualTo(classePadraoDTO2);
        classePadraoDTO2.setId(classePadraoDTO1.getId());
        assertThat(classePadraoDTO1).isEqualTo(classePadraoDTO2);
        classePadraoDTO2.setId(2L);
        assertThat(classePadraoDTO1).isNotEqualTo(classePadraoDTO2);
        classePadraoDTO1.setId(null);
        assertThat(classePadraoDTO1).isNotEqualTo(classePadraoDTO2);
    }
}
