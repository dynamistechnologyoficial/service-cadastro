package com.banco.cadastro.br.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * A ArquivoByte.
 */
@Entity
@Table(name = "DYCAD_ArquivoByte")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArquivoByte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "arquivo", nullable = false)
    private byte[] arquivo;
    
    @JsonIgnoreProperties(value = { "arquivoByte" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", referencedColumnName = "id",unique = true)
    private ArqMetadata arqMetadata;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ArquivoByte id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getArquivo() {
        return this.arquivo;
    }

    public ArquivoByte arquivo(byte[] arquivo) {
        this.setArquivo(arquivo);
        return this;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public ArqMetadata getArqMetadata() {
        return this.arqMetadata;
    }

    public void setArqMetadata(ArqMetadata arqMetadata) {
        this.arqMetadata = arqMetadata;
    }

    public ArquivoByte arqMetadata(ArqMetadata arqMetadata) {
        this.setArqMetadata(arqMetadata);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArquivoByte)) {
            return false;
        }
        return getId() != null && getId().equals(((ArquivoByte) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoByte{" +
            "id=" + getId() +
            ", arquivo='" + getArquivo() + "'" +
            "}";
    }
}
