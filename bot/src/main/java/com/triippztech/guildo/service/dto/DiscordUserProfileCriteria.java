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
 * Criteria class for the {@link com.triippztech.guildo.domain.DiscordUserProfile} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.DiscordUserProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /discord-user-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class DiscordUserProfileCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter favoriteGame;

    private StringFilter profilePhoto;

    private StringFilter twitterUrl;

    private StringFilter twitchUrl;

    private StringFilter youtubeUrl;

    private StringFilter facebookUrl;

    private StringFilter hitboxUrl;

    private StringFilter beamUrl;

    public DiscordUserProfileCriteria(){
    }

    public DiscordUserProfileCriteria(DiscordUserProfileCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.favoriteGame = other.favoriteGame == null ? null : other.favoriteGame.copy();
        this.profilePhoto = other.profilePhoto == null ? null : other.profilePhoto.copy();
        this.twitterUrl = other.twitterUrl == null ? null : other.twitterUrl.copy();
        this.twitchUrl = other.twitchUrl == null ? null : other.twitchUrl.copy();
        this.youtubeUrl = other.youtubeUrl == null ? null : other.youtubeUrl.copy();
        this.facebookUrl = other.facebookUrl == null ? null : other.facebookUrl.copy();
        this.hitboxUrl = other.hitboxUrl == null ? null : other.hitboxUrl.copy();
        this.beamUrl = other.beamUrl == null ? null : other.beamUrl.copy();
    }

    @Override
    public DiscordUserProfileCriteria copy() {
        return new DiscordUserProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFavoriteGame() {
        return favoriteGame;
    }

    public void setFavoriteGame(StringFilter favoriteGame) {
        this.favoriteGame = favoriteGame;
    }

    public StringFilter getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(StringFilter profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public StringFilter getTwitterUrl() {
        return twitterUrl;
    }

    public void setTwitterUrl(StringFilter twitterUrl) {
        this.twitterUrl = twitterUrl;
    }

    public StringFilter getTwitchUrl() {
        return twitchUrl;
    }

    public void setTwitchUrl(StringFilter twitchUrl) {
        this.twitchUrl = twitchUrl;
    }

    public StringFilter getYoutubeUrl() {
        return youtubeUrl;
    }

    public void setYoutubeUrl(StringFilter youtubeUrl) {
        this.youtubeUrl = youtubeUrl;
    }

    public StringFilter getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(StringFilter facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public StringFilter getHitboxUrl() {
        return hitboxUrl;
    }

    public void setHitboxUrl(StringFilter hitboxUrl) {
        this.hitboxUrl = hitboxUrl;
    }

    public StringFilter getBeamUrl() {
        return beamUrl;
    }

    public void setBeamUrl(StringFilter beamUrl) {
        this.beamUrl = beamUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DiscordUserProfileCriteria that = (DiscordUserProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(favoriteGame, that.favoriteGame) &&
            Objects.equals(profilePhoto, that.profilePhoto) &&
            Objects.equals(twitterUrl, that.twitterUrl) &&
            Objects.equals(twitchUrl, that.twitchUrl) &&
            Objects.equals(youtubeUrl, that.youtubeUrl) &&
            Objects.equals(facebookUrl, that.facebookUrl) &&
            Objects.equals(hitboxUrl, that.hitboxUrl) &&
            Objects.equals(beamUrl, that.beamUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        favoriteGame,
        profilePhoto,
        twitterUrl,
        twitchUrl,
        youtubeUrl,
        facebookUrl,
        hitboxUrl,
        beamUrl
        );
    }

    @Override
    public String toString() {
        return "DiscordUserProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (favoriteGame != null ? "favoriteGame=" + favoriteGame + ", " : "") +
                (profilePhoto != null ? "profilePhoto=" + profilePhoto + ", " : "") +
                (twitterUrl != null ? "twitterUrl=" + twitterUrl + ", " : "") +
                (twitchUrl != null ? "twitchUrl=" + twitchUrl + ", " : "") +
                (youtubeUrl != null ? "youtubeUrl=" + youtubeUrl + ", " : "") +
                (facebookUrl != null ? "facebookUrl=" + facebookUrl + ", " : "") +
                (hitboxUrl != null ? "hitboxUrl=" + hitboxUrl + ", " : "") +
                (beamUrl != null ? "beamUrl=" + beamUrl + ", " : "") +
            "}";
    }

}
