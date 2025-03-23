package com.banco.cadastro.br.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ClassePadraoCriteriaTest {

    @Test
    void newClassePadraoCriteriaHasAllFiltersNullTest() {
        var classePadraoCriteria = new ClassePadraoCriteria();
        assertThat(classePadraoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void classePadraoCriteriaFluentMethodsCreatesFiltersTest() {
        var classePadraoCriteria = new ClassePadraoCriteria();

        setAllFilters(classePadraoCriteria);

        assertThat(classePadraoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void classePadraoCriteriaCopyCreatesNullFilterTest() {
        var classePadraoCriteria = new ClassePadraoCriteria();
        var copy = classePadraoCriteria.copy();

        assertThat(classePadraoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(classePadraoCriteria)
        );
    }

    @Test
    void classePadraoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var classePadraoCriteria = new ClassePadraoCriteria();
        setAllFilters(classePadraoCriteria);

        var copy = classePadraoCriteria.copy();

        assertThat(classePadraoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(classePadraoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var classePadraoCriteria = new ClassePadraoCriteria();

        assertThat(classePadraoCriteria).hasToString("ClassePadraoCriteria{}");
    }

    private static void setAllFilters(ClassePadraoCriteria classePadraoCriteria) {
        classePadraoCriteria.id();
        classePadraoCriteria.nome();
        classePadraoCriteria.descricao();
        classePadraoCriteria.createdBy();
        classePadraoCriteria.createdDate();
        classePadraoCriteria.lastModifiedBy();
        classePadraoCriteria.lastModifiedDate();
        classePadraoCriteria.dadoPadraoId();
        classePadraoCriteria.distinct();
    }

    private static Condition<ClassePadraoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDadoPadraoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ClassePadraoCriteria> copyFiltersAre(
        ClassePadraoCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDadoPadraoId(), copy.getDadoPadraoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
