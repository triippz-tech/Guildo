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
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildPoll} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildPollResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-polls?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildPollCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter pollName;

    private StringFilter description;

    private LongFilter textChannelId;

    private LocalDateFilter finishTime;

    private BooleanFilter completed;

    private LongFilter guildId;

    private LongFilter pollItemsId;

    private LongFilter pollServerId;

    public GuildPollCriteria(){
    }

    public GuildPollCriteria(GuildPollCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.pollName = other.pollName == null ? null : other.pollName.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.textChannelId = other.textChannelId == null ? null : other.textChannelId.copy();
        this.finishTime = other.finishTime == null ? null : other.finishTime.copy();
        this.completed = other.completed == null ? null : other.completed.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.pollItemsId = other.pollItemsId == null ? null : other.pollItemsId.copy();
        this.pollServerId = other.pollServerId == null ? null : other.pollServerId.copy();
    }

    @Override
    public GuildPollCriteria copy() {
        return new GuildPollCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPollName() {
        return pollName;
    }

    public void setPollName(StringFilter pollName) {
        this.pollName = pollName;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getTextChannelId() {
        return textChannelId;
    }

    public void setTextChannelId(LongFilter textChannelId) {
        this.textChannelId = textChannelId;
    }

    public LocalDateFilter getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateFilter finishTime) {
        this.finishTime = finishTime;
    }

    public BooleanFilter getCompleted() {
        return completed;
    }

    public void setCompleted(BooleanFilter completed) {
        this.completed = completed;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getPollItemsId() {
        return pollItemsId;
    }

    public void setPollItemsId(LongFilter pollItemsId) {
        this.pollItemsId = pollItemsId;
    }

    public LongFilter getPollServerId() {
        return pollServerId;
    }

    public void setPollServerId(LongFilter pollServerId) {
        this.pollServerId = pollServerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildPollCriteria that = (GuildPollCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(pollName, that.pollName) &&
            Objects.equals(description, that.description) &&
            Objects.equals(textChannelId, that.textChannelId) &&
            Objects.equals(finishTime, that.finishTime) &&
            Objects.equals(completed, that.completed) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(pollItemsId, that.pollItemsId) &&
            Objects.equals(pollServerId, that.pollServerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        pollName,
        description,
        textChannelId,
        finishTime,
        completed,
        guildId,
        pollItemsId,
        pollServerId
        );
    }

    @Override
    public String toString() {
        return "GuildPollCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (pollName != null ? "pollName=" + pollName + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (textChannelId != null ? "textChannelId=" + textChannelId + ", " : "") +
                (finishTime != null ? "finishTime=" + finishTime + ", " : "") +
                (completed != null ? "completed=" + completed + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (pollItemsId != null ? "pollItemsId=" + pollItemsId + ", " : "") +
                (pollServerId != null ? "pollServerId=" + pollServerId + ", " : "") +
            "}";
    }

}
