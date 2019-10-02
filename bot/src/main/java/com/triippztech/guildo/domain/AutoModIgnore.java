package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents the the roles and in what channel\nwill be ignored (text) when AutoModeration is enabled\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the the roles and in what channel\nwill be ignored (text) when AutoModeration is enabled\n@author Mark Tripoli")
@Entity
@Table(name = "auto_mod_ignore")
public class AutoModIgnore implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotNull
    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public AutoModIgnore roleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getChannelId() {
        return channelId;
    }

    public AutoModIgnore channelId(Long channelId) {
        this.channelId = channelId;
        return this;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AutoModIgnore)) {
            return false;
        }
        return id != null && id.equals(((AutoModIgnore) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "AutoModIgnore{" +
            "id=" + getId() +
            ", roleId=" + getRoleId() +
            ", channelId=" + getChannelId() +
            "}";
    }
}
