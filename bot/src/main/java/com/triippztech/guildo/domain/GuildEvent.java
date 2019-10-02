package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents an Event owned by a guild (raid, dungeon, etc)\n@author Mark Tripoli
 */
@ApiModel(description = "Represents an Event owned by a guild (raid, dungeon, etc)\n@author Mark Tripoli")
@Entity
@Table(name = "guild_event")
public class GuildEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "event_name", length = 250, nullable = false)
    private String eventName;

    @NotNull
    @Size(max = 250)
    @Column(name = "event_image_url", length = 250, nullable = false)
    private String eventImageUrl;

    
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "event_message", nullable = false)
    private String eventMessage;

    @NotNull
    @Column(name = "event_start", nullable = false)
    private Instant eventStart;

    @Column(name = "guild_id")
    private Long guildId;

    @ManyToOne
    @JsonIgnoreProperties("guildEvents")
    private GuildServer eventGuild;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public GuildEvent eventName(String eventName) {
        this.eventName = eventName;
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public GuildEvent eventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
        return this;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventMessage() {
        return eventMessage;
    }

    public GuildEvent eventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
        return this;
    }

    public void setEventMessage(String eventMessage) {
        this.eventMessage = eventMessage;
    }

    public Instant getEventStart() {
        return eventStart;
    }

    public GuildEvent eventStart(Instant eventStart) {
        this.eventStart = eventStart;
        return this;
    }

    public void setEventStart(Instant eventStart) {
        this.eventStart = eventStart;
    }

    public Long getGuildId() {
        return guildId;
    }

    public GuildEvent guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public GuildServer getEventGuild() {
        return eventGuild;
    }

    public GuildEvent eventGuild(GuildServer guildServer) {
        this.eventGuild = guildServer;
        return this;
    }

    public void setEventGuild(GuildServer guildServer) {
        this.eventGuild = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildEvent)) {
            return false;
        }
        return id != null && id.equals(((GuildEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildEvent{" +
            "id=" + getId() +
            ", eventName='" + getEventName() + "'" +
            ", eventImageUrl='" + getEventImageUrl() + "'" +
            ", eventMessage='" + getEventMessage() + "'" +
            ", eventStart='" + getEventStart() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
