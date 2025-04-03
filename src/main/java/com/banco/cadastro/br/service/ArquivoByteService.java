package com.banco.cadastro.br.service;

import com.banco.cadastro.br.service.dto.ArquivoByteDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.banco.cadastro.br.domain.ArquivoByte}.
 */
public interface ArquivoByteService {
    /**
     * Save a arquivoByte.
     *
     * @param arquivoByteDTO the entity to save.
     * @return the persisted entity.
     */
    ArquivoByteDTO save(ArquivoByteDTO arquivoByteDTO);

    /**
     * Updates a arquivoByte.
     *
     * @param arquivoByteDTO the entity to update.
     * @return the persisted entity.
     */
    ArquivoByteDTO update(ArquivoByteDTO arquivoByteDTO);

    /**
     * Partially updates a arquivoByte.
     *
     * @param arquivoByteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ArquivoByteDTO> partialUpdate(ArquivoByteDTO arquivoByteDTO);

    /**
     * Get the "id" arquivoByte.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ArquivoByteDTO> findOne(Long id);

    /**
     * Delete the "id" arquivoByte.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
