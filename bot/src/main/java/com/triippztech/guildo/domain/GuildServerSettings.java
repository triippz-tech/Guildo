package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents the Guildo settings of a particular Guild\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the Guildo settings of a particular Guild\n@author Mark Tripoli")
@Entity
@Table(name = "guild_server_settings")
public class GuildServerSettings implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(max = 40)
    @Column(name = "prefix", length = 40)
    private String prefix;

    @Size(max = 32)
    @Column(name = "timezone", length = 32)
    private String timezone;

    @NotNull
    @Column(name = "raid_mode_enabled", nullable = false)
    private Boolean raidModeEnabled;

    @NotNull
    @Column(name = "raid_mode_reason", nullable = false)
    private String raidModeReason;

    @NotNull
    @Column(name = "max_strikes", nullable = false)
    private Integer maxStrikes;

    @NotNull
    @Column(name = "accepting_applications", nullable = false)
    private Boolean acceptingApplications;

    @OneToOne
    @JoinColumn(unique = true)
    private AutoModeration autoModConfig;

    @OneToOne
    @JoinColumn(unique = true)
    private Punishment punishmentConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrefix() {
        return prefix;
    }

    public GuildServerSettings prefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTimezone() {
        return timezone;
    }

    public GuildServerSettings timezone(String timezone) {
        this.timezone = timezone;
        return this;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean isRaidModeEnabled() {
        return raidModeEnabled;
    }

    public GuildServerSettings raidModeEnabled(Boolean raidModeEnabled) {
        this.raidModeEnabled = raidModeEnabled;
        return this;
    }

    public void setRaidModeEnabled(Boolean raidModeEnabled) {
        this.raidModeEnabled = raidModeEnabled;
    }

    public String getRaidModeReason() {
        return raidModeReason;
    }

    public GuildServerSettings raidModeReason(String raidModeReason) {
        this.raidModeReason = raidModeReason;
        return this;
    }

    public void setRaidModeReason(String raidModeReason) {
        this.raidModeReason = raidModeReason;
    }

    public Integer getMaxStrikes() {
        return maxStrikes;
    }

    public GuildServerSettings maxStrikes(Integer maxStrikes) {
        this.maxStrikes = maxStrikes;
        return this;
    }

    public void setMaxStrikes(Integer maxStrikes) {
        this.maxStrikes = maxStrikes;
    }

    public Boolean isAcceptingApplications() {
        return acceptingApplications;
    }

    public GuildServerSettings acceptingApplications(Boolean acceptingApplications) {
        this.acceptingApplications = acceptingApplications;
        return this;
    }

    public void setAcceptingApplications(Boolean acceptingApplications) {
        this.acceptingApplications = acceptingApplications;
    }

    public AutoModeration getAutoModConfig() {
        return autoModConfig;
    }

    public GuildServerSettings autoModConfig(AutoModeration autoModeration) {
        this.autoModConfig = autoModeration;
        return this;
    }

    public void setAutoModConfig(AutoModeration autoModeration) {
        this.autoModConfig = autoModeration;
    }

    public Punishment getPunishmentConfig() {
        return punishmentConfig;
    }

    public GuildServerSettings punishmentConfig(Punishment punishment) {
        this.punishmentConfig = punishment;
        return this;
    }

    public void setPunishmentConfig(Punishment punishment) {
        this.punishmentConfig = punishment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildServerSettings)) {
            return false;
        }
        return id != null && id.equals(((GuildServerSettings) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildServerSettings{" +
            "id=" + getId() +
            ", prefix='" + getPrefix() + "'" +
            ", timezone='" + getTimezone() + "'" +
            ", raidModeEnabled='" + isRaidModeEnabled() + "'" +
            ", raidModeReason='" + getRaidModeReason() + "'" +
            ", maxStrikes=" + getMaxStrikes() +
            ", acceptingApplications='" + isAcceptingApplications() + "'" +
            "}";
    }
}
