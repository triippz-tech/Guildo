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
 * Criteria class for the {@link com.triippztech.guildo.domain.ModerationLogItem} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.ModerationLogItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /moderation-log-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ModerationLogItemCriteria implements Serializable, Criteria {
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

    private LongFilter channelId;

    private StringFilter channelName;

    private LongFilter issuedById;

    private StringFilter issuedByName;

    private LongFilter issuedToId;

    private StringFilter issuedToName;

    private StringFilter reason;

    private InstantFilter time;

    private PunishmentTypeFilter moderationAction;

    private LongFilter guildId;

    private LongFilter modItemGuildServerId;

    public ModerationLogItemCriteria(){
    }

    public ModerationLogItemCriteria(ModerationLogItemCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.channelName = other.channelName == null ? null : other.channelName.copy();
        this.issuedById = other.issuedById == null ? null : other.issuedById.copy();
        this.issuedByName = other.issuedByName == null ? null : other.issuedByName.copy();
        this.issuedToId = other.issuedToId == null ? null : other.issuedToId.copy();
        this.issuedToName = other.issuedToName == null ? null : other.issuedToName.copy();
        this.reason = other.reason == null ? null : other.reason.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.moderationAction = other.moderationAction == null ? null : other.moderationAction.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.modItemGuildServerId = other.modItemGuildServerId == null ? null : other.modItemGuildServerId.copy();
    }

    @Override
    public ModerationLogItemCriteria copy() {
        return new ModerationLogItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getChannelId() {
        return channelId;
    }

    public void setChannelId(LongFilter channelId) {
        this.channelId = channelId;
    }

    public StringFilter getChannelName() {
        return channelName;
    }

    public void setChannelName(StringFilter channelName) {
        this.channelName = channelName;
    }

    public LongFilter getIssuedById() {
        return issuedById;
    }

    public void setIssuedById(LongFilter issuedById) {
        this.issuedById = issuedById;
    }

    public StringFilter getIssuedByName() {
        return issuedByName;
    }

    public void setIssuedByName(StringFilter issuedByName) {
        this.issuedByName = issuedByName;
    }

    public LongFilter getIssuedToId() {
        return issuedToId;
    }

    public void setIssuedToId(LongFilter issuedToId) {
        this.issuedToId = issuedToId;
    }

    public StringFilter getIssuedToName() {
        return issuedToName;
    }

    public void setIssuedToName(StringFilter issuedToName) {
        this.issuedToName = issuedToName;
    }

    public StringFilter getReason() {
        return reason;
    }

    public void setReason(StringFilter reason) {
        this.reason = reason;
    }

    public InstantFilter getTime() {
        return time;
    }

    public void setTime(InstantFilter time) {
        this.time = time;
    }

    public PunishmentTypeFilter getModerationAction() {
        return moderationAction;
    }

    public void setModerationAction(PunishmentTypeFilter moderationAction) {
        this.moderationAction = moderationAction;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getModItemGuildServerId() {
        return modItemGuildServerId;
    }

    public void setModItemGuildServerId(LongFilter modItemGuildServerId) {
        this.modItemGuildServerId = modItemGuildServerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ModerationLogItemCriteria that = (ModerationLogItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(channelName, that.channelName) &&
            Objects.equals(issuedById, that.issuedById) &&
            Objects.equals(issuedByName, that.issuedByName) &&
            Objects.equals(issuedToId, that.issuedToId) &&
            Objects.equals(issuedToName, that.issuedToName) &&
            Objects.equals(reason, that.reason) &&
            Objects.equals(time, that.time) &&
            Objects.equals(moderationAction, that.moderationAction) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(modItemGuildServerId, that.modItemGuildServerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        channelId,
        channelName,
        issuedById,
        issuedByName,
        issuedToId,
        issuedToName,
        reason,
        time,
        moderationAction,
        guildId,
        modItemGuildServerId
        );
    }

    @Override
    public String toString() {
        return "ModerationLogItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (channelId != null ? "channelId=" + channelId + ", " : "") +
                (channelName != null ? "channelName=" + channelName + ", " : "") +
                (issuedById != null ? "issuedById=" + issuedById + ", " : "") +
                (issuedByName != null ? "issuedByName=" + issuedByName + ", " : "") +
                (issuedToId != null ? "issuedToId=" + issuedToId + ", " : "") +
                (issuedToName != null ? "issuedToName=" + issuedToName + ", " : "") +
                (reason != null ? "reason=" + reason + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (moderationAction != null ? "moderationAction=" + moderationAction + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (modItemGuildServerId != null ? "modItemGuildServerId=" + modItemGuildServerId + ", " : "") +
            "}";
    }

}
