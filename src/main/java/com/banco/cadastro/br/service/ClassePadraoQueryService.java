package com.banco.cadastro.br.service;

import com.banco.cadastro.br.domain.*; // for static metamodels
import com.banco.cadastro.br.domain.ClassePadrao;
import com.banco.cadastro.br.repository.ClassePadraoRepository;
import com.banco.cadastro.br.service.criteria.ClassePadraoCriteria;
import com.banco.cadastro.br.service.dto.ClassePadraoDTO;
import com.banco.cadastro.br.service.mapper.ClassePadraoMapper;
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
 * Service for executing complex queries for {@link ClassePadrao} entities in the database.
 * The main input is a {@link ClassePadraoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ClassePadraoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClassePadraoQueryService extends QueryService<ClassePadrao> {

    private static final Logger LOG = LoggerFactory.getLogger(ClassePadraoQueryService.class);

    private final ClassePadraoRepository classePadraoRepository;

    private final ClassePadraoMapper classePadraoMapper;

    public ClassePadraoQueryService(ClassePadraoRepository classePadraoRepository, ClassePadraoMapper classePadraoMapper) {
        this.classePadraoRepository = classePadraoRepository;
        this.classePadraoMapper = classePadraoMapper;
    }

    /**
     * Return a {@link Page} of {@link ClassePadraoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassePadraoDTO> findByCriteria(ClassePadraoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ClassePadrao> specification = createSpecification(criteria);
        return classePadraoRepository.findAll(specification, page).map(classePadraoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClassePadraoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ClassePadrao> specification = createSpecification(criteria);
        return classePadraoRepository.count(specification);
    }

    /**
     * Function to convert {@link ClassePadraoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ClassePadrao> createSpecification(ClassePadraoCriteria criteria) {
        Specification<ClassePadrao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ClassePadrao_.id));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), ClassePadrao_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), ClassePadrao_.descricao));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ClassePadrao_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ClassePadrao_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), ClassePadrao_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), ClassePadrao_.lastModifiedDate));
            }
            if (criteria.getDadoPadraoId() != null && criteria.getId() !=null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDadoPadraoId(),
                            root -> root.join(ClassePadrao_.dadoPadraos, JoinType.LEFT)
                            .get(DadoPadrao_.id).get(DadoPadraoPK_.idDado)
                        )
                    ).and(
                        buildSpecification(
                            criteria.getId(),
                            root -> {return root.join(ClassePadrao_.dadoPadraos, JoinType.LEFT)
                            		.get(DadoPadrao_.id).get(DadoPadraoPK_.classePadraoId);
                            }
                        )
                    );
            }
        }
        return specification;
    }
}
