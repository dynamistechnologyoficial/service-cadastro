package com.banco.cadastro.br.service;

import com.banco.cadastro.br.domain.*; // for static metamodels
import com.banco.cadastro.br.domain.ArqMetadata;
import com.banco.cadastro.br.repository.ArqMetadataRepository;
import com.banco.cadastro.br.service.criteria.ArqMetadataCriteria;
import com.banco.cadastro.br.service.dto.ArqMetadataDTO;
import com.banco.cadastro.br.service.mapper.ArqMetadataMapper;
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
 * Service for executing complex queries for {@link ArqMetadata} entities in the database.
 * The main input is a {@link ArqMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ArqMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArqMetadataQueryService extends QueryService<ArqMetadata> {

    private static final Logger LOG = LoggerFactory.getLogger(ArqMetadataQueryService.class);

    private final ArqMetadataRepository arqMetadataRepository;

    private final ArqMetadataMapper arqMetadataMapper;

    public ArqMetadataQueryService(ArqMetadataRepository arqMetadataRepository, ArqMetadataMapper arqMetadataMapper) {
        this.arqMetadataRepository = arqMetadataRepository;
        this.arqMetadataMapper = arqMetadataMapper;
    }

    /**
     * Return a {@link Page} of {@link ArqMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArqMetadataDTO> findByCriteria(ArqMetadataCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArqMetadata> specification = createSpecification(criteria);
        return arqMetadataRepository.findAll(specification, page).map(arqMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArqMetadataCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ArqMetadata> specification = createSpecification(criteria);
        return arqMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link ArqMetadataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ArqMetadata> createSpecification(ArqMetadataCriteria criteria) {
        Specification<ArqMetadata> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ArqMetadata_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), ArqMetadata_.nome));
            }
            if (criteria.getObs() != null) {
                specification = specification.and(buildStringSpecification(criteria.getObs(), ArqMetadata_.obs));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ArqMetadata_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ArqMetadata_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ArqMetadata_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ArqMetadata_.lastModifiedDate));
            }
        }
        return specification;
    }
}
