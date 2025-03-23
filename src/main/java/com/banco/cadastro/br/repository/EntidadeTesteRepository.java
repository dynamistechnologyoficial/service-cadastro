package com.banco.cadastro.br.repository;

import com.banco.cadastro.br.domain.EntidadeTeste;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EntidadeTeste entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntidadeTesteRepository extends JpaRepository<EntidadeTeste, Long>, JpaSpecificationExecutor<EntidadeTeste> {}
