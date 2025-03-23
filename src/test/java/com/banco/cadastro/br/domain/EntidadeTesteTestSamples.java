package com.banco.cadastro.br.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EntidadeTesteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EntidadeTeste getEntidadeTesteSample1() {
        return new EntidadeTeste().id(1L).nome("nome1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static EntidadeTeste getEntidadeTesteSample2() {
        return new EntidadeTeste().id(2L).nome("nome2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static EntidadeTeste getEntidadeTesteRandomSampleGenerator() {
        return new EntidadeTeste()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
