package com.banco.cadastro.br.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ArquivoByteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ArquivoByte getArquivoByteSample1() {
        return new ArquivoByte().id(1L);
    }

    public static ArquivoByte getArquivoByteSample2() {
        return new ArquivoByte().id(2L);
    }

    public static ArquivoByte getArquivoByteRandomSampleGenerator() {
        return new ArquivoByte().id(longCount.incrementAndGet());
    }
}
