package com.banco.cadastro.br.service.mapper;

import com.banco.cadastro.br.domain.ArqMetadata;
import com.banco.cadastro.br.domain.ArquivoByte;
import com.banco.cadastro.br.service.dto.ArqMetadataDTO;
import com.banco.cadastro.br.service.dto.ArquivoByteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArquivoByte} and its DTO {@link ArquivoByteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArquivoByteMapper extends EntityMapper<ArquivoByteDTO, ArquivoByte> {
    @Mapping(target = "arqMetadata", source = "arqMetadata", qualifiedByName = "arqMetadataId")
    ArquivoByteDTO toDto(ArquivoByte s);

    @Named("arqMetadataId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArqMetadataDTO toDtoArqMetadataId(ArqMetadata arqMetadata);
}
