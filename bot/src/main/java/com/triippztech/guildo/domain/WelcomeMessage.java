package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * Represents A welcome message used to greet\nnew users to a Guild\n@author Mark Tripoli
 */
@ApiModel(description = "Represents A welcome message used to greet\nnew users to a Guild\n@author Mark Tripoli")
@Entity
@Table(name = "welcome_message")
public class WelcomeMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "message_title", nullable = false)
    private String messageTitle;

    @NotNull
    @Column(name = "body", nullable = false)
    private String body;

    @NotNull
    @Column(name = "footer", nullable = false)
    private String footer;

    @Column(name = "logo_url")
    private String logoUrl;

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

    public String getName() {
        return name;
    }

    public WelcomeMessage name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public WelcomeMessage messageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
        return this;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getBody() {
        return body;
    }

    public WelcomeMessage body(String body) {
        this.body = body;
        return this;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public WelcomeMessage footer(String footer) {
        this.footer = footer;
        return this;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public WelcomeMessage logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Long getGuildId() {
        return guildId;
    }

    public WelcomeMessage guildId(Long guildId) {
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
        if (!(o instanceof WelcomeMessage)) {
            return false;
        }
        return id != null && id.equals(((WelcomeMessage) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "WelcomeMessage{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", messageTitle='" + getMessageTitle() + "'" +
            ", body='" + getBody() + "'" +
            ", footer='" + getFooter() + "'" +
            ", logoUrl='" + getLogoUrl() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
