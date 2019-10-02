package com.triippztech.guildo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.triippztech.guildo.domain.enumeration.GuildType;
import com.triippztech.guildo.domain.enumeration.GuildPlayStyle;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildServerProfile} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildServerProfileResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-server-profiles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildServerProfileCriteria implements Serializable, Criteria {
    /**
     * Class for filtering GuildType
     */
    public static class GuildTypeFilter extends Filter<GuildType> {

        public GuildTypeFilter() {
        }

        public GuildTypeFilter(GuildTypeFilter filter) {
            super(filter);
        }

        @Override
        public GuildTypeFilter copy() {
            return new GuildTypeFilter(this);
        }

    }
    /**
     * Class for filtering GuildPlayStyle
     */
    public static class GuildPlayStyleFilter extends Filter<GuildPlayStyle> {

        public GuildPlayStyleFilter() {
        }

        public GuildPlayStyleFilter(GuildPlayStyleFilter filter) {
            super(filter);
        }

        @Override
        public GuildPlayStyleFilter copy() {
            return new GuildPlayStyleFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private GuildTypeFilter guildType;

    private GuildPlayStyleFilter playStyle;

    private StringFilter description;

    private StringFilter website;

    private StringFilter discordUrl;

    public GuildServerProfileCriteria(){
    }

    public GuildServerProfileCriteria(GuildServerProfileCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.guildType = other.guildType == null ? null : other.guildType.copy();
        this.playStyle = other.playStyle == null ? null : other.playStyle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.website = other.website == null ? null : other.website.copy();
        this.discordUrl = other.discordUrl == null ? null : other.discordUrl.copy();
    }

    @Override
    public GuildServerProfileCriteria copy() {
        return new GuildServerProfileCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public GuildTypeFilter getGuildType() {
        return guildType;
    }

    public void setGuildType(GuildTypeFilter guildType) {
        this.guildType = guildType;
    }

    public GuildPlayStyleFilter getPlayStyle() {
        return playStyle;
    }

    public void setPlayStyle(GuildPlayStyleFilter playStyle) {
        this.playStyle = playStyle;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StringFilter getWebsite() {
        return website;
    }

    public void setWebsite(StringFilter website) {
        this.website = website;
    }

    public StringFilter getDiscordUrl() {
        return discordUrl;
    }

    public void setDiscordUrl(StringFilter discordUrl) {
        this.discordUrl = discordUrl;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildServerProfileCriteria that = (GuildServerProfileCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(guildType, that.guildType) &&
            Objects.equals(playStyle, that.playStyle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(website, that.website) &&
            Objects.equals(discordUrl, that.discordUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        guildType,
        playStyle,
        description,
        website,
        discordUrl
        );
    }

    @Override
    public String toString() {
        return "GuildServerProfileCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (guildType != null ? "guildType=" + guildType + ", " : "") +
                (playStyle != null ? "playStyle=" + playStyle + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (website != null ? "website=" + website + ", " : "") +
                (discordUrl != null ? "discordUrl=" + discordUrl + ", " : "") +
            "}";
    }

}
