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
 * Criteria class for the {@link com.triippztech.guildo.domain.GuildPollItem} entity. This class is used
 * in {@link com.triippztech.guildo.web.rest.GuildPollItemResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /guild-poll-items?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class GuildPollItemCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter itemName;

    private IntegerFilter votes;

    public GuildPollItemCriteria(){
    }

    public GuildPollItemCriteria(GuildPollItemCriteria other){
        this.id = other.id == null ? null : other.id.copy();
        this.itemName = other.itemName == null ? null : other.itemName.copy();
        this.votes = other.votes == null ? null : other.votes.copy();
    }

    @Override
    public GuildPollItemCriteria copy() {
        return new GuildPollItemCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getItemName() {
        return itemName;
    }

    public void setItemName(StringFilter itemName) {
        this.itemName = itemName;
    }

    public IntegerFilter getVotes() {
        return votes;
    }

    public void setVotes(IntegerFilter votes) {
        this.votes = votes;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GuildPollItemCriteria that = (GuildPollItemCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(itemName, that.itemName) &&
            Objects.equals(votes, that.votes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        itemName,
        votes
        );
    }

    @Override
    public String toString() {
        return "GuildPollItemCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (itemName != null ? "itemName=" + itemName + ", " : "") +
                (votes != null ? "votes=" + votes + ", " : "") +
            "}";
    }

}
