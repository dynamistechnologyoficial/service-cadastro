package com.banco.cadastro.br.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArquivoByteDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoByteDTO.class);
        ArquivoByteDTO arquivoByteDTO1 = new ArquivoByteDTO();
        arquivoByteDTO1.setId(1L);
        ArquivoByteDTO arquivoByteDTO2 = new ArquivoByteDTO();
        assertThat(arquivoByteDTO1).isNotEqualTo(arquivoByteDTO2);
        arquivoByteDTO2.setId(arquivoByteDTO1.getId());
        assertThat(arquivoByteDTO1).isEqualTo(arquivoByteDTO2);
        arquivoByteDTO2.setId(2L);
        assertThat(arquivoByteDTO1).isNotEqualTo(arquivoByteDTO2);
        arquivoByteDTO1.setId(null);
        assertThat(arquivoByteDTO1).isNotEqualTo(arquivoByteDTO2);
    }
}
