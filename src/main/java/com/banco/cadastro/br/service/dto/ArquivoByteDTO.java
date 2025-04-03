package com.banco.cadastro.br.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.banco.cadastro.br.domain.ArquivoByte} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArquivoByteDTO implements Serializable {

    @NotNull
    private Long id;

    @Lob
    private byte[] arquivo;

    private ArqMetadataDTO arqMetadata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getArquivo() {
        return arquivo;
    }

    public void setArquivo(byte[] arquivo) {
        this.arquivo = arquivo;
    }

    public ArqMetadataDTO getArqMetadata() {
        return arqMetadata;
    }

    public void setArqMetadata(ArqMetadataDTO arqMetadata) {
        this.arqMetadata = arqMetadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ArquivoByteDTO)) {
            return false;
        }

        ArquivoByteDTO arquivoByteDTO = (ArquivoByteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, arquivoByteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoByteDTO{" +
            "id=" + getId() +
            ", arquivo='" + getArquivo() + "'" +
            ", arqMetadata=" + getArqMetadata() +
            "}";
    }
}
