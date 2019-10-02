package com.triippztech.guildo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.triippztech.guildo.domain.enumeration.PunishmentType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.Punishment} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.PunishmentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /punishments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PunishmentCriteria implements Serializable, Criteria {
    /**
     * Class for filtering PunishmentType
     */
    public static class PunishmentTypeFilter extends Filter<PunishmentType> {

        public PunishmentTypeFilter() {
        }

        public PunishmentTypeFilter(PunishmentTypeFilter filter) {
            super(filter);
        }

        @Override
        public PunishmentTypeFilter copy() {
            return new PunishmentTypeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter maxStrikes;

    private PunishmentTypeFilter action;

    private InstantFilter punishmentDuration;

    private LongFilter guildId;

    public PunishmentCriteria(){
    }

    public PunishmentCriteria(PunishmentCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.maxStrikes = other.maxStrikes == null ? null : other.maxStrikes.copy();
        this.action = other.action == null ? null : other.action.copy();
        this.punishmentDuration = other.punishmentDuration == null ? null : other.punishmentDuration.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
    }

    @Override
    public PunishmentCriteria copy() {
        return new PunishmentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getMaxStrikes() {
        return maxStrikes;
    }

    public void setMaxStrikes(IntegerFilter maxStrikes) {
        this.maxStrikes = maxStrikes;
    }

    public PunishmentTypeFilter getAction() {
        return action;
    }

    public void setAction(PunishmentTypeFilter action) {
        this.action = action;
    }

    public InstantFilter getPunishmentDuration() {
        return punishmentDuration;
    }

    public void setPunishmentDuration(InstantFilter punishmentDuration) {
        this.punishmentDuration = punishmentDuration;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PunishmentCriteria that = (PunishmentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(maxStrikes, that.maxStrikes) &&
            Objects.equals(action, that.action) &&
            Objects.equals(punishmentDuration, that.punishmentDuration) &&
            Objects.equals(guildId, that.guildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        maxStrikes,
        action,
        punishmentDuration,
        guildId
        );
    }

    @Override
    public String toString() {
        return "PunishmentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (maxStrikes != null ? "maxStrikes=" + maxStrikes + ", " : "") +
                (action != null ? "action=" + action + ", " : "") +
                (punishmentDuration != null ? "punishmentDuration=" + punishmentDuration + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
            "}";
    }

}
