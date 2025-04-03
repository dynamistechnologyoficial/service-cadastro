package com.banco.cadastro.br.service.mapper;

import static com.banco.cadastro.br.domain.ArquivoByteAsserts.*;
import static com.banco.cadastro.br.domain.ArquivoByteTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArquivoByteMapperTest {

    private ArquivoByteMapper arquivoByteMapper;

    @BeforeEach
    void setUp() {
        arquivoByteMapper = new ArquivoByteMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArquivoByteSample1();
        var actual = arquivoByteMapper.toEntity(arquivoByteMapper.toDto(expected));
        assertArquivoByteAllPropertiesEquals(expected, actual);
    }
}
