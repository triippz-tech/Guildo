package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents the automoderation configuration\nfor a specific guild\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the automoderation configuration\nfor a specific guild\n@author Mark Tripoli")
@Entity
@Table(name = "auto_moderation")
public class AutoModeration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "invite_strikes", nullable = false)
    private Integer inviteStrikes;

    @NotNull
    @Column(name = "copy_pasta_strikes", nullable = false)
    private Integer copyPastaStrikes;

    @NotNull
    @Column(name = "everyone_mention_strikes", nullable = false)
    private Integer everyoneMentionStrikes;

    @NotNull
    @Column(name = "referral_strikes", nullable = false)
    private Integer referralStrikes;

    @NotNull
    @Column(name = "duplicate_strikes", nullable = false)
    private Integer duplicateStrikes;

    @NotNull
    @Column(name = "resolve_urls", nullable = false)
    private Boolean resolveUrls;

    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    @OneToOne
    @JoinColumn(unique = true)
    private AutoModIgnore ignoreConfig;

    @OneToOne
    @JoinColumn(unique = true)
    private AutoModMentions mentionConfig;

    @OneToOne
    @JoinColumn(unique = true)
    private AutoModAntiDup antiDupConfig;

    @OneToOne
    @JoinColumn(unique = true)
    private AutoModAutoRaid autoRaidConfig;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getInviteStrikes() {
        return inviteStrikes;
    }

    public AutoModeration inviteStrikes(Integer inviteStrikes) {
        this.inviteStrikes = inviteStrikes;
        return this;
    }

    public void setInviteStrikes(Integer inviteStrikes) {
        this.inviteStrikes = inviteStrikes;
    }

    public Integer getCopyPastaStrikes() {
        return copyPastaStrikes;
    }

    public AutoModeration copyPastaStrikes(Integer copyPastaStrikes) {
        this.copyPastaStrikes = copyPastaStrikes;
        return this;
    }

    public void setCopyPastaStrikes(Integer copyPastaStrikes) {
        this.copyPastaStrikes = copyPastaStrikes;
    }

    public Integer getEveryoneMentionStrikes() {
        return everyoneMentionStrikes;
    }

    public AutoModeration everyoneMentionStrikes(Integer everyoneMentionStrikes) {
        this.everyoneMentionStrikes = everyoneMentionStrikes;
        return this;
    }

    public void setEveryoneMentionStrikes(Integer everyoneMentionStrikes) {
        this.everyoneMentionStrikes = everyoneMentionStrikes;
    }

    public Integer getReferralStrikes() {
        return referralStrikes;
    }

    public AutoModeration referralStrikes(Integer referralStrikes) {
        this.referralStrikes = referralStrikes;
        return this;
    }

    public void setReferralStrikes(Integer referralStrikes) {
        this.referralStrikes = referralStrikes;
    }

    public Integer getDuplicateStrikes() {
        return duplicateStrikes;
    }

    public AutoModeration duplicateStrikes(Integer duplicateStrikes) {
        this.duplicateStrikes = duplicateStrikes;
        return this;
    }

    public void setDuplicateStrikes(Integer duplicateStrikes) {
        this.duplicateStrikes = duplicateStrikes;
    }

    public Boolean isResolveUrls() {
        return resolveUrls;
    }

    public AutoModeration resolveUrls(Boolean resolveUrls) {
        this.resolveUrls = resolveUrls;
        return this;
    }

    public void setResolveUrls(Boolean resolveUrls) {
        this.resolveUrls = resolveUrls;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public AutoModeration enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public AutoModIgnore getIgnoreConfig() {
        return ignoreConfig;
    }

    public AutoModeration ignoreConfig(AutoModIgnore autoModIgnore) {
        this.ignoreConfig = autoModIgnore;
        return this;
    }

    public void setIgnoreConfig(AutoModIgnore autoModIgnore) {
        this.ignoreConfig = autoModIgnore;
    }

    public AutoModMentions getMentionConfig() {
        return mentionConfig;
    }

    public AutoModeration mentionConfig(AutoModMentions autoModMentions) {
        this.mentionConfig = autoModMentions;
        return this;
    }

    public void setMentionConfig(AutoModMentions autoModMentions) {
        this.mentionConfig = autoModMentions;
    }

    public AutoModAntiDup getAntiDupConfig() {
        return antiDupConfig;
    }

    public AutoModeration antiDupConfig(AutoModAntiDup autoModAntiDup) {
        this.antiDupConfig = autoModAntiDup;
        return this;
    }

    public void setAntiDupConfig(AutoModAntiDup autoModAntiDup) {
        this.antiDupConfig = autoModAntiDup;
    }

    public AutoModAutoRaid getAutoRaidConfig() {
        return autoRaidConfig;
    }

    public AutoModeration autoRaidConfig(AutoModAutoRaid autoModAutoRaid) {
        this.autoRaidConfig = autoModAutoRaid;
        return this;
    }

    public void setAutoRaidConfig(AutoModAutoRaid autoModAutoRaid) {
        this.autoRaidConfig = autoModAutoRaid;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoModeration)) {
            return false;
        }
        return id != null && id.equals(((AutoModeration) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AutoModeration{" +
            "id=" + getId() +
            ", inviteStrikes=" + getInviteStrikes() +
            ", copyPastaStrikes=" + getCopyPastaStrikes() +
            ", everyoneMentionStrikes=" + getEveryoneMentionStrikes() +
            ", referralStrikes=" + getReferralStrikes() +
            ", duplicateStrikes=" + getDuplicateStrikes() +
            ", resolveUrls='" + isResolveUrls() + "'" +
            ", enabled='" + isEnabled() + "'" +
            "}";
    }
}
