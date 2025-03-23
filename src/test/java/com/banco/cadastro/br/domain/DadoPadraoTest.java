package com.banco.cadastro.br.domain;

import static com.banco.cadastro.br.domain.ClassePadraoTestSamples.*;
import static com.banco.cadastro.br.domain.DadoPadraoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DadoPadraoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DadoPadrao.class);
        DadoPadrao dadoPadrao1 = getDadoPadraoSample1();
        DadoPadrao dadoPadrao2 = new DadoPadrao();
        assertThat(dadoPadrao1).isNotEqualTo(dadoPadrao2);

        dadoPadrao2.setId(dadoPadrao1.getId());
        assertThat(dadoPadrao1).isEqualTo(dadoPadrao2);

        dadoPadrao2 = getDadoPadraoSample2();
        assertThat(dadoPadrao1).isNotEqualTo(dadoPadrao2);
    }

    @Test
    void classePadraoTest() {
        DadoPadrao dadoPadrao = getDadoPadraoRandomSampleGenerator();
        ClassePadrao classePadraoBack = getClassePadraoRandomSampleGenerator();

        dadoPadrao.setClassePadrao(classePadraoBack);
        assertThat(dadoPadrao.getClassePadrao()).isEqualTo(classePadraoBack);

        dadoPadrao.classePadrao(null);
        assertThat(dadoPadrao.getClassePadrao()).isNull();
    }
}
