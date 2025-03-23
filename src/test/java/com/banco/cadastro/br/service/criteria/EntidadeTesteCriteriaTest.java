package com.banco.cadastro.br.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class EntidadeTesteCriteriaTest {

    @Test
    void newEntidadeTesteCriteriaHasAllFiltersNullTest() {
        var entidadeTesteCriteria = new EntidadeTesteCriteria();
        assertThat(entidadeTesteCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void entidadeTesteCriteriaFluentMethodsCreatesFiltersTest() {
        var entidadeTesteCriteria = new EntidadeTesteCriteria();

        setAllFilters(entidadeTesteCriteria);

        assertThat(entidadeTesteCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void entidadeTesteCriteriaCopyCreatesNullFilterTest() {
        var entidadeTesteCriteria = new EntidadeTesteCriteria();
        var copy = entidadeTesteCriteria.copy();

        assertThat(entidadeTesteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(entidadeTesteCriteria)
        );
    }

    @Test
    void entidadeTesteCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var entidadeTesteCriteria = new EntidadeTesteCriteria();
        setAllFilters(entidadeTesteCriteria);

        var copy = entidadeTesteCriteria.copy();

        assertThat(entidadeTesteCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(entidadeTesteCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var entidadeTesteCriteria = new EntidadeTesteCriteria();

        assertThat(entidadeTesteCriteria).hasToString("EntidadeTesteCriteria{}");
    }

    private static void setAllFilters(EntidadeTesteCriteria entidadeTesteCriteria) {
        entidadeTesteCriteria.id();
        entidadeTesteCriteria.nome();
        entidadeTesteCriteria.dtnascimento();
        entidadeTesteCriteria.ativooo();
        entidadeTesteCriteria.createdBy();
        entidadeTesteCriteria.createdDate();
        entidadeTesteCriteria.lastModifiedBy();
        entidadeTesteCriteria.lastModifiedDate();
        entidadeTesteCriteria.distinct();
    }

    private static Condition<EntidadeTesteCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getDtnascimento()) &&
                condition.apply(criteria.getAtivooo()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<EntidadeTesteCriteria> copyFiltersAre(
        EntidadeTesteCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getDtnascimento(), copy.getDtnascimento()) &&
                condition.apply(criteria.getAtivooo(), copy.getAtivooo()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
