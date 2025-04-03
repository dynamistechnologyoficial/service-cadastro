package com.banco.cadastro.br.service.mapper;

import static com.banco.cadastro.br.domain.ArqMetadataAsserts.*;
import static com.banco.cadastro.br.domain.ArqMetadataTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ArqMetadataMapperTest {

    private ArqMetadataMapper arqMetadataMapper;

    @BeforeEach
    void setUp() {
        arqMetadataMapper = new ArqMetadataMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getArqMetadataSample1();
        var actual = arqMetadataMapper.toEntity(arqMetadataMapper.toDto(expected));
        assertArqMetadataAllPropertiesEquals(expected, actual);
    }
}
