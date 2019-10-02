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
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildApplicationForm} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildApplicationFormResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-application-forms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildApplicationFormCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter guildId;

    public GuildApplicationFormCriteria(){
    }

    public GuildApplicationFormCriteria(GuildApplicationFormCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
    }

    @Override
    public GuildApplicationFormCriteria copy() {
        return new GuildApplicationFormCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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
        final GuildApplicationFormCriteria that = (GuildApplicationFormCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(guildId, that.guildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        guildId
        );
    }

    @Override
    public String toString() {
        return "GuildApplicationFormCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
            "}";
    }

}
