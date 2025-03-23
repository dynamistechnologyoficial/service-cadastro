package com.banco.cadastro.br.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 * A DadoPadraoPK.
 */
@Embeddable
public class DadoPadraoPK implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @NotNull
    @Column(name = "idDado", nullable = false, insertable = false, updatable = false)
    private Long idDado;

    @NotNull
    @Column(name = "classePadraoId", nullable = false, insertable = false, updatable = false)
    private Long classePadraoId;
    
	public DadoPadraoPK(@NotNull Long idDado, @NotNull Long classePadraoId) {
		super();
		this.idDado = idDado;
		this.classePadraoId = classePadraoId;
	}
	
	public DadoPadraoPK() {
		
	}

	public Long getIdDado() {
		return idDado;
	}
	
	public DadoPadraoPK idDado(Long idDado) {
        this.setIdDado(idDado);
        return this;
    }

	public void setIdDado(Long idDado) {
		this.idDado = idDado;
	}

	public Long getClassePadraoId() {
		return classePadraoId;
	}
	
    public DadoPadraoPK classePadraoId(Long classePadraoId) {
        this.setClassePadraoId(classePadraoId);
        return this;
    }

	public void setClassePadraoId(Long classePadraoId) {
		this.classePadraoId = classePadraoId;
	}
    
    
}
