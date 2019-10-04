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
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildServerStrike} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildServerStrikeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-server-strikes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildServerStrikeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter count;

    private LongFilter userId;

    private LongFilter guildId;

    private LongFilter discordUserId;

    public GuildServerStrikeCriteria(){
    }

    public GuildServerStrikeCriteria(GuildServerStrikeCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.count = other.count == null ? null : other.count.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.discordUserId = other.discordUserId == null ? null : other.discordUserId.copy();
    }

    @Override
    public GuildServerStrikeCriteria copy() {
        return new GuildServerStrikeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getCount() {
        return count;
    }

    public void setCount(IntegerFilter count) {
        this.count = count;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getDiscordUserId() {
        return discordUserId;
    }

    public void setDiscordUserId(LongFilter discordUserId) {
        this.discordUserId = discordUserId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildServerStrikeCriteria that = (GuildServerStrikeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(count, that.count) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(discordUserId, that.discordUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        count,
        userId,
        guildId,
        discordUserId
        );
    }

    @Override
    public String toString() {
        return "GuildServerStrikeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (count != null ? "count=" + count + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (discordUserId != null ? "discordUserId=" + discordUserId + ", " : "") +
            "}";
    }

}
