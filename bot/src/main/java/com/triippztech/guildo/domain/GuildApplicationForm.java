package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents the Application Form (doc,txt, etc.) which a Discord User\nmay need to fillout and submit within their application\n@author Mark Tripoli
 */
@ApiModel(description = "Represents the Application Form (doc,txt, etc.) which a Discord User\nmay need to fillout and submit within their application\n@author Mark Tripoli")
@Entity
@Table(name = "guild_application_form")
public class GuildApplicationForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    
    @Lob
    @Column(name = "application_form", nullable = false)
    private byte[] applicationForm;

    @Column(name = "application_form_content_type", nullable = false)
    private String applicationFormContentType;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getApplicationForm() {
        return applicationForm;
    }

    public GuildApplicationForm applicationForm(byte[] applicationForm) {
        this.applicationForm = applicationForm;
        return this;
    }

    public void setApplicationForm(byte[] applicationForm) {
        this.applicationForm = applicationForm;
    }

    public String getApplicationFormContentType() {
        return applicationFormContentType;
    }

    public GuildApplicationForm applicationFormContentType(String applicationFormContentType) {
        this.applicationFormContentType = applicationFormContentType;
        return this;
    }

    public void setApplicationFormContentType(String applicationFormContentType) {
        this.applicationFormContentType = applicationFormContentType;
    }

    public Long getGuildId() {
        return guildId;
    }

    public GuildApplicationForm guildId(Long guildId) {
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
        if (!(o instanceof GuildApplicationForm)) {
            return false;
        }
        return id != null && id.equals(((GuildApplicationForm) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildApplicationForm{" +
            "id=" + getId() +
            ", applicationForm='" + getApplicationForm() + "'" +
            ", applicationFormContentType='" + getApplicationFormContentType() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
