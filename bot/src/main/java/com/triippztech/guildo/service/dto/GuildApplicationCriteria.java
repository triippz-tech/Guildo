package com.triippztech.guildo.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.triippztech.guildo.domain.enumeration.ApplicationStatus;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildApplication} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildApplicationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-applications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildApplicationCriteria implements Serializable, Criteria {
    /**
     * Class for filtering ApplicationStatus
     */
    public static class ApplicationStatusFilter extends Filter<ApplicationStatus> {

        public ApplicationStatusFilter() {
        }

        public ApplicationStatusFilter(ApplicationStatusFilter filter) {
            super(filter);
        }

        @Override
        public ApplicationStatusFilter copy() {
            return new ApplicationStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter characterName;

    private StringFilter characterType;

    private ApplicationStatusFilter status;

    private LongFilter acceptedById;

    private LongFilter appliedUserId;

    private LongFilter guildServerId;

    public GuildApplicationCriteria(){
    }

    public GuildApplicationCriteria(GuildApplicationCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.characterName = other.characterName == null ? null : other.characterName.copy();
        this.characterType = other.characterType == null ? null : other.characterType.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.acceptedById = other.acceptedById == null ? null : other.acceptedById.copy();
        this.appliedUserId = other.appliedUserId == null ? null : other.appliedUserId.copy();
        this.guildServerId = other.guildServerId == null ? null : other.guildServerId.copy();
    }

    @Override
    public GuildApplicationCriteria copy() {
        return new GuildApplicationCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCharacterName() {
        return characterName;
    }

    public void setCharacterName(StringFilter characterName) {
        this.characterName = characterName;
    }

    public StringFilter getCharacterType() {
        return characterType;
    }

    public void setCharacterType(StringFilter characterType) {
        this.characterType = characterType;
    }

    public ApplicationStatusFilter getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatusFilter status) {
        this.status = status;
    }

    public LongFilter getAcceptedById() {
        return acceptedById;
    }

    public void setAcceptedById(LongFilter acceptedById) {
        this.acceptedById = acceptedById;
    }

    public LongFilter getAppliedUserId() {
        return appliedUserId;
    }

    public void setAppliedUserId(LongFilter appliedUserId) {
        this.appliedUserId = appliedUserId;
    }

    public LongFilter getGuildServerId() {
        return guildServerId;
    }

    public void setGuildServerId(LongFilter guildServerId) {
        this.guildServerId = guildServerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildApplicationCriteria that = (GuildApplicationCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(characterName, that.characterName) &&
            Objects.equals(characterType, that.characterType) &&
            Objects.equals(status, that.status) &&
            Objects.equals(acceptedById, that.acceptedById) &&
            Objects.equals(appliedUserId, that.appliedUserId) &&
            Objects.equals(guildServerId, that.guildServerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        characterName,
        characterType,
        status,
        acceptedById,
        appliedUserId,
        guildServerId
        );
    }

    @Override
    public String toString() {
        return "GuildApplicationCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (characterName != null ? "characterName=" + characterName + ", " : "") +
                (characterType != null ? "characterType=" + characterType + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (acceptedById != null ? "acceptedById=" + acceptedById + ", " : "") +
                (appliedUserId != null ? "appliedUserId=" + appliedUserId + ", " : "") +
                (guildServerId != null ? "guildServerId=" + guildServerId + ", " : "") +
            "}";
    }

}
