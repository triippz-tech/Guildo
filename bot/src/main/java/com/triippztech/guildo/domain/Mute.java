package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents a temprary muting of a Discord user within a guild\n@author Mark Tripoli
 */
@ApiModel(description = "Represents a temprary muting of a Discord user within a guild\n@author Mark Tripoli")
@Entity
@Table(name = "mute")
public class Mute implements Serializable {

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
    private String endTime;

    @Column(name = "guild_id")
    private Long guildId;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JsonIgnoreProperties("userMutes")
    private DiscordUser mutedUser;

    @ManyToOne
    @JsonIgnoreProperties("mutedUsers")
    private GuildServer mutedGuildServer;

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

    public Mute reason(String reason) {
        this.reason = reason;
        return this;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getEndTime() {
        return endTime;
    }

    public Mute endTime(String endTime) {
        this.endTime = endTime;
        return this;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getGuildId() {
        return guildId;
    }

    public Mute guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public Long getUserId() {
        return userId;
    }

    public Mute userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public DiscordUser getMutedUser() {
        return mutedUser;
    }

    public Mute mutedUser(DiscordUser discordUser) {
        this.mutedUser = discordUser;
        return this;
    }

    public void setMutedUser(DiscordUser discordUser) {
        this.mutedUser = discordUser;
    }

    public GuildServer getMutedGuildServer() {
        return mutedGuildServer;
    }

    public Mute mutedGuildServer(GuildServer guildServer) {
        this.mutedGuildServer = guildServer;
        return this;
    }

    public void setMutedGuildServer(GuildServer guildServer) {
        this.mutedGuildServer = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Mute)) {
            return false;
        }
        return id != null && id.equals(((Mute) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Mute{" +
            "id=" + getId() +
            ", reason='" + getReason() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", guildId=" + getGuildId() +
            ", userId=" + getUserId() +
            "}";
    }
}
