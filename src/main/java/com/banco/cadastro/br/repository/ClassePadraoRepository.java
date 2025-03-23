package com.banco.cadastro.br.repository;

import com.banco.cadastro.br.domain.ClassePadrao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ClassePadrao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClassePadraoRepository extends JpaRepository<ClassePadrao, Long>, JpaSpecificationExecutor<ClassePadrao> {}
