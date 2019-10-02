package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * Represents A Giveaway which a Guild User may create\n@author Mark Tripoli
 */
@ApiModel(description = "Represents A Giveaway which a Guild User may create\n@author Mark Tripoli")
@Entity
@Table(name = "give_away")
public class GiveAway implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(max = 250)
    @Column(name = "name", length = 250, nullable = false)
    private String name;

    @Column(name = "image")
    private String image;

    
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Column(name = "message_id", nullable = false)
    private Long messageId;

    @NotNull
    @Column(name = "text_channel_id", nullable = false)
    private Long textChannelId;

    @NotNull
    @Column(name = "finish", nullable = false)
    private Instant finish;

    @NotNull
    @Column(name = "expired", nullable = false)
    private Boolean expired;

    @Column(name = "guild_id")
    private Long guildId;

    @OneToOne
    @JoinColumn(unique = true)
    private DiscordUser winner;

    @ManyToOne
    @JsonIgnoreProperties("giveAways")
    private GuildServer guildGiveAway;

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

    public GiveAway name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public GiveAway image(String image) {
        this.image = image;
        return this;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public GiveAway message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageId() {
        return messageId;
    }

    public GiveAway messageId(Long messageId) {
        this.messageId = messageId;
        return this;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Long getTextChannelId() {
        return textChannelId;
    }

    public GiveAway textChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
        return this;
    }

    public void setTextChannelId(Long textChannelId) {
        this.textChannelId = textChannelId;
    }

    public Instant getFinish() {
        return finish;
    }

    public GiveAway finish(Instant finish) {
        this.finish = finish;
        return this;
    }

    public void setFinish(Instant finish) {
        this.finish = finish;
    }

    public Boolean isExpired() {
        return expired;
    }

    public GiveAway expired(Boolean expired) {
        this.expired = expired;
        return this;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Long getGuildId() {
        return guildId;
    }

    public GiveAway guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public DiscordUser getWinner() {
        return winner;
    }

    public GiveAway winner(DiscordUser discordUser) {
        this.winner = discordUser;
        return this;
    }

    public void setWinner(DiscordUser discordUser) {
        this.winner = discordUser;
    }

    public GuildServer getGuildGiveAway() {
        return guildGiveAway;
    }

    public GiveAway guildGiveAway(GuildServer guildServer) {
        this.guildGiveAway = guildServer;
        return this;
    }

    public void setGuildGiveAway(GuildServer guildServer) {
        this.guildGiveAway = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GiveAway)) {
            return false;
        }
        return id != null && id.equals(((GiveAway) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GiveAway{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", image='" + getImage() + "'" +
            ", message='" + getMessage() + "'" +
            ", messageId=" + getMessageId() +
            ", textChannelId=" + getTextChannelId() +
            ", finish='" + getFinish() + "'" +
            ", expired='" + isExpired() + "'" +
            ", guildId=" + getGuildId() +
            "}";
    }
}
