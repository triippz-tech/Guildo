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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildEvent} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildEventCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter eventName;

    private StringFilter eventImageUrl;

    private InstantFilter eventStart;

    private LongFilter guildId;

    private LongFilter eventGuildId;

    public GuildEventCriteria(){
    }

    public GuildEventCriteria(GuildEventCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.eventName = other.eventName == null ? null : other.eventName.copy();
        this.eventImageUrl = other.eventImageUrl == null ? null : other.eventImageUrl.copy();
        this.eventStart = other.eventStart == null ? null : other.eventStart.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.eventGuildId = other.eventGuildId == null ? null : other.eventGuildId.copy();
    }

    @Override
    public GuildEventCriteria copy() {
        return new GuildEventCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getEventName() {
        return eventName;
    }

    public void setEventName(StringFilter eventName) {
        this.eventName = eventName;
    }

    public StringFilter getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(StringFilter eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public InstantFilter getEventStart() {
        return eventStart;
    }

    public void setEventStart(InstantFilter eventStart) {
        this.eventStart = eventStart;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getEventGuildId() {
        return eventGuildId;
    }

    public void setEventGuildId(LongFilter eventGuildId) {
        this.eventGuildId = eventGuildId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildEventCriteria that = (GuildEventCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(eventName, that.eventName) &&
            Objects.equals(eventImageUrl, that.eventImageUrl) &&
            Objects.equals(eventStart, that.eventStart) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(eventGuildId, that.eventGuildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        eventName,
        eventImageUrl,
        eventStart,
        guildId,
        eventGuildId
        );
    }

    @Override
    public String toString() {
        return "GuildEventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (eventName != null ? "eventName=" + eventName + ", " : "") +
                (eventImageUrl != null ? "eventImageUrl=" + eventImageUrl + ", " : "") +
                (eventStart != null ? "eventStart=" + eventStart + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (eventGuildId != null ? "eventGuildId=" + eventGuildId + ", " : "") +
            "}";
    }

}
