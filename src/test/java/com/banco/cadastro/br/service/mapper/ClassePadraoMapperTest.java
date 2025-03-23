package com.banco.cadastro.br.service.mapper;

import static com.banco.cadastro.br.domain.ClassePadraoAsserts.*;
import static com.banco.cadastro.br.domain.ClassePadraoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ClassePadraoMapperTest {

    private ClassePadraoMapper classePadraoMapper;

    @BeforeEach
    void setUp() {
        classePadraoMapper = new ClassePadraoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getClassePadraoSample1();
        var actual = classePadraoMapper.toEntity(classePadraoMapper.toDto(expected));
        assertClassePadraoAllPropertiesEquals(expected, actual);
    }
}
