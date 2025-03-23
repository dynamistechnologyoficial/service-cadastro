package com.banco.cadastro.br.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.banco.cadastro.br.domain.EntidadeTeste} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EntidadeTesteDTO implements Serializable {

    private Long id;

    private String nome;

    private LocalDate dtnascimento;

    private Boolean ativooo;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDtnascimento() {
        return dtnascimento;
    }

    public void setDtnascimento(LocalDate dtnascimento) {
        this.dtnascimento = dtnascimento;
    }

    public Boolean getAtivooo() {
        return ativooo;
    }

    public void setAtivooo(Boolean ativooo) {
        this.ativooo = ativooo;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntidadeTesteDTO)) {
            return false;
        }

        EntidadeTesteDTO entidadeTesteDTO = (EntidadeTesteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, entidadeTesteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EntidadeTesteDTO{" +
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
