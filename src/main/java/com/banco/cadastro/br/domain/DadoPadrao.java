package com.banco.cadastro.br.domain;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

/**
 * A DadoPadrao.
 */
@Entity
@Table(name = "DYCAD_DadoPadrao")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DadoPadrao extends AbstractAuditingEntity<DadoPadraoPK> implements Serializable, Persistable<DadoPadraoPK> {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DadoPadraoPK id;

    @NotNull
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "ativo")
    private Boolean ativo;
    
    @Column(name = "idDado", insertable = false, nullable = false, updatable = false)
    private Long idDado;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
   // @NotNull
	@JoinColumn(name = "classePadraoId", referencedColumnName = "id", insertable = false, updatable = false)
    @JsonIgnoreProperties(value = { "dadoPadraos" }, allowSetters = true)
    private ClassePadrao classePadrao;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumns({
		@JoinColumn(name = "idDadoDependencia", referencedColumnName = "idDado", insertable = false, nullable = true, updatable = false),
		@JoinColumn(name = "classePadraoIdDependencia", referencedColumnName = "classePadraoId", insertable = false, nullable = true, updatable = false), })
    private DadoPadrao dadoPadraoDependencia;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public DadoPadraoPK getId() {
        return this.id;
    }

    public DadoPadrao id(DadoPadraoPK id) {
        this.setId(id);
        return this;
    }

    public void setId(DadoPadraoPK id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public DadoPadrao nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public DadoPadrao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public DadoPadrao ativo(Boolean ativo) {
        this.setAtivo(ativo);
        return this;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public Long getIdDado() {
        return this.idDado;
    }

    public DadoPadrao idDado(Long idDado) {
        this.setIdDado(idDado);
        return this;
    }

    public void setIdDado(Long idDado) {
        this.idDado = idDado;
    }

    public ClassePadrao getClassePadrao() {
        return this.classePadrao;
    }

    public void setClassePadrao(ClassePadrao classePadrao) {
        this.classePadrao = classePadrao;
    }

    public DadoPadrao classePadrao(ClassePadrao classePadrao) {
        this.setClassePadrao(classePadrao);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    
    public DadoPadrao getDadoPadraoDependencia() {
		return dadoPadraoDependencia;
	}

	public void setDadoPadraoDependencia(DadoPadrao dadoPadraoDependencia) {
		this.dadoPadraoDependencia = dadoPadraoDependencia;
	}

    // Inherited createdBy methods
    public DadoPadrao createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public DadoPadrao createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public DadoPadrao lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public DadoPadrao lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @org.springframework.data.annotation.Transient
    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public DadoPadrao setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DadoPadrao)) {
            return false;
        }
        return getId() != null && ((DadoPadrao) o).getId() !=null
        		&& getId().getClassePadraoId().equals(((DadoPadrao) o).getId().getClassePadraoId())
        		&& getId().getIdDado().equals(((DadoPadrao) o).getId().getIdDado());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DadoPadrao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
