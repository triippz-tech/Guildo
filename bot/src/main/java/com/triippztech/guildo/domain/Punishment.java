package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.triippztech.guildo.domain.enumeration.PunishmentType;

/**
 * Represents the a punishment a moderator\nmay hand to a Discord User in their server\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the a punishment a moderator\nmay hand to a Discord User in their server\n@author Mark Tripoli")
@Entity
@Table(name = "punishment")
public class Punishment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "max_strikes", nullable = false)
    private Integer maxStrikes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private PunishmentType action;

    @Column(name = "punishment_duration")
    private Instant punishmentDuration;

    @Column(name = "guild_id")
    private Long guildId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxStrikes() {
        return maxStrikes;
    }

    public Punishment maxStrikes(Integer maxStrikes) {
        this.maxStrikes = maxStrikes;
        return this;
    }

    public void setMaxStrikes(Integer maxStrikes) {
        this.maxStrikes = maxStrikes;
    }

    public PunishmentType getAction() {
        return action;
    }

    public Punishment action(PunishmentType action) {
        this.action = action;
        return this;
    }

    public void setAction(PunishmentType action) {
        this.action = action;
    }

    public Instant getPunishmentDuration() {
        return punishmentDuration;
    }

    public Punishment punishmentDuration(Instant punishmentDuration) {
        this.punishmentDuration = punishmentDuration;
        return this;
    }

    public void setPunishmentDuration(Instant punishmentDuration) {
        this.punishmentDuration = punishmentDuration;
    }

    public Long getGuildId() {
        return guildId;
    }

    public Punishment guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Punishment)) {
            return false;
        }
        return id != null && id.equals(((Punishment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Punishment{" +
            "id=" + getId() +
            ", maxStrikes=" + getMaxStrikes() +
            ", action='" + getAction() + "'" +
            ", punishmentDuration='" + getPunishmentDuration() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
