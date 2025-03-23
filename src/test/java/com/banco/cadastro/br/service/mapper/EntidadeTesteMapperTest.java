package com.banco.cadastro.br.service.mapper;

import static com.banco.cadastro.br.domain.EntidadeTesteAsserts.*;
import static com.banco.cadastro.br.domain.EntidadeTesteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EntidadeTesteMapperTest {

    private EntidadeTesteMapper entidadeTesteMapper;

    @BeforeEach
    void setUp() {
        entidadeTesteMapper = new EntidadeTesteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEntidadeTesteSample1();
        var actual = entidadeTesteMapper.toEntity(entidadeTesteMapper.toDto(expected));
        assertEntidadeTesteAllPropertiesEquals(expected, actual);
    }
}
