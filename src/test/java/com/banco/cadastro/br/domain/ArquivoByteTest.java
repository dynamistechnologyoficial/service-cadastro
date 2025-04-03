package com.banco.cadastro.br.domain;

import static com.banco.cadastro.br.domain.ArqMetadataTestSamples.*;
import static com.banco.cadastro.br.domain.ArquivoByteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ArquivoByteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ArquivoByte.class);
        ArquivoByte arquivoByte1 = getArquivoByteSample1();
        ArquivoByte arquivoByte2 = new ArquivoByte();
        assertThat(arquivoByte1).isNotEqualTo(arquivoByte2);

        arquivoByte2.setId(arquivoByte1.getId());
        assertThat(arquivoByte1).isEqualTo(arquivoByte2);

        arquivoByte2 = getArquivoByteSample2();
        assertThat(arquivoByte1).isNotEqualTo(arquivoByte2);
    }

    @Test
    void ArqMetadataTest() {
        ArquivoByte arquivoByte = getArquivoByteRandomSampleGenerator();
        ArqMetadata arqMetadataBack = getArqMetadataRandomSampleGenerator();

        arquivoByte.setArqMetadata(arqMetadataBack);
        assertThat(arquivoByte.getArqMetadata()).isEqualTo(arqMetadataBack);

        arquivoByte.arqMetadata(null);
        assertThat(arquivoByte.getArqMetadata()).isNull();
    }
}
