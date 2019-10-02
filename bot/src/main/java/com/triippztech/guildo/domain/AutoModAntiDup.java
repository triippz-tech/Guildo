package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents the Anti Duplicates configuration\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the Anti Duplicates configuration\n@author Mark Tripoli")
@Entity
@Table(name = "auto_mod_anti_dup")
public class AutoModAntiDup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "delete_threshold", nullable = false)
    private Integer deleteThreshold;

    @NotNull
    @Column(name = "dups_to_punish", nullable = false)
    private Integer dupsToPunish;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDeleteThreshold() {
        return deleteThreshold;
    }

    public AutoModAntiDup deleteThreshold(Integer deleteThreshold) {
        this.deleteThreshold = deleteThreshold;
        return this;
    }

    public void setDeleteThreshold(Integer deleteThreshold) {
        this.deleteThreshold = deleteThreshold;
    }

    public Integer getDupsToPunish() {
        return dupsToPunish;
    }

    public AutoModAntiDup dupsToPunish(Integer dupsToPunish) {
        this.dupsToPunish = dupsToPunish;
        return this;
    }

    public void setDupsToPunish(Integer dupsToPunish) {
        this.dupsToPunish = dupsToPunish;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoModAntiDup)) {
            return false;
        }
        return id != null && id.equals(((AutoModAntiDup) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AutoModAntiDup{" +
            "id=" + getId() +
            ", deleteThreshold=" + getDeleteThreshold() +
            ", dupsToPunish=" + getDupsToPunish() +
            "}";
    }
}
