package com.banco.cadastro.br.service;

import com.banco.cadastro.br.domain.*; // for static metamodels
import com.banco.cadastro.br.domain.ArquivoByte;
import com.banco.cadastro.br.repository.ArquivoByteRepository;
import com.banco.cadastro.br.service.criteria.ArquivoByteCriteria;
import com.banco.cadastro.br.service.dto.ArquivoByteDTO;
import com.banco.cadastro.br.service.mapper.ArquivoByteMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ArquivoByte} entities in the database.
 * The main input is a {@link ArquivoByteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ArquivoByteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArquivoByteQueryService extends QueryService<ArquivoByte> {

    private static final Logger LOG = LoggerFactory.getLogger(ArquivoByteQueryService.class);

    private final ArquivoByteRepository arquivoByteRepository;

    private final ArquivoByteMapper arquivoByteMapper;

    public ArquivoByteQueryService(ArquivoByteRepository arquivoByteRepository, ArquivoByteMapper arquivoByteMapper) {
        this.arquivoByteRepository = arquivoByteRepository;
        this.arquivoByteMapper = arquivoByteMapper;
    }

    /**
     * Return a {@link Page} of {@link ArquivoByteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArquivoByteDTO> findByCriteria(ArquivoByteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArquivoByte> specification = createSpecification(criteria);
        return arquivoByteRepository.findAll(specification, page).map(arquivoByteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArquivoByteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ArquivoByte> specification = createSpecification(criteria);
        return arquivoByteRepository.count(specification);
    }

    /**
     * Function to convert {@link ArquivoByteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ArquivoByte> createSpecification(ArquivoByteCriteria criteria) {
        Specification<ArquivoByte> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ArquivoByte_.id));
            }
            if (criteria.getArqMetadataId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getArqMetadataId(), root ->
                        root.join(ArquivoByte_.arqMetadata, JoinType.LEFT).get(ArqMetadata_.id)
                    )
                );
            }
        }
        return specification;
    }
}
