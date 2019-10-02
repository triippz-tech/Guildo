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
 * Criteria class for the {@link com.triippztech.guildo.domain.AutoModAutoRaid} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.AutoModAutoRaidResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auto-mod-auto-raids?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AutoModAutoRaidCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BooleanFilter autoRaidEnabled;

    private IntegerFilter autoRaidTimeThreshold;

    public AutoModAutoRaidCriteria(){
    }

    public AutoModAutoRaidCriteria(AutoModAutoRaidCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.autoRaidEnabled = other.autoRaidEnabled == null ? null : other.autoRaidEnabled.copy();
        this.autoRaidTimeThreshold = other.autoRaidTimeThreshold == null ? null : other.autoRaidTimeThreshold.copy();
    }

    @Override
    public AutoModAutoRaidCriteria copy() {
        return new AutoModAutoRaidCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public BooleanFilter getAutoRaidEnabled() {
        return autoRaidEnabled;
    }

    public void setAutoRaidEnabled(BooleanFilter autoRaidEnabled) {
        this.autoRaidEnabled = autoRaidEnabled;
    }

    public IntegerFilter getAutoRaidTimeThreshold() {
        return autoRaidTimeThreshold;
    }

    public void setAutoRaidTimeThreshold(IntegerFilter autoRaidTimeThreshold) {
        this.autoRaidTimeThreshold = autoRaidTimeThreshold;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AutoModAutoRaidCriteria that = (AutoModAutoRaidCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(autoRaidEnabled, that.autoRaidEnabled) &&
            Objects.equals(autoRaidTimeThreshold, that.autoRaidTimeThreshold);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        autoRaidEnabled,
        autoRaidTimeThreshold
        );
    }

    @Override
    public String toString() {
        return "AutoModAutoRaidCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (autoRaidEnabled != null ? "autoRaidEnabled=" + autoRaidEnabled + ", " : "") +
                (autoRaidTimeThreshold != null ? "autoRaidTimeThreshold=" + autoRaidTimeThreshold + ", " : "") +
            "}";
    }

}
