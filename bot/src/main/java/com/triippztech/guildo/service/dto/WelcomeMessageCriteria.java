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
 * Criteria class for the {@link com.triippztech.guildo.domain.WelcomeMessage} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.WelcomeMessageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /welcome-messages?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class WelcomeMessageCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter messageTitle;

    private StringFilter body;

    private StringFilter footer;

    private StringFilter logoUrl;

    private LongFilter guildId;

    public WelcomeMessageCriteria(){
    }

    public WelcomeMessageCriteria(WelcomeMessageCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.messageTitle = other.messageTitle == null ? null : other.messageTitle.copy();
        this.body = other.body == null ? null : other.body.copy();
        this.footer = other.footer == null ? null : other.footer.copy();
        this.logoUrl = other.logoUrl == null ? null : other.logoUrl.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
    }

    @Override
    public WelcomeMessageCriteria copy() {
        return new WelcomeMessageCriteria(this);
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

    public StringFilter getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(StringFilter messageTitle) {
        this.messageTitle = messageTitle;
    }

    public StringFilter getBody() {
        return body;
    }

    public void setBody(StringFilter body) {
        this.body = body;
    }

    public StringFilter getFooter() {
        return footer;
    }

    public void setFooter(StringFilter footer) {
        this.footer = footer;
    }

    public StringFilter getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(StringFilter logoUrl) {
        this.logoUrl = logoUrl;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final WelcomeMessageCriteria that = (WelcomeMessageCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(messageTitle, that.messageTitle) &&
            Objects.equals(body, that.body) &&
            Objects.equals(footer, that.footer) &&
            Objects.equals(logoUrl, that.logoUrl) &&
            Objects.equals(guildId, that.guildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        messageTitle,
        body,
        footer,
        logoUrl,
        guildId
        );
    }

    @Override
    public String toString() {
        return "WelcomeMessageCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (messageTitle != null ? "messageTitle=" + messageTitle + ", " : "") +
                (body != null ? "body=" + body + ", " : "") +
                (footer != null ? "footer=" + footer + ", " : "") +
                (logoUrl != null ? "logoUrl=" + logoUrl + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
            "}";
    }

}
