package com.triippztech.guildo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.triippztech.guildo.domain.enumeration.GuildServerLevel;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildServer} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildServerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-servers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildServerCriteria implements Serializable, Criteria {
    /**
     * Class for filtering GuildServerLevel
     */
    public static class GuildServerLevelFilter extends Filter<GuildServerLevel> {

        public GuildServerLevelFilter() {
        }

        public GuildServerLevelFilter(GuildServerLevelFilter filter) {
            super(filter);
        }

        @Override
        public GuildServerLevelFilter copy() {
            return new GuildServerLevelFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter guildId;

    private StringFilter guildName;

    private StringFilter icon;

    private LongFilter owner;

    private GuildServerLevelFilter serverLevel;

    private LongFilter guildProfileId;

    private LongFilter applicationFormId;

    private LongFilter guildSettingsId;

    private LongFilter welcomeMessageId;

    private LongFilter serverPollsId;

    private LongFilter guildAnnoucementsId;

    private LongFilter guildEventsId;

    private LongFilter giveAwaysId;

    private LongFilter modLogItemsId;

    private LongFilter serverLogItemsId;

    private LongFilter guildTempBansId;

    private LongFilter mutedUsersId;

    private LongFilter guildApplicationsId;

    public GuildServerCriteria(){
    }

    public GuildServerCriteria(GuildServerCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.guildName = other.guildName == null ? null : other.guildName.copy();
        this.icon = other.icon == null ? null : other.icon.copy();
        this.owner = other.owner == null ? null : other.owner.copy();
        this.serverLevel = other.serverLevel == null ? null : other.serverLevel.copy();
        this.guildProfileId = other.guildProfileId == null ? null : other.guildProfileId.copy();
        this.applicationFormId = other.applicationFormId == null ? null : other.applicationFormId.copy();
        this.guildSettingsId = other.guildSettingsId == null ? null : other.guildSettingsId.copy();
        this.welcomeMessageId = other.welcomeMessageId == null ? null : other.welcomeMessageId.copy();
        this.serverPollsId = other.serverPollsId == null ? null : other.serverPollsId.copy();
        this.guildAnnoucementsId = other.guildAnnoucementsId == null ? null : other.guildAnnoucementsId.copy();
        this.guildEventsId = other.guildEventsId == null ? null : other.guildEventsId.copy();
        this.giveAwaysId = other.giveAwaysId == null ? null : other.giveAwaysId.copy();
        this.modLogItemsId = other.modLogItemsId == null ? null : other.modLogItemsId.copy();
        this.serverLogItemsId = other.serverLogItemsId == null ? null : other.serverLogItemsId.copy();
        this.guildTempBansId = other.guildTempBansId == null ? null : other.guildTempBansId.copy();
        this.mutedUsersId = other.mutedUsersId == null ? null : other.mutedUsersId.copy();
        this.guildApplicationsId = other.guildApplicationsId == null ? null : other.guildApplicationsId.copy();
    }

    @Override
    public GuildServerCriteria copy() {
        return new GuildServerCriteria(this);
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

    public StringFilter getGuildName() {
        return guildName;
    }

    public void setGuildName(StringFilter guildName) {
        this.guildName = guildName;
    }

    public StringFilter getIcon() {
        return icon;
    }

    public void setIcon(StringFilter icon) {
        this.icon = icon;
    }

    public LongFilter getOwner() {
        return owner;
    }

    public void setOwner(LongFilter owner) {
        this.owner = owner;
    }

    public GuildServerLevelFilter getServerLevel() {
        return serverLevel;
    }

    public void setServerLevel(GuildServerLevelFilter serverLevel) {
        this.serverLevel = serverLevel;
    }

    public LongFilter getGuildProfileId() {
        return guildProfileId;
    }

    public void setGuildProfileId(LongFilter guildProfileId) {
        this.guildProfileId = guildProfileId;
    }

    public LongFilter getApplicationFormId() {
        return applicationFormId;
    }

    public void setApplicationFormId(LongFilter applicationFormId) {
        this.applicationFormId = applicationFormId;
    }

    public LongFilter getGuildSettingsId() {
        return guildSettingsId;
    }

    public void setGuildSettingsId(LongFilter guildSettingsId) {
        this.guildSettingsId = guildSettingsId;
    }

    public LongFilter getWelcomeMessageId() {
        return welcomeMessageId;
    }

    public void setWelcomeMessageId(LongFilter welcomeMessageId) {
        this.welcomeMessageId = welcomeMessageId;
    }

    public LongFilter getServerPollsId() {
        return serverPollsId;
    }

    public void setServerPollsId(LongFilter serverPollsId) {
        this.serverPollsId = serverPollsId;
    }

    public LongFilter getGuildAnnoucementsId() {
        return guildAnnoucementsId;
    }

    public void setGuildAnnoucementsId(LongFilter guildAnnoucementsId) {
        this.guildAnnoucementsId = guildAnnoucementsId;
    }

    public LongFilter getGuildEventsId() {
        return guildEventsId;
    }

    public void setGuildEventsId(LongFilter guildEventsId) {
        this.guildEventsId = guildEventsId;
    }

    public LongFilter getGiveAwaysId() {
        return giveAwaysId;
    }

    public void setGiveAwaysId(LongFilter giveAwaysId) {
        this.giveAwaysId = giveAwaysId;
    }

    public LongFilter getModLogItemsId() {
        return modLogItemsId;
    }

    public void setModLogItemsId(LongFilter modLogItemsId) {
        this.modLogItemsId = modLogItemsId;
    }

    public LongFilter getServerLogItemsId() {
        return serverLogItemsId;
    }

    public void setServerLogItemsId(LongFilter serverLogItemsId) {
        this.serverLogItemsId = serverLogItemsId;
    }

    public LongFilter getGuildTempBansId() {
        return guildTempBansId;
    }

    public void setGuildTempBansId(LongFilter guildTempBansId) {
        this.guildTempBansId = guildTempBansId;
    }

    public LongFilter getMutedUsersId() {
        return mutedUsersId;
    }

    public void setMutedUsersId(LongFilter mutedUsersId) {
        this.mutedUsersId = mutedUsersId;
    }

    public LongFilter getGuildApplicationsId() {
        return guildApplicationsId;
    }

    public void setGuildApplicationsId(LongFilter guildApplicationsId) {
        this.guildApplicationsId = guildApplicationsId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildServerCriteria that = (GuildServerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(guildName, that.guildName) &&
            Objects.equals(icon, that.icon) &&
            Objects.equals(owner, that.owner) &&
            Objects.equals(serverLevel, that.serverLevel) &&
            Objects.equals(guildProfileId, that.guildProfileId) &&
            Objects.equals(applicationFormId, that.applicationFormId) &&
            Objects.equals(guildSettingsId, that.guildSettingsId) &&
            Objects.equals(welcomeMessageId, that.welcomeMessageId) &&
            Objects.equals(serverPollsId, that.serverPollsId) &&
            Objects.equals(guildAnnoucementsId, that.guildAnnoucementsId) &&
            Objects.equals(guildEventsId, that.guildEventsId) &&
            Objects.equals(giveAwaysId, that.giveAwaysId) &&
            Objects.equals(modLogItemsId, that.modLogItemsId) &&
            Objects.equals(serverLogItemsId, that.serverLogItemsId) &&
            Objects.equals(guildTempBansId, that.guildTempBansId) &&
            Objects.equals(mutedUsersId, that.mutedUsersId) &&
            Objects.equals(guildApplicationsId, that.guildApplicationsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        guildId,
        guildName,
        icon,
        owner,
        serverLevel,
        guildProfileId,
        applicationFormId,
        guildSettingsId,
        welcomeMessageId,
        serverPollsId,
        guildAnnoucementsId,
        guildEventsId,
        giveAwaysId,
        modLogItemsId,
        serverLogItemsId,
        guildTempBansId,
        mutedUsersId,
        guildApplicationsId
        );
    }

    @Override
    public String toString() {
        return "GuildServerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (guildName != null ? "guildName=" + guildName + ", " : "") +
                (icon != null ? "icon=" + icon + ", " : "") +
                (owner != null ? "owner=" + owner + ", " : "") +
                (serverLevel != null ? "serverLevel=" + serverLevel + ", " : "") +
                (guildProfileId != null ? "guildProfileId=" + guildProfileId + ", " : "") +
                (applicationFormId != null ? "applicationFormId=" + applicationFormId + ", " : "") +
                (guildSettingsId != null ? "guildSettingsId=" + guildSettingsId + ", " : "") +
                (welcomeMessageId != null ? "welcomeMessageId=" + welcomeMessageId + ", " : "") +
                (serverPollsId != null ? "serverPollsId=" + serverPollsId + ", " : "") +
                (guildAnnoucementsId != null ? "guildAnnoucementsId=" + guildAnnoucementsId + ", " : "") +
                (guildEventsId != null ? "guildEventsId=" + guildEventsId + ", " : "") +
                (giveAwaysId != null ? "giveAwaysId=" + giveAwaysId + ", " : "") +
                (modLogItemsId != null ? "modLogItemsId=" + modLogItemsId + ", " : "") +
                (serverLogItemsId != null ? "serverLogItemsId=" + serverLogItemsId + ", " : "") +
                (guildTempBansId != null ? "guildTempBansId=" + guildTempBansId + ", " : "") +
                (mutedUsersId != null ? "mutedUsersId=" + mutedUsersId + ", " : "") +
                (guildApplicationsId != null ? "guildApplicationsId=" + guildApplicationsId + ", " : "") +
            "}";
    }

}
