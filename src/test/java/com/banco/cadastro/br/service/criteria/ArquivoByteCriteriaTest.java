package com.banco.cadastro.br.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ArquivoByteCriteriaTest {

    @Test
    void newArquivoByteCriteriaHasAllFiltersNullTest() {
        var arquivoByteCriteria = new ArquivoByteCriteria();
        assertThat(arquivoByteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void arquivoByteCriteriaFluentMethodsCreatesFiltersTest() {
        var arquivoByteCriteria = new ArquivoByteCriteria();

        setAllFilters(arquivoByteCriteria);

        assertThat(arquivoByteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void arquivoByteCriteriaCopyCreatesNullFilterTest() {
        var arquivoByteCriteria = new ArquivoByteCriteria();
        var copy = arquivoByteCriteria.copy();

        assertThat(arquivoByteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(arquivoByteCriteria)
        );
    }

    @Test
    void arquivoByteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var arquivoByteCriteria = new ArquivoByteCriteria();
        setAllFilters(arquivoByteCriteria);

        var copy = arquivoByteCriteria.copy();

        assertThat(arquivoByteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(arquivoByteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var arquivoByteCriteria = new ArquivoByteCriteria();

        assertThat(arquivoByteCriteria).hasToString("ArquivoByteCriteria{}");
    }

    private static void setAllFilters(ArquivoByteCriteria arquivoByteCriteria) {
        arquivoByteCriteria.id();
        arquivoByteCriteria.ArqMetadataId();
        arquivoByteCriteria.distinct();
    }

    private static Condition<ArquivoByteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getArqMetadataId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ArquivoByteCriteria> copyFiltersAre(ArquivoByteCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getArqMetadataId(), copy.getArqMetadataId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
