package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents the max number of role mentions(@everyone),\nmax number of lines allowed in a text message, and the max number\nof mentions a discord user may write.\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the max number of role mentions(@everyone),\nmax number of lines allowed in a text message, and the max number\nof mentions a discord user may write.\n@author Mark Tripoli")
@Entity
@Table(name = "auto_mod_mentions")
public class AutoModMentions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "max_mentions", nullable = false)
    private Integer maxMentions;

    @NotNull
    @Column(name = "max_msg_lines", nullable = false)
    private Integer maxMsgLines;

    @NotNull
    @Column(name = "max_role_mentions", nullable = false)
    private Integer maxRoleMentions;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxMentions() {
        return maxMentions;
    }

    public AutoModMentions maxMentions(Integer maxMentions) {
        this.maxMentions = maxMentions;
        return this;
    }

    public void setMaxMentions(Integer maxMentions) {
        this.maxMentions = maxMentions;
    }

    public Integer getMaxMsgLines() {
        return maxMsgLines;
    }

    public AutoModMentions maxMsgLines(Integer maxMsgLines) {
        this.maxMsgLines = maxMsgLines;
        return this;
    }

    public void setMaxMsgLines(Integer maxMsgLines) {
        this.maxMsgLines = maxMsgLines;
    }

    public Integer getMaxRoleMentions() {
        return maxRoleMentions;
    }

    public AutoModMentions maxRoleMentions(Integer maxRoleMentions) {
        this.maxRoleMentions = maxRoleMentions;
        return this;
    }

    public void setMaxRoleMentions(Integer maxRoleMentions) {
        this.maxRoleMentions = maxRoleMentions;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoModMentions)) {
            return false;
        }
        return id != null && id.equals(((AutoModMentions) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AutoModMentions{" +
            "id=" + getId() +
            ", maxMentions=" + getMaxMentions() +
            ", maxMsgLines=" + getMaxMsgLines() +
            ", maxRoleMentions=" + getMaxRoleMentions() +
            "}";
    }
}
