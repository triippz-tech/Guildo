package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents The AutoRaid configuration. This\nis used to determine if autoraid is enabled\nand the threshhold to look back in time to see\nhow many users have joined the server recently\n@author Mark Tripoli
 */
@ApiModel(description = "Represents The AutoRaid configuration. This\nis used to determine if autoraid is enabled\nand the threshhold to look back in time to see\nhow many users have joined the server recently\n@author Mark Tripoli")
@Entity
@Table(name = "auto_mod_auto_raid")
public class AutoModAutoRaid implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "auto_raid_enabled", nullable = false)
    private Boolean autoRaidEnabled;

    @NotNull
    @Column(name = "auto_raid_time_threshold", nullable = false)
    private Integer autoRaidTimeThreshold;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isAutoRaidEnabled() {
        return autoRaidEnabled;
    }

    public AutoModAutoRaid autoRaidEnabled(Boolean autoRaidEnabled) {
        this.autoRaidEnabled = autoRaidEnabled;
        return this;
    }

    public void setAutoRaidEnabled(Boolean autoRaidEnabled) {
        this.autoRaidEnabled = autoRaidEnabled;
    }

    public Integer getAutoRaidTimeThreshold() {
        return autoRaidTimeThreshold;
    }

    public AutoModAutoRaid autoRaidTimeThreshold(Integer autoRaidTimeThreshold) {
        this.autoRaidTimeThreshold = autoRaidTimeThreshold;
        return this;
    }

    public void setAutoRaidTimeThreshold(Integer autoRaidTimeThreshold) {
        this.autoRaidTimeThreshold = autoRaidTimeThreshold;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoModAutoRaid)) {
            return false;
        }
        return id != null && id.equals(((AutoModAutoRaid) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AutoModAutoRaid{" +
            "id=" + getId() +
            ", autoRaidEnabled='" + isAutoRaidEnabled() + "'" +
            ", autoRaidTimeThreshold=" + getAutoRaidTimeThreshold() +
            "}";
    }
}
