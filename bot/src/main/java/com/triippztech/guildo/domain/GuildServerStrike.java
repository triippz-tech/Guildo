package com.triippztech.guildo.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A GuildServerStrike.
 */
@Entity
@Table(name = "guild_server_strike")
public class GuildServerStrike implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @NotNull
    @Min(value = 0)
    @Column(name = "count", nullable = false)
    private Integer count;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private DiscordUser discordUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCount() {
        return count;
    }

    public GuildServerStrike count(Integer count) {
        this.count = count;
        return this;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Long getUserId() {
        return userId;
    }

    public GuildServerStrike userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGuildId() {
        return guildId;
    }

    public GuildServerStrike guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public DiscordUser getDiscordUser() {
        return discordUser;
    }

    public GuildServerStrike discordUser(DiscordUser discordUser) {
        this.discordUser = discordUser;
        return this;
    }

    public void setDiscordUser(DiscordUser discordUser) {
        this.discordUser = discordUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildServerStrike)) {
            return false;
        }
        return id != null && id.equals(((GuildServerStrike) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildServerStrike{" +
            "id=" + getId() +
            ", count=" + getCount() +
            ", userId=" + getUserId() +
            ", guildId=" + getGuildId() +
            "}";
    }
}
