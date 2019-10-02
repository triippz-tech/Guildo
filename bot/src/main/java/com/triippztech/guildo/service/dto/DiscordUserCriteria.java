package com.triippztech.guildo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.triippztech.guildo.domain.enumeration.DiscordUserLevel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.DiscordUser} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.DiscordUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /discord-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DiscordUserCriteria implements Serializable, Criteria {
    /**
     * Class for filtering DiscordUserLevel
     */
    public static class DiscordUserLevelFilter extends Filter<DiscordUserLevel> {

        public DiscordUserLevelFilter() {
        }

        public DiscordUserLevelFilter(DiscordUserLevelFilter filter) {
            super(filter);
        }

        @Override
        public DiscordUserLevelFilter copy() {
            return new DiscordUserLevelFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter userId;

    private StringFilter userName;

    private StringFilter icon;

    private IntegerFilter commandsIssued;

    private BooleanFilter blacklisted;

    private DiscordUserLevelFilter userLevel;

    private LongFilter userProfileId;

    private LongFilter userTempBansId;

    private LongFilter userMutesId;

    private LongFilter userApplicationsId;

    public DiscordUserCriteria(){
    }

    public DiscordUserCriteria(DiscordUserCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.userName = other.userName == null ? null : other.userName.copy();
        this.icon = other.icon == null ? null : other.icon.copy();
        this.commandsIssued = other.commandsIssued == null ? null : other.commandsIssued.copy();
        this.blacklisted = other.blacklisted == null ? null : other.blacklisted.copy();
        this.userLevel = other.userLevel == null ? null : other.userLevel.copy();
        this.userProfileId = other.userProfileId == null ? null : other.userProfileId.copy();
        this.userTempBansId = other.userTempBansId == null ? null : other.userTempBansId.copy();
        this.userMutesId = other.userMutesId == null ? null : other.userMutesId.copy();
        this.userApplicationsId = other.userApplicationsId == null ? null : other.userApplicationsId.copy();
    }

    @Override
    public DiscordUserCriteria copy() {
        return new DiscordUserCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
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

    public StringFilter getIcon() {
        return icon;
    }

    public void setIcon(StringFilter icon) {
        this.icon = icon;
    }

    public IntegerFilter getCommandsIssued() {
        return commandsIssued;
    }

    public void setCommandsIssued(IntegerFilter commandsIssued) {
        this.commandsIssued = commandsIssued;
    }

    public BooleanFilter getBlacklisted() {
        return blacklisted;
    }

    public void setBlacklisted(BooleanFilter blacklisted) {
        this.blacklisted = blacklisted;
    }

    public DiscordUserLevelFilter getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(DiscordUserLevelFilter userLevel) {
        this.userLevel = userLevel;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public LongFilter getUserTempBansId() {
        return userTempBansId;
    }

    public void setUserTempBansId(LongFilter userTempBansId) {
        this.userTempBansId = userTempBansId;
    }

    public LongFilter getUserMutesId() {
        return userMutesId;
    }

    public void setUserMutesId(LongFilter userMutesId) {
        this.userMutesId = userMutesId;
    }

    public LongFilter getUserApplicationsId() {
        return userApplicationsId;
    }

    public void setUserApplicationsId(LongFilter userApplicationsId) {
        this.userApplicationsId = userApplicationsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscordUserCriteria that = (DiscordUserCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(userName, that.userName) &&
            Objects.equals(icon, that.icon) &&
            Objects.equals(commandsIssued, that.commandsIssued) &&
            Objects.equals(blacklisted, that.blacklisted) &&
            Objects.equals(userLevel, that.userLevel) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(userTempBansId, that.userTempBansId) &&
            Objects.equals(userMutesId, that.userMutesId) &&
            Objects.equals(userApplicationsId, that.userApplicationsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        userId,
        userName,
        icon,
        commandsIssued,
        blacklisted,
        userLevel,
        userProfileId,
        userTempBansId,
        userMutesId,
        userApplicationsId
        );
    }

    @Override
    public String toString() {
        return "DiscordUserCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (userName != null ? "userName=" + userName + ", " : "") +
                (icon != null ? "icon=" + icon + ", " : "") +
                (commandsIssued != null ? "commandsIssued=" + commandsIssued + ", " : "") +
                (blacklisted != null ? "blacklisted=" + blacklisted + ", " : "") +
                (userLevel != null ? "userLevel=" + userLevel + ", " : "") +
                (userProfileId != null ? "userProfileId=" + userProfileId + ", " : "") +
                (userTempBansId != null ? "userTempBansId=" + userTempBansId + ", " : "") +
                (userMutesId != null ? "userMutesId=" + userMutesId + ", " : "") +
                (userApplicationsId != null ? "userApplicationsId=" + userApplicationsId + ", " : "") +
            "}";
    }

}
