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
 * Criteria class for the {@link com.triippztech.guildo.domain.TempBan} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.TempBanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /temp-bans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class TempBanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter reason;

    private InstantFilter endTime;

    private LongFilter guildId;

    private LongFilter userId;

    private LongFilter bannedUserId;

    private LongFilter tempBanGuildServerId;

    public TempBanCriteria(){
    }

    public TempBanCriteria(TempBanCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.reason = other.reason == null ? null : other.reason.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.bannedUserId = other.bannedUserId == null ? null : other.bannedUserId.copy();
        this.tempBanGuildServerId = other.tempBanGuildServerId == null ? null : other.tempBanGuildServerId.copy();
    }

    @Override
    public TempBanCriteria copy() {
        return new TempBanCriteria(this);
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

    public InstantFilter getEndTime() {
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
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

    public LongFilter getBannedUserId() {
        return bannedUserId;
    }

    public void setBannedUserId(LongFilter bannedUserId) {
        this.bannedUserId = bannedUserId;
    }

    public LongFilter getTempBanGuildServerId() {
        return tempBanGuildServerId;
    }

    public void setTempBanGuildServerId(LongFilter tempBanGuildServerId) {
        this.tempBanGuildServerId = tempBanGuildServerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TempBanCriteria that = (TempBanCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(bannedUserId, that.bannedUserId) &&
            Objects.equals(tempBanGuildServerId, that.tempBanGuildServerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        reason,
        endTime,
        guildId,
        userId,
        bannedUserId,
        tempBanGuildServerId
        );
    }

    @Override
    public String toString() {
        return "TempBanCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (reason != null ? "reason=" + reason + ", " : "") +
                (endTime != null ? "endTime=" + endTime + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (bannedUserId != null ? "bannedUserId=" + bannedUserId + ", " : "") +
                (tempBanGuildServerId != null ? "tempBanGuildServerId=" + tempBanGuildServerId + ", " : "") +
            "}";
    }

}
