package com.banco.cadastro.br.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DadoPadraoCriteriaTest {

    @Test
    void newDadoPadraoCriteriaHasAllFiltersNullTest() {
        var dadoPadraoCriteria = new DadoPadraoCriteria();
        assertThat(dadoPadraoCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void dadoPadraoCriteriaFluentMethodsCreatesFiltersTest() {
        var dadoPadraoCriteria = new DadoPadraoCriteria();

        setAllFilters(dadoPadraoCriteria);

        assertThat(dadoPadraoCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void dadoPadraoCriteriaCopyCreatesNullFilterTest() {
        var dadoPadraoCriteria = new DadoPadraoCriteria();
        var copy = dadoPadraoCriteria.copy();

        assertThat(dadoPadraoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(dadoPadraoCriteria)
        );
    }

    @Test
    void dadoPadraoCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var dadoPadraoCriteria = new DadoPadraoCriteria();
        setAllFilters(dadoPadraoCriteria);

        var copy = dadoPadraoCriteria.copy();

        assertThat(dadoPadraoCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(dadoPadraoCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var dadoPadraoCriteria = new DadoPadraoCriteria();

        assertThat(dadoPadraoCriteria).hasToString("DadoPadraoCriteria{}");
    }

    private static void setAllFilters(DadoPadraoCriteria dadoPadraoCriteria) {
    	dadoPadraoCriteria.classePadraoId();
    	dadoPadraoCriteria.idDadoDependencia();
        dadoPadraoCriteria.classePadraoIdDependencia();
        dadoPadraoCriteria.idDado();
        dadoPadraoCriteria.nome();
        dadoPadraoCriteria.descricao();
        dadoPadraoCriteria.ativo();
        dadoPadraoCriteria.createdBy();
        dadoPadraoCriteria.createdDate();
        dadoPadraoCriteria.lastModifiedBy();
        dadoPadraoCriteria.lastModifiedDate();
        dadoPadraoCriteria.classePadraoId();
        dadoPadraoCriteria.distinct();
    }

    private static Condition<DadoPadraoCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
	            condition.apply(criteria.getClassePadraoId()) &&
	            condition.apply(criteria.getIdDadoDependencia()) &&
	            condition.apply(criteria.getClassePadraoIdDependencia()) &&
	            condition.apply(criteria.getIdDado()) &&
                condition.apply(criteria.getNome()) &&
                condition.apply(criteria.getDescricao()) &&
                condition.apply(criteria.getAtivo()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getClassePadraoId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DadoPadraoCriteria> copyFiltersAre(DadoPadraoCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
	            condition.apply(criteria.getClassePadraoId(), copy.getClassePadraoId()) &&
	            condition.apply(criteria.getIdDadoDependencia(), copy.getIdDadoDependencia()) &&
	            condition.apply(criteria.getClassePadraoIdDependencia(), copy.getClassePadraoIdDependencia()) &&
	            condition.apply(criteria.getIdDado(), copy.getIdDado()) &&
                condition.apply(criteria.getNome(), copy.getNome()) &&
                condition.apply(criteria.getDescricao(), copy.getDescricao()) &&
                condition.apply(criteria.getAtivo(), copy.getAtivo()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getClassePadraoId(), copy.getClassePadraoId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
