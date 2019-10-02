package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

import java.io.Serializable;

import com.triippztech.guildo.domain.enumeration.GuildType;

import com.triippztech.guildo.domain.enumeration.GuildPlayStyle;

/**
 * Represents the specific details of a Guild\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the specific details of a Guild\n@author Mark Tripoli")
@Entity
@Table(name = "guild_server_profile")
public class GuildServerProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "guild_type")
    private GuildType guildType;

    @Enumerated(EnumType.STRING)
    @Column(name = "play_style")
    private GuildPlayStyle playStyle;

    @Column(name = "description")
    private String description;

    @Column(name = "website")
    private String website;

    @Column(name = "discord_url")
    private String discordUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GuildType getGuildType() {
        return guildType;
    }

    public GuildServerProfile guildType(GuildType guildType) {
        this.guildType = guildType;
        return this;
    }

    public void setGuildType(GuildType guildType) {
        this.guildType = guildType;
    }

    public GuildPlayStyle getPlayStyle() {
        return playStyle;
    }

    public GuildServerProfile playStyle(GuildPlayStyle playStyle) {
        this.playStyle = playStyle;
        return this;
    }

    public void setPlayStyle(GuildPlayStyle playStyle) {
        this.playStyle = playStyle;
    }

    public String getDescription() {
        return description;
    }

    public GuildServerProfile description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public GuildServerProfile website(String website) {
        this.website = website;
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDiscordUrl() {
        return discordUrl;
    }

    public GuildServerProfile discordUrl(String discordUrl) {
        this.discordUrl = discordUrl;
        return this;
    }

    public void setDiscordUrl(String discordUrl) {
        this.discordUrl = discordUrl;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildServerProfile)) {
            return false;
        }
        return id != null && id.equals(((GuildServerProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildServerProfile{" +
            "id=" + getId() +
            ", guildType='" + getGuildType() + "'" +
            ", playStyle='" + getPlayStyle() + "'" +
            ", description='" + getDescription() + "'" +
            ", website='" + getWebsite() + "'" +
            ", discordUrl='" + getDiscordUrl() + "'" +
            "}";
    }
}
