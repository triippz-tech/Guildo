package com.triippztech.guildo.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

import com.triippztech.guildo.domain.enumeration.ApplicationStatus;

/**
 * Represents an application to a specific guild from a discord user\n@author Mark Tripoli
 */
@ApiModel(description = "Represents an application to a specific guild from a discord user\n@author Mark Tripoli")
@Entity
@Table(name = "guild_application")
public class GuildApplication implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "character_name", nullable = false)
    private String characterName;

    @NotNull
    @Column(name = "character_type", nullable = false)
    private String characterType;

    @Lob
    @Column(name = "application_file")
    private byte[] applicationFile;

    @Column(name = "application_file_content_type")
    private String applicationFileContentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @OneToOne
    @JoinColumn(unique = true)
    private DiscordUser acceptedBy;

    @ManyToOne
    @JsonIgnoreProperties("userApplications")
    private DiscordUser appliedUser;

    @ManyToOne
    @JsonIgnoreProperties("guildApplications")
    private GuildServer guildServer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCharacterName() {
        return characterName;
    }

    public GuildApplication characterName(String characterName) {
        this.characterName = characterName;
        return this;
    }

    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    public String getCharacterType() {
        return characterType;
    }

    public GuildApplication characterType(String characterType) {
        this.characterType = characterType;
        return this;
    }

    public void setCharacterType(String characterType) {
        this.characterType = characterType;
    }

    public byte[] getApplicationFile() {
        return applicationFile;
    }

    public GuildApplication applicationFile(byte[] applicationFile) {
        this.applicationFile = applicationFile;
        return this;
    }

    public void setApplicationFile(byte[] applicationFile) {
        this.applicationFile = applicationFile;
    }

    public String getApplicationFileContentType() {
        return applicationFileContentType;
    }

    public GuildApplication applicationFileContentType(String applicationFileContentType) {
        this.applicationFileContentType = applicationFileContentType;
        return this;
    }

    public void setApplicationFileContentType(String applicationFileContentType) {
        this.applicationFileContentType = applicationFileContentType;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public GuildApplication status(ApplicationStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public DiscordUser getAcceptedBy() {
        return acceptedBy;
    }

    public GuildApplication acceptedBy(DiscordUser discordUser) {
        this.acceptedBy = discordUser;
        return this;
    }

    public void setAcceptedBy(DiscordUser discordUser) {
        this.acceptedBy = discordUser;
    }

    public DiscordUser getAppliedUser() {
        return appliedUser;
    }

    public GuildApplication appliedUser(DiscordUser discordUser) {
        this.appliedUser = discordUser;
        return this;
    }

    public void setAppliedUser(DiscordUser discordUser) {
        this.appliedUser = discordUser;
    }

    public GuildServer getGuildServer() {
        return guildServer;
    }

    public GuildApplication guildServer(GuildServer guildServer) {
        this.guildServer = guildServer;
        return this;
    }

    public void setGuildServer(GuildServer guildServer) {
        this.guildServer = guildServer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildApplication)) {
            return false;
        }
        return id != null && id.equals(((GuildApplication) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildApplication{" +
            "id=" + getId() +
            ", characterName='" + getCharacterName() + "'" +
            ", characterType='" + getCharacterType() + "'" +
            ", applicationFile='" + getApplicationFile() + "'" +
            ", applicationFileContentType='" + getApplicationFileContentType() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
