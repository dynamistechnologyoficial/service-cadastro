package com.banco.cadastro.br.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArqMetadataDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArqMetadataDTO.class);
        ArqMetadataDTO arqMetadataDTO1 = new ArqMetadataDTO();
        arqMetadataDTO1.setId(1L);
        ArqMetadataDTO arqMetadataDTO2 = new ArqMetadataDTO();
        assertThat(arqMetadataDTO1).isNotEqualTo(arqMetadataDTO2);
        arqMetadataDTO2.setId(arqMetadataDTO1.getId());
        assertThat(arqMetadataDTO1).isEqualTo(arqMetadataDTO2);
        arqMetadataDTO2.setId(2L);
        assertThat(arqMetadataDTO1).isNotEqualTo(arqMetadataDTO2);
        arqMetadataDTO1.setId(null);
        assertThat(arqMetadataDTO1).isNotEqualTo(arqMetadataDTO2);
    }
}
