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
 * Criteria class for the {@link com.triippztech.guildo.domain.ScheduledAnnouncement} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.ScheduledAnnouncementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /scheduled-announcements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ScheduledAnnouncementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter annoucementTitle;

    private StringFilter annoucementImgUrl;

    private InstantFilter annoucementFire;

    private LongFilter guildId;

    private LongFilter annouceGuildId;

    public ScheduledAnnouncementCriteria(){
    }

    public ScheduledAnnouncementCriteria(ScheduledAnnouncementCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.annoucementTitle = other.annoucementTitle == null ? null : other.annoucementTitle.copy();
        this.annoucementImgUrl = other.annoucementImgUrl == null ? null : other.annoucementImgUrl.copy();
        this.annoucementFire = other.annoucementFire == null ? null : other.annoucementFire.copy();
        this.guildId = other.guildId == null ? null : other.guildId.copy();
        this.annouceGuildId = other.annouceGuildId == null ? null : other.annouceGuildId.copy();
    }

    @Override
    public ScheduledAnnouncementCriteria copy() {
        return new ScheduledAnnouncementCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getAnnoucementTitle() {
        return annoucementTitle;
    }

    public void setAnnoucementTitle(StringFilter annoucementTitle) {
        this.annoucementTitle = annoucementTitle;
    }

    public StringFilter getAnnoucementImgUrl() {
        return annoucementImgUrl;
    }

    public void setAnnoucementImgUrl(StringFilter annoucementImgUrl) {
        this.annoucementImgUrl = annoucementImgUrl;
    }

    public InstantFilter getAnnoucementFire() {
        return annoucementFire;
    }

    public void setAnnoucementFire(InstantFilter annoucementFire) {
        this.annoucementFire = annoucementFire;
    }

    public LongFilter getGuildId() {
        return guildId;
    }

    public void setGuildId(LongFilter guildId) {
        this.guildId = guildId;
    }

    public LongFilter getAnnouceGuildId() {
        return annouceGuildId;
    }

    public void setAnnouceGuildId(LongFilter annouceGuildId) {
        this.annouceGuildId = annouceGuildId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ScheduledAnnouncementCriteria that = (ScheduledAnnouncementCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(annoucementTitle, that.annoucementTitle) &&
            Objects.equals(annoucementImgUrl, that.annoucementImgUrl) &&
            Objects.equals(annoucementFire, that.annoucementFire) &&
            Objects.equals(guildId, that.guildId) &&
            Objects.equals(annouceGuildId, that.annouceGuildId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        annoucementTitle,
        annoucementImgUrl,
        annoucementFire,
        guildId,
        annouceGuildId
        );
    }

    @Override
    public String toString() {
        return "ScheduledAnnouncementCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (annoucementTitle != null ? "annoucementTitle=" + annoucementTitle + ", " : "") +
                (annoucementImgUrl != null ? "annoucementImgUrl=" + annoucementImgUrl + ", " : "") +
                (annoucementFire != null ? "annoucementFire=" + annoucementFire + ", " : "") +
                (guildId != null ? "guildId=" + guildId + ", " : "") +
                (annouceGuildId != null ? "annouceGuildId=" + annouceGuildId + ", " : "") +
            "}";
    }

}
