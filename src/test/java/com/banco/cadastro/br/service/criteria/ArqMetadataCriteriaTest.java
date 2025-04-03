package com.banco.cadastro.br.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ArqMetadataCriteriaTest {

    @Test
    void newArqMetadataCriteriaHasAllFiltersNullTest() {
        var arqMetadataCriteria = new ArqMetadataCriteria();
        assertThat(arqMetadataCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void arqMetadataCriteriaFluentMethodsCreatesFiltersTest() {
        var arqMetadataCriteria = new ArqMetadataCriteria();

        setAllFilters(arqMetadataCriteria);

        assertThat(arqMetadataCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void arqMetadataCriteriaCopyCreatesNullFilterTest() {
        var arqMetadataCriteria = new ArqMetadataCriteria();
        var copy = arqMetadataCriteria.copy();

        assertThat(arqMetadataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(arqMetadataCriteria)
        );
    }

    @Test
    void arqMetadataCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var arqMetadataCriteria = new ArqMetadataCriteria();
        setAllFilters(arqMetadataCriteria);

        var copy = arqMetadataCriteria.copy();

        assertThat(arqMetadataCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(arqMetadataCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var arqMetadataCriteria = new ArqMetadataCriteria();

        assertThat(arqMetadataCriteria).hasToString("ArqMetadataCriteria{}");
    }

    private static void setAllFilters(ArqMetadataCriteria arqMetadataCriteria) {
        arqMetadataCriteria.id();
        arqMetadataCriteria.nome();
        arqMetadataCriteria.obs();
        arqMetadataCriteria.createdBy();
        arqMetadataCriteria.createdDate();
        arqMetadataCriteria.lastModifiedBy();
        arqMetadataCriteria.lastModifiedDate();
        arqMetadataCriteria.arquivoByteId();
        arqMetadataCriteria.distinct();
    }

    private static Condition<ArqMetadataCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getObs()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getArquivoByteId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ArqMetadataCriteria> copyFiltersAre(ArqMetadataCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getObs(), copy.getObs()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getArquivoByteId(), copy.getArquivoByteId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
