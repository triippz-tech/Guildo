package com.triippztech.guildo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.AutoModAntiDup} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.AutoModAntiDupResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auto-mod-anti-dups?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AutoModAntiDupCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter deleteThreshold;

    private IntegerFilter dupsToPunish;

    public AutoModAntiDupCriteria(){
    }

    public AutoModAntiDupCriteria(AutoModAntiDupCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.deleteThreshold = other.deleteThreshold == null ? null : other.deleteThreshold.copy();
        this.dupsToPunish = other.dupsToPunish == null ? null : other.dupsToPunish.copy();
    }

    @Override
    public AutoModAntiDupCriteria copy() {
        return new AutoModAntiDupCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getDeleteThreshold() {
        return deleteThreshold;
    }

    public void setDeleteThreshold(IntegerFilter deleteThreshold) {
        this.deleteThreshold = deleteThreshold;
    }

    public IntegerFilter getDupsToPunish() {
        return dupsToPunish;
    }

    public void setDupsToPunish(IntegerFilter dupsToPunish) {
        this.dupsToPunish = dupsToPunish;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AutoModAntiDupCriteria that = (AutoModAntiDupCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(deleteThreshold, that.deleteThreshold) &&
            Objects.equals(dupsToPunish, that.dupsToPunish);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        deleteThreshold,
        dupsToPunish
        );
    }

    @Override
    public String toString() {
        return "AutoModAntiDupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (deleteThreshold != null ? "deleteThreshold=" + deleteThreshold + ", " : "") +
                (dupsToPunish != null ? "dupsToPunish=" + dupsToPunish + ", " : "") +
            "}";
    }

}
