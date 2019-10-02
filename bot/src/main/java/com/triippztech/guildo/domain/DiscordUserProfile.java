package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;

import java.io.Serializable;

/**
 * Represents the Guildo Bot profile for a specific\nDiscord User.\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the Guildo Bot profile for a specific\nDiscord User.\n@author Mark Tripoli")
@Entity
@Table(name = "discord_user_profile")
public class DiscordUserProfile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "favorite_game")
    private String favoriteGame;

    @Column(name = "profile_photo")
    private String profilePhoto;

    @Column(name = "twitter_url")
    private String twitterUrl;

    @Column(name = "twitch_url")
    private String twitchUrl;

    @Column(name = "youtube_url")
    private String youtubeUrl;

    @Column(name = "facebook_url")
    private String facebookUrl;

    @Column(name = "hitbox_url")
    private String hitboxUrl;

    @Column(name = "beam_url")
    private String beamUrl;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFavoriteGame() {
        return favoriteGame;
    }

    public DiscordUserProfile favoriteGame(String favoriteGame) {
        this.favoriteGame = favoriteGame;
        return this;
    }

    public void setFavoriteGame(String favoriteGame) {
        this.favoriteGame = favoriteGame;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public DiscordUserProfile profilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
        return this;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getTwitterUrl() {
        return twitterUrl;
    }

    public DiscordUserProfile twitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
        return this;
    }

    public void setTwitterUrl(String twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public String getTwitchUrl() {
        return twitchUrl;
    }

    public DiscordUserProfile twitchUrl(String twitchUrl) {
        this.twitchUrl = twitchUrl;
        return this;
    }

    public void setTwitchUrl(String twitchUrl) {
        this.twitchUrl = twitchUrl;
    }

    public String getYoutubeUrl() {
        return youtubeUrl;
    }

    public DiscordUserProfile youtubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
        return this;
    }

    public void setYoutubeUrl(String youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public DiscordUserProfile facebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
        return this;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getHitboxUrl() {
        return hitboxUrl;
    }

    public DiscordUserProfile hitboxUrl(String hitboxUrl) {
        this.hitboxUrl = hitboxUrl;
        return this;
    }

    public void setHitboxUrl(String hitboxUrl) {
        this.hitboxUrl = hitboxUrl;
    }

    public String getBeamUrl() {
        return beamUrl;
    }

    public DiscordUserProfile beamUrl(String beamUrl) {
        this.beamUrl = beamUrl;
        return this;
    }

    public void setBeamUrl(String beamUrl) {
        this.beamUrl = beamUrl;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscordUserProfile)) {
            return false;
        }
        return id != null && id.equals(((DiscordUserProfile) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DiscordUserProfile{" +
            "id=" + getId() +
            ", favoriteGame='" + getFavoriteGame() + "'" +
            ", profilePhoto='" + getProfilePhoto() + "'" +
            ", twitterUrl='" + getTwitterUrl() + "'" +
            ", twitchUrl='" + getTwitchUrl() + "'" +
            ", youtubeUrl='" + getYoutubeUrl() + "'" +
            ", facebookUrl='" + getFacebookUrl() + "'" +
            ", hitboxUrl='" + getHitboxUrl() + "'" +
            ", beamUrl='" + getBeamUrl() + "'" +
            "}";
    }
}
