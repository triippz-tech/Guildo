package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents an Item which will be placed\ninto the Poll. Used to create a list of items.\n@author Mark Tripoli
 */
@ApiModel(description = "Represents an Item which will be placed\ninto the Poll. Used to create a list of items.\n@author Mark Tripoli")
@Entity
@Table(name = "guild_poll_item")
public class GuildPollItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "item_name", nullable = false)
    private String itemName;

    @NotNull
    @Column(name = "votes", nullable = false)
    private Integer votes;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public GuildPollItem itemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getVotes() {
        return votes;
    }

    public GuildPollItem votes(Integer votes) {
        this.votes = votes;
        return this;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildPollItem)) {
            return false;
        }
        return id != null && id.equals(((GuildPollItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildPollItem{" +
            "id=" + getId() +
            ", itemName='" + getItemName() + "'" +
            ", votes=" + getVotes() +
            "}";
    }
}
