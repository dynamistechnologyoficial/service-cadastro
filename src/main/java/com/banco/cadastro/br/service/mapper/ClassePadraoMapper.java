package com.banco.cadastro.br.service.mapper;

import com.banco.cadastro.br.domain.ClassePadrao;
import com.banco.cadastro.br.service.dto.ClassePadraoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ClassePadrao} and its DTO {@link ClassePadraoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClassePadraoMapper extends EntityMapper<ClassePadraoDTO, ClassePadrao> {}
