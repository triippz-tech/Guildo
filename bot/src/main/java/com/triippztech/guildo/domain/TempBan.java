package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents a temporary ban of a Discord user from a guild\n@author Mark Tripoli
 */
@ApiModel(description = "Represents a temporary ban of a Discord user from a guild\n@author Mark Tripoli")
@Entity
@Table(name = "temp_ban")
public class TempBan implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "reason", nullable = false)
    private String reason;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JsonIgnoreProperties("userTempBans")
    private DiscordUser bannedUser;

    @ManyToOne
    @JsonIgnoreProperties("guildTempBans")
    private GuildServer tempBanGuildServer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public TempBan reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public TempBan endTime(Instant endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Long getGuildId() {
        return guildId;
    }

    public TempBan guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getUserId() {
        return userId;
    }

    public TempBan userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DiscordUser getBannedUser() {
        return bannedUser;
    }

    public TempBan bannedUser(DiscordUser discordUser) {
        this.bannedUser = discordUser;
        return this;
    }

    public void setBannedUser(DiscordUser discordUser) {
        this.bannedUser = discordUser;
    }

    public GuildServer getTempBanGuildServer() {
        return tempBanGuildServer;
    }

    public TempBan tempBanGuildServer(GuildServer guildServer) {
        this.tempBanGuildServer = guildServer;
        return this;
    }

    public void setTempBanGuildServer(GuildServer guildServer) {
        this.tempBanGuildServer = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TempBan)) {
            return false;
        }
        return id != null && id.equals(((TempBan) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TempBan{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", guildId=" + getGuildId() +
            ", userId=" + getUserId() +
            "}";
    }
}
