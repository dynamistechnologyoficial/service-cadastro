package com.banco.cadastro.br.repository;

import com.banco.cadastro.br.domain.ArquivoByte;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArquivoByte entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArquivoByteRepository extends JpaRepository<ArquivoByte, Long>, JpaSpecificationExecutor<ArquivoByte> {}
