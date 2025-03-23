package com.banco.cadastro.br.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClassePadraoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ClassePadrao getClassePadraoSample1() {
        return new ClassePadrao().id(1L).nome("nome1").descricao("descricao1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static ClassePadrao getClassePadraoSample2() {
        return new ClassePadrao().id(2L).nome("nome2").descricao("descricao2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static ClassePadrao getClassePadraoRandomSampleGenerator() {
        return new ClassePadrao()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .descricao(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
