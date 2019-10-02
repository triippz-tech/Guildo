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
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildServerSettings} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildServerSettingsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-server-settings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildServerSettingsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prefix;

    private StringFilter timezone;

    private BooleanFilter raidModeEnabled;

    private StringFilter raidModeReason;

    private IntegerFilter maxStrikes;

    private BooleanFilter acceptingApplications;

    private LongFilter autoModConfigId;

    private LongFilter punishmentConfigId;

    public GuildServerSettingsCriteria(){
    }

    public GuildServerSettingsCriteria(GuildServerSettingsCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.prefix = other.prefix == null ? null : other.prefix.copy();
        this.timezone = other.timezone == null ? null : other.timezone.copy();
        this.raidModeEnabled = other.raidModeEnabled == null ? null : other.raidModeEnabled.copy();
        this.raidModeReason = other.raidModeReason == null ? null : other.raidModeReason.copy();
        this.maxStrikes = other.maxStrikes == null ? null : other.maxStrikes.copy();
        this.acceptingApplications = other.acceptingApplications == null ? null : other.acceptingApplications.copy();
        this.autoModConfigId = other.autoModConfigId == null ? null : other.autoModConfigId.copy();
        this.punishmentConfigId = other.punishmentConfigId == null ? null : other.punishmentConfigId.copy();
    }

    @Override
    public GuildServerSettingsCriteria copy() {
        return new GuildServerSettingsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPrefix() {
        return prefix;
    }

    public void setPrefix(StringFilter prefix) {
        this.prefix = prefix;
    }

    public StringFilter getTimezone() {
        return timezone;
    }

    public void setTimezone(StringFilter timezone) {
        this.timezone = timezone;
    }

    public BooleanFilter getRaidModeEnabled() {
        return raidModeEnabled;
    }

    public void setRaidModeEnabled(BooleanFilter raidModeEnabled) {
        this.raidModeEnabled = raidModeEnabled;
    }

    public StringFilter getRaidModeReason() {
        return raidModeReason;
    }

    public void setRaidModeReason(StringFilter raidModeReason) {
        this.raidModeReason = raidModeReason;
    }

    public IntegerFilter getMaxStrikes() {
        return maxStrikes;
    }

    public void setMaxStrikes(IntegerFilter maxStrikes) {
        this.maxStrikes = maxStrikes;
    }

    public BooleanFilter getAcceptingApplications() {
        return acceptingApplications;
    }

    public void setAcceptingApplications(BooleanFilter acceptingApplications) {
        this.acceptingApplications = acceptingApplications;
    }

    public LongFilter getAutoModConfigId() {
        return autoModConfigId;
    }

    public void setAutoModConfigId(LongFilter autoModConfigId) {
        this.autoModConfigId = autoModConfigId;
    }

    public LongFilter getPunishmentConfigId() {
        return punishmentConfigId;
    }

    public void setPunishmentConfigId(LongFilter punishmentConfigId) {
        this.punishmentConfigId = punishmentConfigId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildServerSettingsCriteria that = (GuildServerSettingsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(prefix, that.prefix) &&
            Objects.equals(timezone, that.timezone) &&
            Objects.equals(raidModeEnabled, that.raidModeEnabled) &&
            Objects.equals(raidModeReason, that.raidModeReason) &&
            Objects.equals(maxStrikes, that.maxStrikes) &&
            Objects.equals(acceptingApplications, that.acceptingApplications) &&
            Objects.equals(autoModConfigId, that.autoModConfigId) &&
            Objects.equals(punishmentConfigId, that.punishmentConfigId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        prefix,
        timezone,
        raidModeEnabled,
        raidModeReason,
        maxStrikes,
        acceptingApplications,
        autoModConfigId,
        punishmentConfigId
        );
    }

    @Override
    public String toString() {
        return "GuildServerSettingsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (prefix != null ? "prefix=" + prefix + ", " : "") +
                (timezone != null ? "timezone=" + timezone + ", " : "") +
                (raidModeEnabled != null ? "raidModeEnabled=" + raidModeEnabled + ", " : "") +
                (raidModeReason != null ? "raidModeReason=" + raidModeReason + ", " : "") +
                (maxStrikes != null ? "maxStrikes=" + maxStrikes + ", " : "") +
                (acceptingApplications != null ? "acceptingApplications=" + acceptingApplications + ", " : "") +
                (autoModConfigId != null ? "autoModConfigId=" + autoModConfigId + ", " : "") +
                (punishmentConfigId != null ? "punishmentConfigId=" + punishmentConfigId + ", " : "") +
            "}";
    }

}
