package com.banco.cadastro.br.domain;

import static com.banco.cadastro.br.domain.EntidadeTesteTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.banco.cadastro.br.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EntidadeTesteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EntidadeTeste.class);
        EntidadeTeste entidadeTeste1 = getEntidadeTesteSample1();
        EntidadeTeste entidadeTeste2 = new EntidadeTeste();
        assertThat(entidadeTeste1).isNotEqualTo(entidadeTeste2);

        entidadeTeste2.setId(entidadeTeste1.getId());
        assertThat(entidadeTeste1).isEqualTo(entidadeTeste2);

        entidadeTeste2 = getEntidadeTesteSample2();
        assertThat(entidadeTeste1).isNotEqualTo(entidadeTeste2);
    }
}
