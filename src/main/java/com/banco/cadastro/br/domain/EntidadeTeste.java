package com.banco.cadastro.br.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.springframework.data.domain.Persistable;

/**
 * A EntidadeTeste.
 */
@Entity
@Table(name = "entidade_teste")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntidadeTeste extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "dtnascimento")
    private LocalDate dtnascimento;

    @Column(name = "ativooo")
    private Boolean ativooo;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EntidadeTeste id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public EntidadeTeste nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDtnascimento() {
        return this.dtnascimento;
    }

    public EntidadeTeste dtnascimento(LocalDate dtnascimento) {
        this.setDtnascimento(dtnascimento);
        return this;
    }

    public void setDtnascimento(LocalDate dtnascimento) {
        this.dtnascimento = dtnascimento;
    }

    public Boolean getAtivooo() {
        return this.ativooo;
    }

    public EntidadeTeste ativooo(Boolean ativooo) {
        this.setAtivooo(ativooo);
        return this;
    }

    public void setAtivooo(Boolean ativooo) {
        this.ativooo = ativooo;
    }

    // Inherited createdBy methods
    public EntidadeTeste createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public EntidadeTeste createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public EntidadeTeste lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public EntidadeTeste lastModifiedDate(Instant lastModifiedDate) {
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

    public EntidadeTeste setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntidadeTeste)) {
            return false;
        }
        return getId() != null && getId().equals(((EntidadeTeste) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntidadeTeste{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", dtnascimento='" + getDtnascimento() + "'" +
            ", ativooo='" + getAtivooo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
