package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Represents a Poll an authorized Discord User\nmay create in a particular Guild\n@author Mark Tripoli
 */
@ApiModel(description = "Represents a Poll an authorized Discord User\nmay create in a particular Guild\n@author Mark Tripoli")
@Entity
@Table(name = "guild_poll")
public class GuildPoll implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "poll_name", nullable = false)
    private String pollName;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @NotNull
    @Column(name = "text_channel_id", nullable = false)
    private Long textChannelId;

    @NotNull
    @Column(name = "finish_time", nullable = false)
    private LocalDate finishTime;

    @NotNull
    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "guild_id")
    private Long guildId;

    @ManyToOne
    @JsonIgnoreProperties("guildPolls")
    private GuildPollItem pollItems;

    @ManyToOne
    @JsonIgnoreProperties("serverPolls")
    private GuildServer pollServer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPollName() {
        return pollName;
    }

    public GuildPoll pollName(String pollName) {
        this.pollName = pollName;
        return this;
    }

    public void setPollName(String pollName) {
        this.pollName = pollName;
    }

    public String getDescription() {
        return description;
    }

    public GuildPoll description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTextChannelId() {
        return textChannelId;
    }

    public GuildPoll textChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
        return this;
    }

    public void setTextChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
    }

    public LocalDate getFinishTime() {
        return finishTime;
    }

    public GuildPoll finishTime(LocalDate finishTime) {
        this.finishTime = finishTime;
        return this;
    }

    public void setFinishTime(LocalDate finishTime) {
        this.finishTime = finishTime;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public GuildPoll completed(Boolean completed) {
        this.completed = completed;
        return this;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Long getGuildId() {
        return guildId;
    }

    public GuildPoll guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public GuildPollItem getPollItems() {
        return pollItems;
    }

    public GuildPoll pollItems(GuildPollItem guildPollItem) {
        this.pollItems = guildPollItem;
        return this;
    }

    public void setPollItems(GuildPollItem guildPollItem) {
        this.pollItems = guildPollItem;
    }

    public GuildServer getPollServer() {
        return pollServer;
    }

    public GuildPoll pollServer(GuildServer guildServer) {
        this.pollServer = guildServer;
        return this;
    }

    public void setPollServer(GuildServer guildServer) {
        this.pollServer = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildPoll)) {
            return false;
        }
        return id != null && id.equals(((GuildPoll) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildPoll{" +
            "id=" + getId() +
            ", pollName='" + getPollName() + "'" +
            ", description='" + getDescription() + "'" +
            ", textChannelId=" + getTextChannelId() +
            ", finishTime='" + getFinishTime() + "'" +
            ", completed='" + isCompleted() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
