package com.banco.cadastro.br.service.mapper;

import static com.banco.cadastro.br.domain.DadoPadraoAsserts.*;
import static com.banco.cadastro.br.domain.DadoPadraoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DadoPadraoMapperTest {

    private DadoPadraoMapper dadoPadraoMapper;

    @BeforeEach
    void setUp() {
        dadoPadraoMapper = new DadoPadraoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDadoPadraoSample1();
        var actual = dadoPadraoMapper.toEntity(dadoPadraoMapper.toDto(expected));
        assertDadoPadraoAllPropertiesEquals(expected, actual);
    }
}
