package com.triippztech.guildo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.triippztech.guildo.domain.enumeration.Activity;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.ServerLogItem} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.ServerLogItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /server-log-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ServerLogItemCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Activity
     */
    public static class ActivityFilter extends Filter<Activity> {

        public ActivityFilter() {
        }

        public ActivityFilter(ActivityFilter filter) {
            super(filter);
        }

        @Override
        public ActivityFilter copy() {
            return new ActivityFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private ActivityFilter activity;

    private LongFilter channelId;

    private StringFilter channelName;

    private InstantFilter time;

    private LongFilter userId;

    private StringFilter userName;

    private LongFilter guildId;

    private LongFilter serverItemGuildServerId;

    public ServerLogItemCriteria(){
    }

    public ServerLogItemCriteria(ServerLogItemCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.activity = other.activity == null ? null : other.activity.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
        this.channelName = other.channelName == null ? null : other.channelName.copy();
        this.time = other.time == null ? null : other.time.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.serverItemGuildServerId = other.serverItemGuildServerId == null ? null : other.serverItemGuildServerId.copy();
    }

    @Override
    public ServerLogItemCriteria copy() {
        return new ServerLogItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public ActivityFilter getActivity() {
        return activity;
    }

    public void setActivity(ActivityFilter activity) {
        this.activity = activity;
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

    public InstantFilter getTime() {
        return time;
    }

    public void setTime(InstantFilter time) {
        this.time = time;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public StringFilter getUserName() {
        return userName;
    }

    public void setUserName(StringFilter userName) {
        this.userName = userName;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getServerItemGuildServerId() {
        return serverItemGuildServerId;
    }

    public void setServerItemGuildServerId(LongFilter serverItemGuildServerId) {
        this.serverItemGuildServerId = serverItemGuildServerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ServerLogItemCriteria that = (ServerLogItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(activity, that.activity) &&
            Objects.equals(channelId, that.channelId) &&
            Objects.equals(channelName, that.channelName) &&
            Objects.equals(time, that.time) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(serverItemGuildServerId, that.serverItemGuildServerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        activity,
        channelId,
        channelName,
        time,
        userId,
        userName,
        guildId,
        serverItemGuildServerId
        );
    }

    @Override
    public String toString() {
        return "ServerLogItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (activity != null ? "activity=" + activity + ", " : "") +
                (channelId != null ? "channelId=" + channelId + ", " : "") +
                (channelName != null ? "channelName=" + channelName + ", " : "") +
                (time != null ? "time=" + time + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (userName != null ? "userName=" + userName + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (serverItemGuildServerId != null ? "serverItemGuildServerId=" + serverItemGuildServerId + ", " : "") +
            "}";
    }

}
