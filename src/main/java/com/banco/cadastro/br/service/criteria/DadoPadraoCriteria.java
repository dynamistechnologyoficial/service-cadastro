package com.banco.cadastro.br.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import org.springdoc.core.annotations.ParameterObject;

import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.banco.cadastro.br.domain.DadoPadrao} entity. This class is used
 * in {@link com.banco.cadastro.br.web.rest.DadoPadraoResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /dado-padraos?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DadoPadraoCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private StringFilter nome;

    private StringFilter descricao;

    private BooleanFilter ativo;
    
    private LongFilter idDado;

    private LongFilter classePadraoId;
    
    private LongFilter idDadoDependencia;

    private LongFilter classePadraoIdDependencia;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private Boolean distinct;

    public DadoPadraoCriteria() {}

    public DadoPadraoCriteria(DadoPadraoCriteria other) {        
        this.nome= other.optionalNome().map(StringFilter::copy).orElse(null);
        this.descricao= other.optionalDescricao().map(StringFilter::copy).orElse(null);
        this.ativo= other.optionalAtivo().map(BooleanFilter::copy).orElse(null);
        this.classePadraoId= other.optionalClassePadraoId().map(LongFilter::copy).orElse(null);
        this.idDado= other.optionalIdDado().map(LongFilter::copy).orElse(null);
        this.classePadraoIdDependencia= other.optionalClassePadraoIdDependencia().map(LongFilter::copy).orElse(null);
        this.idDadoDependencia = other.optionalIdDadoDependencia().map(LongFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DadoPadraoCriteria copy() {
        return new DadoPadraoCriteria(this);
    }

    public StringFilter getNome() {
        return nome;
    }

    public Optional<StringFilter> optionalNome() {
        return Optional.ofNullable(nome);
    }

    public StringFilter nome() {
        if (nome == null) {
            setNome(new StringFilter());
        }
        return nome;
    }

    public void setNome(StringFilter nome) {
        this.nome = nome;
    }

    public StringFilter getDescricao() {
        return descricao;
    }

    public Optional<StringFilter> optionalDescricao() {
        return Optional.ofNullable(descricao);
    }

    public StringFilter descricao() {
        if (descricao == null) {
            setDescricao(new StringFilter());
        }
        return descricao;
    }

    public void setDescricao(StringFilter descricao) {
        this.descricao = descricao;
    }

    public BooleanFilter getAtivo() {
        return ativo;
    }

    public Optional<BooleanFilter> optionalAtivo() {
        return Optional.ofNullable(ativo);
    }

    public BooleanFilter ativo() {
        if (ativo == null) {
            setAtivo(new BooleanFilter());
        }
        return ativo;
    }

    public void setAtivo(BooleanFilter ativo) {
        this.ativo = ativo;
    }
    
    public LongFilter getIdDado() {
		return idDado;
	}
    
    public Optional<LongFilter> optionalIdDado() {
        return Optional.ofNullable(idDado);
    }
    
	public LongFilter idDado() {
        if (idDado == null) {
        	idDado = new LongFilter();
        }
        return idDado;
    }

	public void setIdDado(LongFilter idDado) {
		this.idDado = idDado;
	}
	

	public LongFilter getIdDadoDependencia() {
		return idDadoDependencia;
	}
	
	 public Optional<LongFilter> optionalIdDadoDependencia() {
	        return Optional.ofNullable(idDadoDependencia);
	    }
	
	public LongFilter idDadoDependencia() {
        if (idDadoDependencia == null) {
        	idDadoDependencia = new LongFilter();
        }
        return idDadoDependencia;
    }

	public void setIdDadoDependencia(LongFilter idDadoDependencia) {
		this.idDadoDependencia = idDadoDependencia;
	}

	public LongFilter getClassePadraoIdDependencia() {
		return classePadraoIdDependencia;
	}
	
	public Optional<LongFilter> optionalClassePadraoIdDependencia() {
        return Optional.ofNullable(classePadraoIdDependencia);
    }
	
	public LongFilter classePadraoIdDependencia() {
        if (classePadraoIdDependencia == null) {
        	classePadraoIdDependencia = new LongFilter();
        }
        return classePadraoIdDependencia;
    }

	public void setClassePadraoIdDependencia(LongFilter classePadraoIdDependencia) {
		this.classePadraoIdDependencia = classePadraoIdDependencia;
	}

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getClassePadraoId() {
        return classePadraoId;
    }

    public Optional<LongFilter> optionalClassePadraoId() {
        return Optional.ofNullable(classePadraoId);
    }

    public LongFilter classePadraoId() {
        if (classePadraoId == null) {
            setClassePadraoId(new LongFilter());
        }
        return classePadraoId;
    }

    public void setClassePadraoId(LongFilter classePadraoId) {
        this.classePadraoId = classePadraoId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DadoPadraoCriteria that = (DadoPadraoCriteria) o;
        return (
            Objects.equals(nome, that.nome) &&
            Objects.equals(descricao, that.descricao) &&
            Objects.equals(ativo, that.ativo) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(classePadraoId, that.classePadraoId) &&
            Objects.equals(idDado, that.idDado) &&
            Objects.equals(classePadraoIdDependencia, that.classePadraoIdDependencia) &&
            Objects.equals(idDadoDependencia, that.idDadoDependencia) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, descricao, ativo, idDado,classePadraoId, idDadoDependencia,classePadraoIdDependencia, createdBy, createdDate, lastModifiedBy, lastModifiedDate, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DadoPadraoCriteria{" +
            optionalNome().map(f -> "nome=" + f + ", ").orElse("") +
            optionalDescricao().map(f -> "descricao=" + f + ", ").orElse("") +
            optionalAtivo().map(f -> "ativo=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalClassePadraoId().map(f -> "classePadraoId=" + f + ", ").orElse("") +
            optionalIdDado().map(f -> "idDado=" + f + ", ").orElse("") +
            optionalClassePadraoIdDependencia().map(f -> "classePadraoIdDependencia=" + f + ", ").orElse("") +
            optionalIdDadoDependencia().map(f -> "idDadoDependencia=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
