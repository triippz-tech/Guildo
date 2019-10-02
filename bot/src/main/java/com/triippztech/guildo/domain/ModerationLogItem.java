package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import com.triippztech.guildo.domain.enumeration.PunishmentType;

/**
 * Represents a Log Entry of all Moderator\nspecific actions on Users (kicks, bans, etc.)\n@author Mark Tripoli
 */
@ApiModel(description = "Represents a Log Entry of all Moderator\nspecific actions on Users (kicks, bans, etc.)\n@author Mark Tripoli")
@Entity
@Table(name = "moderation_log_item")
public class ModerationLogItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @NotNull
    @Column(name = "channel_name", nullable = false)
    private String channelName;

    @NotNull
    @Column(name = "issued_by_id", nullable = false)
    private Long issuedById;

    @NotNull
    @Column(name = "issued_by_name", nullable = false)
    private String issuedByName;

    @NotNull
    @Column(name = "issued_to_id", nullable = false)
    private Long issuedToId;

    @NotNull
    @Column(name = "issued_to_name", nullable = false)
    private String issuedToName;

    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason;

    @NotNull
    @Column(name = "time", nullable = false)
    private Instant time;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_action")
    private PunishmentType moderationAction;

    @Column(name = "guild_id")
    private Long guildId;

    @ManyToOne
    @JsonIgnoreProperties("modLogItems")
    private GuildServer modItemGuildServer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChannelId() {
        return channelId;
    }

    public ModerationLogItem channelId(Long channelId) {
        this.channelId = channelId;
        return this;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public ModerationLogItem channelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getIssuedById() {
        return issuedById;
    }

    public ModerationLogItem issuedById(Long issuedById) {
        this.issuedById = issuedById;
        return this;
    }

    public void setIssuedById(Long issuedById) {
        this.issuedById = issuedById;
    }

    public String getIssuedByName() {
        return issuedByName;
    }

    public ModerationLogItem issuedByName(String issuedByName) {
        this.issuedByName = issuedByName;
        return this;
    }

    public void setIssuedByName(String issuedByName) {
        this.issuedByName = issuedByName;
    }

    public Long getIssuedToId() {
        return issuedToId;
    }

    public ModerationLogItem issuedToId(Long issuedToId) {
        this.issuedToId = issuedToId;
        return this;
    }

    public void setIssuedToId(Long issuedToId) {
        this.issuedToId = issuedToId;
    }

    public String getIssuedToName() {
        return issuedToName;
    }

    public ModerationLogItem issuedToName(String issuedToName) {
        this.issuedToName = issuedToName;
        return this;
    }

    public void setIssuedToName(String issuedToName) {
        this.issuedToName = issuedToName;
    }

    public String getReason() {
        return reason;
    }

    public ModerationLogItem reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getTime() {
        return time;
    }

    public ModerationLogItem time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public PunishmentType getModerationAction() {
        return moderationAction;
    }

    public ModerationLogItem moderationAction(PunishmentType moderationAction) {
        this.moderationAction = moderationAction;
        return this;
    }

    public void setModerationAction(PunishmentType moderationAction) {
        this.moderationAction = moderationAction;
    }

    public Long getGuildId() {
        return guildId;
    }

    public ModerationLogItem guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public GuildServer getModItemGuildServer() {
        return modItemGuildServer;
    }

    public ModerationLogItem modItemGuildServer(GuildServer guildServer) {
        this.modItemGuildServer = guildServer;
        return this;
    }

    public void setModItemGuildServer(GuildServer guildServer) {
        this.modItemGuildServer = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModerationLogItem)) {
            return false;
        }
        return id != null && id.equals(((ModerationLogItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ModerationLogItem{" +
            "id=" + getId() +
            ", channelId=" + getChannelId() +
            ", channelName='" + getChannelName() + "'" +
            ", issuedById=" + getIssuedById() +
            ", issuedByName='" + getIssuedByName() + "'" +
            ", issuedToId=" + getIssuedToId() +
            ", issuedToName='" + getIssuedToName() + "'" +
            ", reason='" + getReason() + "'" +
            ", time='" + getTime() + "'" +
            ", moderationAction='" + getModerationAction() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }

    public String formatFileStr() {
        DateTimeFormatter formatter =
            DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
                .withLocale( Locale.US )
                .withZone( ZoneId.systemDefault() );
        return String.format(
            "%-18s | %-10s | %-25s | %-25s | %-25s | %s\n"
            , formatter.format( getTime() )
            , getModerationAction()
            , getIssuedToName()
            , getIssuedByName()
            , getChannelName()
            , getReason()
        );
    }

    public static String logHeader() {
        return String.format(
            "%-18s | %-10s | %-25s | %-25s | %-25s | %s\n%-18s | %-10s | %-25s | %-25s | %-25s | %-25s\n"
            , "Time"
            , "Action"
            , "Issued To"
            , "Issued By"
            , "Channel Name"
            , "Reason"
            , "__________________"
            , "__________"
            , "_________________________"
            , "_________________________"
            , "_________________________"
            , "_________________________"
        );
    }
}
