package com.banco.cadastro.br.service;

import com.banco.cadastro.br.domain.*; // for static metamodels
import com.banco.cadastro.br.domain.EntidadeTeste;
import com.banco.cadastro.br.repository.EntidadeTesteRepository;
import com.banco.cadastro.br.service.criteria.EntidadeTesteCriteria;
import com.banco.cadastro.br.service.dto.EntidadeTesteDTO;
import com.banco.cadastro.br.service.mapper.EntidadeTesteMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link EntidadeTeste} entities in the database.
 * The main input is a {@link EntidadeTesteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EntidadeTesteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntidadeTesteQueryService extends QueryService<EntidadeTeste> {

    private static final Logger LOG = LoggerFactory.getLogger(EntidadeTesteQueryService.class);

    private final EntidadeTesteRepository entidadeTesteRepository;

    private final EntidadeTesteMapper entidadeTesteMapper;

    public EntidadeTesteQueryService(EntidadeTesteRepository entidadeTesteRepository, EntidadeTesteMapper entidadeTesteMapper) {
        this.entidadeTesteRepository = entidadeTesteRepository;
        this.entidadeTesteMapper = entidadeTesteMapper;
    }

    /**
     * Return a {@link Page} of {@link EntidadeTesteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EntidadeTesteDTO> findByCriteria(EntidadeTesteCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EntidadeTeste> specification = createSpecification(criteria);
        return entidadeTesteRepository.findAll(specification, page).map(entidadeTesteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EntidadeTesteCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EntidadeTeste> specification = createSpecification(criteria);
        return entidadeTesteRepository.count(specification);
    }

    /**
     * Function to convert {@link EntidadeTesteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EntidadeTeste> createSpecification(EntidadeTesteCriteria criteria) {
        Specification<EntidadeTeste> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EntidadeTeste_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), EntidadeTeste_.nome));
            }
            if (criteria.getDtnascimento() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDtnascimento(), EntidadeTeste_.dtnascimento));
            }
            if (criteria.getAtivooo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivooo(), EntidadeTeste_.ativooo));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), EntidadeTeste_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), EntidadeTeste_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), EntidadeTeste_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), EntidadeTeste_.lastModifiedDate));
            }
        }
        return specification;
    }
}
