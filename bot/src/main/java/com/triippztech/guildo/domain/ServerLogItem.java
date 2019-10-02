package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.triippztech.guildo.domain.enumeration.Activity;

/**
 * Represents a Log entry of all service specific events (name changes, avatar change, etc)\n@author Mark Tripoli
 */
@ApiModel(description = "Represents a Log entry of all service specific events (name changes, avatar change, etc)\n@author Mark Tripoli")
@Entity
@Table(name = "server_log_item")
public class ServerLogItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activity", nullable = false)
    private Activity activity;

    @NotNull
    @Column(name = "channel_id", nullable = false)
    private Long channelId;

    @NotNull
    @Column(name = "channel_name", nullable = false)
    private String channelName;

    @NotNull
    @Column(name = "time", nullable = false)
    private Instant time;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotNull
    @Column(name = "guild_id", nullable = false)
    private Long guildId;

    @ManyToOne
    @JsonIgnoreProperties("serverLogItems")
    private GuildServer serverItemGuildServer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Activity getActivity() {
        return activity;
    }

    public ServerLogItem activity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Long getChannelId() {
        return channelId;
    }

    public ServerLogItem channelId(Long channelId) {
        this.channelId = channelId;
        return this;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public ServerLogItem channelName(String channelName) {
        this.channelName = channelName;
        return this;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Instant getTime() {
        return time;
    }

    public ServerLogItem time(Instant time) {
        this.time = time;
        return this;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public ServerLogItem userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public ServerLogItem userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getGuildId() {
        return guildId;
    }

    public ServerLogItem guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public GuildServer getServerItemGuildServer() {
        return serverItemGuildServer;
    }

    public ServerLogItem serverItemGuildServer(GuildServer guildServer) {
        this.serverItemGuildServer = guildServer;
        return this;
    }

    public void setServerItemGuildServer(GuildServer guildServer) {
        this.serverItemGuildServer = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerLogItem)) {
            return false;
        }
        return id != null && id.equals(((ServerLogItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ServerLogItem{" +
            "id=" + getId() +
            ", activity='" + getActivity() + "'" +
            ", channelId=" + getChannelId() +
            ", channelName='" + getChannelName() + "'" +
            ", time='" + getTime() + "'" +
            ", userId=" + getUserId() +
            ", userName='" + getUserName() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
