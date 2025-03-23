package com.banco.cadastro.br.service;

import com.banco.cadastro.br.service.dto.DadoPadraoDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.banco.cadastro.br.domain.DadoPadrao}.
 */
public interface DadoPadraoService {
    /**
     * Save a dadoPadrao.
     *
     * @param dadoPadraoDTO the entity to save.
     * @return the persisted entity.
     */
    DadoPadraoDTO save(DadoPadraoDTO dadoPadraoDTO);

    /**
     * Updates a dadoPadrao.
     *
     * @param dadoPadraoDTO the entity to update.
     * @return the persisted entity.
     */
    DadoPadraoDTO update(DadoPadraoDTO dadoPadraoDTO);

    /**
     * Partially updates a dadoPadrao.
     *
     * @param dadoPadraoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DadoPadraoDTO> partialUpdate(DadoPadraoDTO dadoPadraoDTO);

    /**
     * Get all the dadoPadraos with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<DadoPadraoDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" dadoPadrao.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DadoPadraoDTO> findOne(Long idDados, Long classePadraoId);

    /**
     * Delete the "id" dadoPadrao.
     *
     * @param id the id of the entity.
     */
    void delete(Long idDados, Long classePadraoId);
}
