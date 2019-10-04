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
 * Criteria class for the {@link com.triippztech.guildo.domain.Mute} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.MuteResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /mutes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MuteCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reason;

    private LongFilter guildId;

    private LongFilter userId;

    private InstantFilter endTime;

    private LongFilter mutedUserId;

    private LongFilter mutedGuildServerId;

    public MuteCriteria(){
    }

    public MuteCriteria(MuteCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.reason = other.reason == null ? null : other.reason.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.mutedUserId = other.mutedUserId == null ? null : other.mutedUserId.copy();
        this.mutedGuildServerId = other.mutedGuildServerId == null ? null : other.mutedGuildServerId.copy();
    }

    @Override
    public MuteCriteria copy() {
        return new MuteCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getReason() {
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
    }

    public LongFilter getMutedUserId() {
        return mutedUserId;
    }

    public void setMutedUserId(LongFilter mutedUserId) {
        this.mutedUserId = mutedUserId;
    }

    public LongFilter getMutedGuildServerId() {
        return mutedGuildServerId;
    }

    public void setMutedGuildServerId(LongFilter mutedGuildServerId) {
        this.mutedGuildServerId = mutedGuildServerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MuteCriteria that = (MuteCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(mutedUserId, that.mutedUserId) &&
            Objects.equals(mutedGuildServerId, that.mutedGuildServerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reason,
        guildId,
        userId,
        endTime,
        mutedUserId,
        mutedGuildServerId
        );
    }

    @Override
    public String toString() {
        return "MuteCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reason != null ? "reason=" + reason + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (mutedUserId != null ? "mutedUserId=" + mutedUserId + ", " : "") +
                (mutedGuildServerId != null ? "mutedGuildServerId=" + mutedGuildServerId + ", " : "") +
            "}";
    }

}
