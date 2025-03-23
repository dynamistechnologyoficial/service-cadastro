package com.banco.cadastro.br.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.banco.cadastro.br.domain.DadoPadrao;
import com.banco.cadastro.br.domain.DadoPadraoPK;

/**
 * Spring Data JPA repository for the DadoPadrao entity.
 */
@Repository
public interface DadoPadraoRepository extends JpaRepository<DadoPadrao, DadoPadraoPK>, JpaSpecificationExecutor<DadoPadrao> {
    default Optional<DadoPadrao> findOneWithEagerRelationships(DadoPadraoPK id) {
        return this.findOneWithToOneRelationships(id.getIdDado(), id.getClassePadraoId());
    }

    default List<DadoPadrao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DadoPadrao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select dadoPadrao from DadoPadrao dadoPadrao left join fetch dadoPadrao.classePadrao",
        countQuery = "select count(dadoPadrao) from DadoPadrao dadoPadrao"
    )
    Page<DadoPadrao> findAllWithToOneRelationships(Pageable pageable);

    @Query("select dadoPadrao from DadoPadrao dadoPadrao left join fetch dadoPadrao.classePadrao")
    List<DadoPadrao> findAllWithToOneRelationships();

    @Query("select dadoPadrao from DadoPadrao dadoPadrao left join fetch dadoPadrao.classePadrao"
    		+ " where dadoPadrao.idDado =:idDado and dadoPadrao.id.classePadraoId =:classePadraoId ")
    Optional<DadoPadrao> findOneWithToOneRelationships(@Param("idDado") Long idDado, @Param("classePadraoId") Long classePadraoId);
}
