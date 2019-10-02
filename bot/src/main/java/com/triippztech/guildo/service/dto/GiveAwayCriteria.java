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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.GiveAway} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GiveAwayResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /give-aways?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GiveAwayCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter image;

    private LongFilter messageId;

    private LongFilter textChannelId;

    private InstantFilter finish;

    private BooleanFilter expired;

    private LongFilter guildId;

    private LongFilter winnerId;

    private LongFilter guildGiveAwayId;

    public GiveAwayCriteria(){
    }

    public GiveAwayCriteria(GiveAwayCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.image = other.image == null ? null : other.image.copy();
        this.messageId = other.messageId == null ? null : other.messageId.copy();
        this.textChannelId = other.textChannelId == null ? null : other.textChannelId.copy();
        this.finish = other.finish == null ? null : other.finish.copy();
        this.expired = other.expired == null ? null : other.expired.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.winnerId = other.winnerId == null ? null : other.winnerId.copy();
        this.guildGiveAwayId = other.guildGiveAwayId == null ? null : other.guildGiveAwayId.copy();
    }

    @Override
    public GiveAwayCriteria copy() {
        return new GiveAwayCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getImage() {
        return image;
    }

    public void setImage(StringFilter image) {
        this.image = image;
    }

    public LongFilter getMessageId() {
        return messageId;
    }

    public void setMessageId(LongFilter messageId) {
        this.messageId = messageId;
    }

    public LongFilter getTextChannelId() {
        return textChannelId;
    }

    public void setTextChannelId(LongFilter textChannelId) {
        this.textChannelId = textChannelId;
    }

    public InstantFilter getFinish() {
        return finish;
    }

    public void setFinish(InstantFilter finish) {
        this.finish = finish;
    }

    public BooleanFilter getExpired() {
        return expired;
    }

    public void setExpired(BooleanFilter expired) {
        this.expired = expired;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(LongFilter winnerId) {
        this.winnerId = winnerId;
    }

    public LongFilter getGuildGiveAwayId() {
        return guildGiveAwayId;
    }

    public void setGuildGiveAwayId(LongFilter guildGiveAwayId) {
        this.guildGiveAwayId = guildGiveAwayId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GiveAwayCriteria that = (GiveAwayCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(image, that.image) &&
            Objects.equals(messageId, that.messageId) &&
            Objects.equals(textChannelId, that.textChannelId) &&
            Objects.equals(finish, that.finish) &&
            Objects.equals(expired, that.expired) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(winnerId, that.winnerId) &&
            Objects.equals(guildGiveAwayId, that.guildGiveAwayId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        image,
        messageId,
        textChannelId,
        finish,
        expired,
        guildId,
        winnerId,
        guildGiveAwayId
        );
    }

    @Override
    public String toString() {
        return "GiveAwayCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (image != null ? "image=" + image + ", " : "") +
                (messageId != null ? "messageId=" + messageId + ", " : "") +
                (textChannelId != null ? "textChannelId=" + textChannelId + ", " : "") +
                (finish != null ? "finish=" + finish + ", " : "") +
                (expired != null ? "expired=" + expired + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (winnerId != null ? "winnerId=" + winnerId + ", " : "") +
                (guildGiveAwayId != null ? "guildGiveAwayId=" + guildGiveAwayId + ", " : "") +
            "}";
    }

}
