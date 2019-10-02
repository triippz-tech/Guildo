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
 * Criteria class for the {@link com.triippztech.guildo.domain.AutoModIgnore} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.AutoModIgnoreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /auto-mod-ignores?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AutoModIgnoreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter roleId;

    private LongFilter channelId;

    public AutoModIgnoreCriteria(){
    }

    public AutoModIgnoreCriteria(AutoModIgnoreCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.roleId = other.roleId == null ? null : other.roleId.copy();
        this.channelId = other.channelId == null ? null : other.channelId.copy();
    }

    @Override
    public AutoModIgnoreCriteria copy() {
        return new AutoModIgnoreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getRoleId() {
        return roleId;
    }

    public void setRoleId(LongFilter roleId) {
        this.roleId = roleId;
    }

    public LongFilter getChannelId() {
        return channelId;
    }

    public void setChannelId(LongFilter channelId) {
        this.channelId = channelId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AutoModIgnoreCriteria that = (AutoModIgnoreCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(roleId, that.roleId) &&
            Objects.equals(channelId, that.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        roleId,
        channelId
        );
    }

    @Override
    public String toString() {
        return "AutoModIgnoreCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (roleId != null ? "roleId=" + roleId + ", " : "") +
                (channelId != null ? "channelId=" + channelId + ", " : "") +
            "}";
    }

}
