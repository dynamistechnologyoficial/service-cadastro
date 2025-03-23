package com.banco.cadastro.br.service;

import com.banco.cadastro.br.domain.*; // for static metamodels
import com.banco.cadastro.br.domain.DadoPadrao;
import com.banco.cadastro.br.repository.DadoPadraoRepository;
import com.banco.cadastro.br.service.criteria.DadoPadraoCriteria;
import com.banco.cadastro.br.service.dto.DadoPadraoDTO;
import com.banco.cadastro.br.service.mapper.DadoPadraoMapper;
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
 * Service for executing complex queries for {@link DadoPadrao} entities in the database.
 * The main input is a {@link DadoPadraoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DadoPadraoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DadoPadraoQueryService extends QueryService<DadoPadrao> {

    private static final Logger LOG = LoggerFactory.getLogger(DadoPadraoQueryService.class);

    private final DadoPadraoRepository dadoPadraoRepository;

    private final DadoPadraoMapper dadoPadraoMapper;

    public DadoPadraoQueryService(DadoPadraoRepository dadoPadraoRepository, DadoPadraoMapper dadoPadraoMapper) {
        this.dadoPadraoRepository = dadoPadraoRepository;
        this.dadoPadraoMapper = dadoPadraoMapper;
    }

    /**
     * Return a {@link Page} of {@link DadoPadraoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DadoPadraoDTO> findByCriteria(DadoPadraoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DadoPadrao> specification = createSpecification(criteria);
        return dadoPadraoRepository.findAll(specification, page).map(dadoPadraoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DadoPadraoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DadoPadrao> specification = createSpecification(criteria);
        return dadoPadraoRepository.count(specification);
    }

    /**
     * Function to convert {@link DadoPadraoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DadoPadrao> createSpecification(DadoPadraoCriteria criteria) {
        Specification<DadoPadrao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getNome() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNome(), DadoPadrao_.nome));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), DadoPadrao_.descricao));
            }
            if (criteria.getAtivo() != null) {
                specification = specification.and(buildSpecification(criteria.getAtivo(), DadoPadrao_.ativo));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), DadoPadrao_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), DadoPadrao_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), DadoPadrao_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), DadoPadrao_.lastModifiedDate));
            }
            if (criteria.getIdDado() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdDado(), DadoPadrao_.idDado));
            }
            if (criteria.getClassePadraoId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getClassePadraoId(), root ->
                        root.join(DadoPadrao_.classePadrao, JoinType.LEFT).get(ClassePadrao_.id)
                    )
                );
            }
            if (criteria.getClassePadraoIdDependencia() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getClassePadraoIdDependencia(),
                            root -> root.join(DadoPadrao_.dadoPadraoDependencia, JoinType.LEFT).get(DadoPadrao_.classePadrao).get(ClassePadrao_.id)
                        )
                    );
            }
            if (criteria.getIdDadoDependencia() != null) {
                specification =
                    specification.and(
                    	buildSpecification(
                            criteria.getIdDadoDependencia(),
                            root -> root.join(DadoPadrao_.dadoPadraoDependencia, JoinType.LEFT).get(DadoPadrao_.idDado)
                        )
                    );
            }
        }
        return specification;
    }
}
