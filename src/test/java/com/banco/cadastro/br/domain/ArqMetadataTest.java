package com.banco.cadastro.br.domain;

import static com.banco.cadastro.br.domain.ArqMetadataTestSamples.*;
import static com.banco.cadastro.br.domain.ArquivoByteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArqMetadataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArqMetadata.class);
        ArqMetadata arqMetadata1 = getArqMetadataSample1();
        ArqMetadata arqMetadata2 = new ArqMetadata();
        assertThat(arqMetadata1).isNotEqualTo(arqMetadata2);

        arqMetadata2.setId(arqMetadata1.getId());
        assertThat(arqMetadata1).isEqualTo(arqMetadata2);

        arqMetadata2 = getArqMetadataSample2();
        assertThat(arqMetadata1).isNotEqualTo(arqMetadata2);
    }
}
