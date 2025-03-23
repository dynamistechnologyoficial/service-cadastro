package com.banco.cadastro.br.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DadoPadraoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DadoPadrao getDadoPadraoSample1() {
        return new DadoPadrao().id(newDadoPadraoPK(1L,1L)).nome("nome1").descricao("descricao1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static DadoPadrao getDadoPadraoSample2() {
        return new DadoPadrao().id(newDadoPadraoPK(2L,2L)).nome("nome2").descricao("descricao2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static DadoPadrao getDadoPadraoRandomSampleGenerator() {
        return new DadoPadrao()
            .id(newDadoPadraoPK(longCount.incrementAndGet(),longCount.incrementAndGet()))
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
    
    private static DadoPadraoPK newDadoPadraoPK(Long idDados, Long classePadraoId ) {
		DadoPadraoPK dadoPadraoPK = new DadoPadraoPK();
    	dadoPadraoPK.idDado(idDados);
    	dadoPadraoPK.classePadraoId(classePadraoId);
    	return dadoPadraoPK;
	}
}
