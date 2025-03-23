package com.banco.cadastro.br.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.banco.cadastro.br.domain.DadoPadrao} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DadoPadraoDTO implements Serializable {

	private Long idDado;
    
    private Long classePadraoId;

    @NotNull
    private String nome;

    private String descricao;

    private Boolean ativo;
    
    private ClassePadraoDTO classePadraoDto;
    
    private DadoPadraoDTO dadoPadraoDependenciaDto;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    @NotNull
    private ClassePadraoDTO classePadrao;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
    
    public Long getIdDado() {
		return idDado;
	}

	public void setIdDado(Long idDado) {
		this.idDado = idDado;
	}

	public Long getClassePadraoId() {
		return classePadraoId;
	}

	public void setClassePadraoId(Long classePadraoId) {
		this.classePadraoId = classePadraoId;
	}

	public ClassePadraoDTO getClassePadraoDto() {
		return classePadraoDto;
	}

	public void setClassePadraoDto(ClassePadraoDTO classePadraoDto) {
		this.classePadraoDto = classePadraoDto;
	}

	public DadoPadraoDTO getDadoPadraoDependenciaDto() {
		return dadoPadraoDependenciaDto;
	}

	public void setDadoPadraoDependenciaDto(DadoPadraoDTO dadoPadraoDependenciaDto) {
		this.dadoPadraoDependenciaDto = dadoPadraoDependenciaDto;
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

    public ClassePadraoDTO getClassePadrao() {
        return classePadrao;
    }

    public void setClassePadrao(ClassePadraoDTO classePadrao) {
        this.classePadrao = classePadrao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DadoPadraoDTO)) {
            return false;
        }

        DadoPadraoDTO dadoPadraoDTO = (DadoPadraoDTO) o;
        if (this.idDado == null || this.classePadraoId == null) {
            return false;
        }
        return Objects.equals(this.idDado, dadoPadraoDTO.idDado) && Objects.equals(this.classePadraoId, dadoPadraoDTO.classePadraoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.idDado, this.classePadraoId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DadoPadraoDTO{" +
        	"idDados=" + getIdDado() +
            ", classePadraoId='" + getClassePadraoId() + "'" +
            ", nome='" + getNome() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ativo='" + getAtivo() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
//            ", classePadrao=" + getClassePadrao() +
            "}";
    }
}
