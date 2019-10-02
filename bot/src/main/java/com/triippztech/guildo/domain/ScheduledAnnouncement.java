package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents an Annoucement which will be broadcasted\nacross a Discord Server at a later date\n@author Mark Tripoli
 */
@ApiModel(description = "Represents an Annoucement which will be broadcasted\nacross a Discord Server at a later date\n@author Mark Tripoli")
@Entity
@Table(name = "scheduled_announcement")
public class ScheduledAnnouncement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "annoucement_title", length = 250, nullable = false)
    private String annoucementTitle;

    @NotNull
    @Size(max = 250)
    @Column(name = "annoucement_img_url", length = 250, nullable = false)
    private String annoucementImgUrl;

    
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "annoucement_message", nullable = false)
    private String annoucementMessage;

    @NotNull
    @Column(name = "annoucement_fire", nullable = false)
    private Instant annoucementFire;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @ManyToOne
    @JsonIgnoreProperties("guildAnnoucements")
    private GuildServer annouceGuild;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnnoucementTitle() {
        return annoucementTitle;
    }

    public ScheduledAnnouncement annoucementTitle(String annoucementTitle) {
        this.annoucementTitle = annoucementTitle;
        return this;
    }

    public void setAnnoucementTitle(String annoucementTitle) {
        this.annoucementTitle = annoucementTitle;
    }

    public String getAnnoucementImgUrl() {
        return annoucementImgUrl;
    }

    public ScheduledAnnouncement annoucementImgUrl(String annoucementImgUrl) {
        this.annoucementImgUrl = annoucementImgUrl;
        return this;
    }

    public void setAnnoucementImgUrl(String annoucementImgUrl) {
        this.annoucementImgUrl = annoucementImgUrl;
    }

    public String getAnnoucementMessage() {
        return annoucementMessage;
    }

    public ScheduledAnnouncement annoucementMessage(String annoucementMessage) {
        this.annoucementMessage = annoucementMessage;
        return this;
    }

    public void setAnnoucementMessage(String annoucementMessage) {
        this.annoucementMessage = annoucementMessage;
    }

    public Instant getAnnoucementFire() {
        return annoucementFire;
    }

    public ScheduledAnnouncement annoucementFire(Instant annoucementFire) {
        this.annoucementFire = annoucementFire;
        return this;
    }

    public void setAnnoucementFire(Instant annoucementFire) {
        this.annoucementFire = annoucementFire;
    }

    public Long getGuildId() {
        return guildId;
    }

    public ScheduledAnnouncement guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public GuildServer getAnnouceGuild() {
        return annouceGuild;
    }

    public ScheduledAnnouncement annouceGuild(GuildServer guildServer) {
        this.annouceGuild = guildServer;
        return this;
    }

    public void setAnnouceGuild(GuildServer guildServer) {
        this.annouceGuild = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScheduledAnnouncement)) {
            return false;
        }
        return id != null && id.equals(((ScheduledAnnouncement) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ScheduledAnnouncement{" +
            "id=" + getId() +
            ", annoucementTitle='" + getAnnoucementTitle() + "'" +
            ", annoucementImgUrl='" + getAnnoucementImgUrl() + "'" +
            ", annoucementMessage='" + getAnnoucementMessage() + "'" +
            ", annoucementFire='" + getAnnoucementFire() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
