package com.banco.cadastro.br.service;

import com.banco.cadastro.br.service.dto.ArqMetadataDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.banco.cadastro.br.domain.ArqMetadata}.
 */
public interface ArqMetadataService {
    /**
     * Save a arqMetadata.
     *
     * @param arqMetadataDTO the entity to save.
     * @return the persisted entity.
     */
    ArqMetadataDTO save(ArqMetadataDTO arqMetadataDTO);

    /**
     * Updates a arqMetadata.
     *
     * @param arqMetadataDTO the entity to update.
     * @return the persisted entity.
     */
    ArqMetadataDTO update(ArqMetadataDTO arqMetadataDTO);

    /**
     * Partially updates a arqMetadata.
     *
     * @param arqMetadataDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArqMetadataDTO> partialUpdate(ArqMetadataDTO arqMetadataDTO);

    /**
     * Get the "id" arqMetadata.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArqMetadataDTO> findOne(Long id);

    /**
     * Delete the "id" arqMetadata.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
