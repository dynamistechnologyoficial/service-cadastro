package com.banco.cadastro.br.service.mapper;

import com.banco.cadastro.br.domain.EntidadeTeste;
import com.banco.cadastro.br.service.dto.EntidadeTesteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EntidadeTeste} and its DTO {@link EntidadeTesteDTO}.
 */
@Mapper(componentModel = "spring")
public interface EntidadeTesteMapper extends EntityMapper<EntidadeTesteDTO, EntidadeTeste> {}
