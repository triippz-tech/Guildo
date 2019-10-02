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
 * Criteria class for the {@link com.triippztech.guildo.domain.AutoModeration} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.AutoModerationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auto-moderations?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AutoModerationCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter inviteStrikes;

    private IntegerFilter copyPastaStrikes;

    private IntegerFilter everyoneMentionStrikes;

    private IntegerFilter referralStrikes;

    private IntegerFilter duplicateStrikes;

    private BooleanFilter resolveUrls;

    private BooleanFilter enabled;

    private LongFilter ignoreConfigId;

    private LongFilter mentionConfigId;

    private LongFilter antiDupConfigId;

    private LongFilter autoRaidConfigId;

    public AutoModerationCriteria(){
    }

    public AutoModerationCriteria(AutoModerationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.inviteStrikes = other.inviteStrikes == null ? null : other.inviteStrikes.copy();
        this.copyPastaStrikes = other.copyPastaStrikes == null ? null : other.copyPastaStrikes.copy();
        this.everyoneMentionStrikes = other.everyoneMentionStrikes == null ? null : other.everyoneMentionStrikes.copy();
        this.referralStrikes = other.referralStrikes == null ? null : other.referralStrikes.copy();
        this.duplicateStrikes = other.duplicateStrikes == null ? null : other.duplicateStrikes.copy();
        this.resolveUrls = other.resolveUrls == null ? null : other.resolveUrls.copy();
        this.enabled = other.enabled == null ? null : other.enabled.copy();
        this.ignoreConfigId = other.ignoreConfigId == null ? null : other.ignoreConfigId.copy();
        this.mentionConfigId = other.mentionConfigId == null ? null : other.mentionConfigId.copy();
        this.antiDupConfigId = other.antiDupConfigId == null ? null : other.antiDupConfigId.copy();
        this.autoRaidConfigId = other.autoRaidConfigId == null ? null : other.autoRaidConfigId.copy();
    }

    @Override
    public AutoModerationCriteria copy() {
        return new AutoModerationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getInviteStrikes() {
        return inviteStrikes;
    }

    public void setInviteStrikes(IntegerFilter inviteStrikes) {
        this.inviteStrikes = inviteStrikes;
    }

    public IntegerFilter getCopyPastaStrikes() {
        return copyPastaStrikes;
    }

    public void setCopyPastaStrikes(IntegerFilter copyPastaStrikes) {
        this.copyPastaStrikes = copyPastaStrikes;
    }

    public IntegerFilter getEveryoneMentionStrikes() {
        return everyoneMentionStrikes;
    }

    public void setEveryoneMentionStrikes(IntegerFilter everyoneMentionStrikes) {
        this.everyoneMentionStrikes = everyoneMentionStrikes;
    }

    public IntegerFilter getReferralStrikes() {
        return referralStrikes;
    }

    public void setReferralStrikes(IntegerFilter referralStrikes) {
        this.referralStrikes = referralStrikes;
    }

    public IntegerFilter getDuplicateStrikes() {
        return duplicateStrikes;
    }

    public void setDuplicateStrikes(IntegerFilter duplicateStrikes) {
        this.duplicateStrikes = duplicateStrikes;
    }

    public BooleanFilter getResolveUrls() {
        return resolveUrls;
    }

    public void setResolveUrls(BooleanFilter resolveUrls) {
        this.resolveUrls = resolveUrls;
    }

    public BooleanFilter getEnabled() {
        return enabled;
    }

    public void setEnabled(BooleanFilter enabled) {
        this.enabled = enabled;
    }

    public LongFilter getIgnoreConfigId() {
        return ignoreConfigId;
    }

    public void setIgnoreConfigId(LongFilter ignoreConfigId) {
        this.ignoreConfigId = ignoreConfigId;
    }

    public LongFilter getMentionConfigId() {
        return mentionConfigId;
    }

    public void setMentionConfigId(LongFilter mentionConfigId) {
        this.mentionConfigId = mentionConfigId;
    }

    public LongFilter getAntiDupConfigId() {
        return antiDupConfigId;
    }

    public void setAntiDupConfigId(LongFilter antiDupConfigId) {
        this.antiDupConfigId = antiDupConfigId;
    }

    public LongFilter getAutoRaidConfigId() {
        return autoRaidConfigId;
    }

    public void setAutoRaidConfigId(LongFilter autoRaidConfigId) {
        this.autoRaidConfigId = autoRaidConfigId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AutoModerationCriteria that = (AutoModerationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(inviteStrikes, that.inviteStrikes) &&
            Objects.equals(copyPastaStrikes, that.copyPastaStrikes) &&
            Objects.equals(everyoneMentionStrikes, that.everyoneMentionStrikes) &&
            Objects.equals(referralStrikes, that.referralStrikes) &&
            Objects.equals(duplicateStrikes, that.duplicateStrikes) &&
            Objects.equals(resolveUrls, that.resolveUrls) &&
            Objects.equals(enabled, that.enabled) &&
            Objects.equals(ignoreConfigId, that.ignoreConfigId) &&
            Objects.equals(mentionConfigId, that.mentionConfigId) &&
            Objects.equals(antiDupConfigId, that.antiDupConfigId) &&
            Objects.equals(autoRaidConfigId, that.autoRaidConfigId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        inviteStrikes,
        copyPastaStrikes,
        everyoneMentionStrikes,
        referralStrikes,
        duplicateStrikes,
        resolveUrls,
        enabled,
        ignoreConfigId,
        mentionConfigId,
        antiDupConfigId,
        autoRaidConfigId
        );
    }

    @Override
    public String toString() {
        return "AutoModerationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (inviteStrikes != null ? "inviteStrikes=" + inviteStrikes + ", " : "") +
                (copyPastaStrikes != null ? "copyPastaStrikes=" + copyPastaStrikes + ", " : "") +
                (everyoneMentionStrikes != null ? "everyoneMentionStrikes=" + everyoneMentionStrikes + ", " : "") +
                (referralStrikes != null ? "referralStrikes=" + referralStrikes + ", " : "") +
                (duplicateStrikes != null ? "duplicateStrikes=" + duplicateStrikes + ", " : "") +
                (resolveUrls != null ? "resolveUrls=" + resolveUrls + ", " : "") +
                (enabled != null ? "enabled=" + enabled + ", " : "") +
                (ignoreConfigId != null ? "ignoreConfigId=" + ignoreConfigId + ", " : "") +
                (mentionConfigId != null ? "mentionConfigId=" + mentionConfigId + ", " : "") +
                (antiDupConfigId != null ? "antiDupConfigId=" + antiDupConfigId + ", " : "") +
                (autoRaidConfigId != null ? "autoRaidConfigId=" + autoRaidConfigId + ", " : "") +
            "}";
    }

}
