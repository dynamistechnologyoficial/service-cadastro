package com.banco.cadastro.br.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.domain.Persistable;

/**
 * A ClassePadrao.
 */
@Entity
@Table(name = "classe_padrao")
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassePadrao extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nome", nullable = false, unique = true)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @org.springframework.data.annotation.Transient
    @Transient
    private boolean isPersisted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "classePadrao")
    @JsonIgnoreProperties(value = { "classePadrao" }, allowSetters = true)
    private Set<DadoPadrao> dadoPadraos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ClassePadrao id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public ClassePadrao nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public ClassePadrao descricao(String descricao) {
        this.setDescricao(descricao);
        return this;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Inherited createdBy methods
    public ClassePadrao createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public ClassePadrao createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public ClassePadrao lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public ClassePadrao lastModifiedDate(Instant lastModifiedDate) {
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

    public ClassePadrao setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<DadoPadrao> getDadoPadraos() {
        return this.dadoPadraos;
    }

    public void setDadoPadraos(Set<DadoPadrao> dadoPadraos) {
        if (this.dadoPadraos != null) {
            this.dadoPadraos.forEach(i -> i.setClassePadrao(null));
        }
        if (dadoPadraos != null) {
            dadoPadraos.forEach(i -> i.setClassePadrao(this));
        }
        this.dadoPadraos = dadoPadraos;
    }

    public ClassePadrao dadoPadraos(Set<DadoPadrao> dadoPadraos) {
        this.setDadoPadraos(dadoPadraos);
        return this;
    }

    public ClassePadrao addDadoPadrao(DadoPadrao dadoPadrao) {
        this.dadoPadraos.add(dadoPadrao);
        dadoPadrao.setClassePadrao(this);
        return this;
    }

    public ClassePadrao removeDadoPadrao(DadoPadrao dadoPadrao) {
        this.dadoPadraos.remove(dadoPadrao);
        dadoPadrao.setClassePadrao(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassePadrao)) {
            return false;
        }
        return getId() != null && getId().equals(((ClassePadrao) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassePadrao{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
