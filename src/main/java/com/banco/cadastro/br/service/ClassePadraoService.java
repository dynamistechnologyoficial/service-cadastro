package com.banco.cadastro.br.service;

import com.banco.cadastro.br.service.dto.ClassePadraoDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.banco.cadastro.br.domain.ClassePadrao}.
 */
public interface ClassePadraoService {
    /**
     * Save a classePadrao.
     *
     * @param classePadraoDTO the entity to save.
     * @return the persisted entity.
     */
    ClassePadraoDTO save(ClassePadraoDTO classePadraoDTO);

    /**
     * Updates a classePadrao.
     *
     * @param classePadraoDTO the entity to update.
     * @return the persisted entity.
     */
    ClassePadraoDTO update(ClassePadraoDTO classePadraoDTO);

    /**
     * Partially updates a classePadrao.
     *
     * @param classePadraoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ClassePadraoDTO> partialUpdate(ClassePadraoDTO classePadraoDTO);

    /**
     * Get the "id" classePadrao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClassePadraoDTO> findOne(Long id);

    /**
     * Delete the "id" classePadrao.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
