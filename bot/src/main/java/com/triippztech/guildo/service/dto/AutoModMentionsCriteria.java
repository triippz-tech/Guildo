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
 * Criteria class for the {@link com.triippztech.guildo.domain.AutoModMentions} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.AutoModMentionsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auto-mod-mentions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AutoModMentionsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter maxMentions;

    private IntegerFilter maxMsgLines;

    private IntegerFilter maxRoleMentions;

    public AutoModMentionsCriteria(){
    }

    public AutoModMentionsCriteria(AutoModMentionsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.maxMentions = other.maxMentions == null ? null : other.maxMentions.copy();
        this.maxMsgLines = other.maxMsgLines == null ? null : other.maxMsgLines.copy();
        this.maxRoleMentions = other.maxRoleMentions == null ? null : other.maxRoleMentions.copy();
    }

    @Override
    public AutoModMentionsCriteria copy() {
        return new AutoModMentionsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getMaxMentions() {
        return maxMentions;
    }

    public void setMaxMentions(IntegerFilter maxMentions) {
        this.maxMentions = maxMentions;
    }

    public IntegerFilter getMaxMsgLines() {
        return maxMsgLines;
    }

    public void setMaxMsgLines(IntegerFilter maxMsgLines) {
        this.maxMsgLines = maxMsgLines;
    }

    public IntegerFilter getMaxRoleMentions() {
        return maxRoleMentions;
    }

    public void setMaxRoleMentions(IntegerFilter maxRoleMentions) {
        this.maxRoleMentions = maxRoleMentions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AutoModMentionsCriteria that = (AutoModMentionsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(maxMentions, that.maxMentions) &&
            Objects.equals(maxMsgLines, that.maxMsgLines) &&
            Objects.equals(maxRoleMentions, that.maxRoleMentions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        maxMentions,
        maxMsgLines,
        maxRoleMentions
        );
    }

    @Override
    public String toString() {
        return "AutoModMentionsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (maxMentions != null ? "maxMentions=" + maxMentions + ", " : "") +
                (maxMsgLines != null ? "maxMsgLines=" + maxMsgLines + ", " : "") +
                (maxRoleMentions != null ? "maxRoleMentions=" + maxRoleMentions + ", " : "") +
            "}";
    }

}
