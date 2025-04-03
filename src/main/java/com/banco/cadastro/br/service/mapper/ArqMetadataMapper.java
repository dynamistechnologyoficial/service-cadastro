package com.banco.cadastro.br.service.mapper;

import com.banco.cadastro.br.domain.ArqMetadata;
import com.banco.cadastro.br.service.dto.ArqMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArqMetadata} and its DTO {@link ArqMetadataDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArqMetadataMapper extends EntityMapper<ArqMetadataDTO, ArqMetadata> {}
