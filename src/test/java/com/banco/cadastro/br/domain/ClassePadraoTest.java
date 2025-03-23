package com.banco.cadastro.br.domain;

import static com.banco.cadastro.br.domain.ClassePadraoTestSamples.*;
import static com.banco.cadastro.br.domain.DadoPadraoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ClassePadraoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassePadrao.class);
        ClassePadrao classePadrao1 = getClassePadraoSample1();
        ClassePadrao classePadrao2 = new ClassePadrao();
        assertThat(classePadrao1).isNotEqualTo(classePadrao2);

        classePadrao2.setId(classePadrao1.getId());
        assertThat(classePadrao1).isEqualTo(classePadrao2);

        classePadrao2 = getClassePadraoSample2();
        assertThat(classePadrao1).isNotEqualTo(classePadrao2);
    }

    @Test
    void dadoPadraoTest() {
        ClassePadrao classePadrao = getClassePadraoRandomSampleGenerator();
        DadoPadrao dadoPadraoBack = getDadoPadraoRandomSampleGenerator();

        classePadrao.addDadoPadrao(dadoPadraoBack);
        assertThat(classePadrao.getDadoPadraos()).containsOnly(dadoPadraoBack);
        assertThat(dadoPadraoBack.getClassePadrao()).isEqualTo(classePadrao);

        classePadrao.removeDadoPadrao(dadoPadraoBack);
        assertThat(classePadrao.getDadoPadraos()).doesNotContain(dadoPadraoBack);
        assertThat(dadoPadraoBack.getClassePadrao()).isNull();

        classePadrao.dadoPadraos(new HashSet<>(Set.of(dadoPadraoBack)));
        assertThat(classePadrao.getDadoPadraos()).containsOnly(dadoPadraoBack);
        assertThat(dadoPadraoBack.getClassePadrao()).isEqualTo(classePadrao);

        classePadrao.setDadoPadraos(new HashSet<>());
        assertThat(classePadrao.getDadoPadraos()).doesNotContain(dadoPadraoBack);
        assertThat(dadoPadraoBack.getClassePadrao()).isNull();
    }
}
