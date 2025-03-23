package com.banco.cadastro.br.service.mapper;

import com.banco.cadastro.br.domain.ClassePadrao;
import com.banco.cadastro.br.domain.DadoPadrao;
import com.banco.cadastro.br.service.dto.ClassePadraoDTO;
import com.banco.cadastro.br.service.dto.DadoPadraoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DadoPadrao} and its DTO {@link DadoPadraoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DadoPadraoMapper extends EntityMapper<DadoPadraoDTO, DadoPadrao> {
	
//  @Mapping(target = "classePadrao", source = "classePadrao", qualifiedByName = "classePadraoNome")
    @Mapping(source = "id.idDado", target = "idDado")
    @Mapping(source = "id.classePadraoId", target = "classePadraoId")
    DadoPadraoDTO toDto(DadoPadrao s);
    
 // @Mapping(source = "classePadrao", target = "classePadrao")
    @Mapping(source = "idDado", target = "id.idDado")
    @Mapping(source = "classePadraoId", target = "id.classePadraoId")
    DadoPadrao toEntity(DadoPadraoDTO s);

    @Named("classePadraoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ClassePadraoDTO toDtoClassePadraoNome(ClassePadrao classePadrao);
}
