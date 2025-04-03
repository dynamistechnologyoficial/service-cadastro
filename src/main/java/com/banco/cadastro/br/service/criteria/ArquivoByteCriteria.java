package com.banco.cadastro.br.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.banco.cadastro.br.domain.ArquivoByte} entity. This class is used
 * in {@link com.banco.cadastro.br.web.rest.ArquivoByteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /arquivo-bytes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ArquivoByteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter ArqMetadataId;

    private Boolean distinct;

    public ArquivoByteCriteria() {}

    public ArquivoByteCriteria(ArquivoByteCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.ArqMetadataId = other.optionalArqMetadataId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ArquivoByteCriteria copy() {
        return new ArquivoByteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getArqMetadataId() {
        return ArqMetadataId;
    }

    public Optional<LongFilter> optionalArqMetadataId() {
        return Optional.ofNullable(ArqMetadataId);
    }

    public LongFilter ArqMetadataId() {
        if (ArqMetadataId == null) {
            setArqMetadataId(new LongFilter());
        }
        return ArqMetadataId;
    }

    public void setArqMetadataId(LongFilter ArqMetadataId) {
        this.ArqMetadataId = ArqMetadataId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ArquivoByteCriteria that = (ArquivoByteCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(ArqMetadataId, that.ArqMetadataId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ArqMetadataId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ArquivoByteCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalArqMetadataId().map(f -> "ArqMetadataId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
