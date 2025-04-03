package com.banco.cadastro.br.repository;

import com.banco.cadastro.br.domain.ArqMetadata;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ArqMetadata entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArqMetadataRepository extends JpaRepository<ArqMetadata, Long>, JpaSpecificationExecutor<ArqMetadata> {}
