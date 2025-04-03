package com.banco.cadastro.br.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ArqMetadataTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArqMetadata getArqMetadataSample1() {
        return new ArqMetadata().id(1L).nome("nome1").obs("obs1").createdBy("createdBy1").lastModifiedBy("lastModifiedBy1");
    }

    public static ArqMetadata getArqMetadataSample2() {
        return new ArqMetadata().id(2L).nome("nome2").obs("obs2").createdBy("createdBy2").lastModifiedBy("lastModifiedBy2");
    }

    public static ArqMetadata getArqMetadataRandomSampleGenerator() {
        return new ArqMetadata()
            .id(longCount.incrementAndGet())
            .nome(UUID.randomUUID().toString())
            .obs(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
