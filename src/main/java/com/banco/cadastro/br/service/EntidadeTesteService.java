package com.banco.cadastro.br.service;

import com.banco.cadastro.br.service.dto.EntidadeTesteDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.banco.cadastro.br.domain.EntidadeTeste}.
 */
public interface EntidadeTesteService {
    /**
     * Save a entidadeTeste.
     *
     * @param entidadeTesteDTO the entity to save.
     * @return the persisted entity.
     */
    EntidadeTesteDTO save(EntidadeTesteDTO entidadeTesteDTO);

    /**
     * Updates a entidadeTeste.
     *
     * @param entidadeTesteDTO the entity to update.
     * @return the persisted entity.
     */
    EntidadeTesteDTO update(EntidadeTesteDTO entidadeTesteDTO);

    /**
     * Partially updates a entidadeTeste.
     *
     * @param entidadeTesteDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EntidadeTesteDTO> partialUpdate(EntidadeTesteDTO entidadeTesteDTO);

    /**
     * Get the "id" entidadeTeste.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EntidadeTesteDTO> findOne(Long id);

    /**
     * Delete the "id" entidadeTeste.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
