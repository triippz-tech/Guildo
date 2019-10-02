package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.triippztech.guildo.domain.enumeration.DiscordUserLevel;

/**
 * Represents a physical Discord user used by Guildo.\n@author Mark Tripoli
 */
@ApiModel(description = "Represents a physical Discord user used by Guildo.\n@author Mark Tripoli")
@Entity
@Table(name = "discord_user")
public class DiscordUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @NotNull
    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "icon")
    private String icon;

    @NotNull
    @Column(name = "commands_issued", nullable = false)
    private Integer commandsIssued;

    @NotNull
    @Column(name = "blacklisted", nullable = false)
    private Boolean blacklisted;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_level", nullable = false)
    private DiscordUserLevel userLevel;

    @OneToOne
    @JoinColumn(unique = true)
    private DiscordUserProfile userProfile;

    @OneToMany(mappedBy = "bannedUser", fetch = FetchType.EAGER)
    private Set<TempBan> userTempBans = new HashSet<>();

    @OneToMany(mappedBy = "mutedUser", fetch = FetchType.EAGER)
    private Set<Mute> userMutes = new HashSet<>();

    @OneToMany(mappedBy = "appliedUser", fetch = FetchType.EAGER)
    private Set<GuildApplication> userApplications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public DiscordUser userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public DiscordUser userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIcon() {
        return icon;
    }

    public DiscordUser icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getCommandsIssued() {
        return commandsIssued;
    }

    public DiscordUser commandsIssued(Integer commandsIssued) {
        this.commandsIssued = commandsIssued;
        return this;
    }

    public void setCommandsIssued(Integer commandsIssued) {
        this.commandsIssued = commandsIssued;
    }

    public Boolean isBlacklisted() {
        return blacklisted;
    }

    public DiscordUser blacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
        return this;
    }

    public void setBlacklisted(Boolean blacklisted) {
        this.blacklisted = blacklisted;
    }

    public DiscordUserLevel getUserLevel() {
        return userLevel;
    }

    public DiscordUser userLevel(DiscordUserLevel userLevel) {
        this.userLevel = userLevel;
        return this;
    }

    public void setUserLevel(DiscordUserLevel userLevel) {
        this.userLevel = userLevel;
    }

    public DiscordUserProfile getUserProfile() {
        return userProfile;
    }

    public DiscordUser userProfile(DiscordUserProfile discordUserProfile) {
        this.userProfile = discordUserProfile;
        return this;
    }

    public void setUserProfile(DiscordUserProfile discordUserProfile) {
        this.userProfile = discordUserProfile;
    }

    public Set<TempBan> getUserTempBans() {
        return userTempBans;
    }

    public DiscordUser userTempBans(Set<TempBan> tempBans) {
        this.userTempBans = tempBans;
        return this;
    }

    public DiscordUser addUserTempBans(TempBan tempBan) {
        this.userTempBans.add(tempBan);
        tempBan.setBannedUser(this);
        return this;
    }

    public DiscordUser removeUserTempBans(TempBan tempBan) {
        this.userTempBans.remove(tempBan);
        tempBan.setBannedUser(null);
        return this;
    }

    public void setUserTempBans(Set<TempBan> tempBans) {
        this.userTempBans = tempBans;
    }

    public Set<Mute> getUserMutes() {
        return userMutes;
    }

    public DiscordUser userMutes(Set<Mute> mutes) {
        this.userMutes = mutes;
        return this;
    }

    public DiscordUser addUserMutes(Mute mute) {
        this.userMutes.add(mute);
        mute.setMutedUser(this);
        return this;
    }

    public DiscordUser removeUserMutes(Mute mute) {
        this.userMutes.remove(mute);
        mute.setMutedUser(null);
        return this;
    }

    public void setUserMutes(Set<Mute> mutes) {
        this.userMutes = mutes;
    }

    public Set<GuildApplication> getUserApplications() {
        return userApplications;
    }

    public DiscordUser userApplications(Set<GuildApplication> guildApplications) {
        this.userApplications = guildApplications;
        return this;
    }

    public DiscordUser addUserApplications(GuildApplication guildApplication) {
        this.userApplications.add(guildApplication);
        guildApplication.setAppliedUser(this);
        return this;
    }

    public DiscordUser removeUserApplications(GuildApplication guildApplication) {
        this.userApplications.remove(guildApplication);
        guildApplication.setAppliedUser(null);
        return this;
    }

    public void setUserApplications(Set<GuildApplication> guildApplications) {
        this.userApplications = guildApplications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DiscordUser)) {
            return false;
        }
        return id != null && id.equals(((DiscordUser) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "DiscordUser{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", userName='" + getUserName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", commandsIssued=" + getCommandsIssued() +
            ", blacklisted='" + isBlacklisted() + "'" +
            ", userLevel='" + getUserLevel() + "'" +
            "}";
    }

    public DiscordUser() {
    }

    public DiscordUser(@NotNull Long userId, @NotNull String userName, String icon, @NotNull DiscordUserProfile userProfile) {
        this.userId = userId;
        this.userName = userName;
        this.icon = icon;
        this.commandsIssued = 0;
        this.userProfile = userProfile;
        this.blacklisted = false;
        this.userLevel = DiscordUserLevel.STANDARD;
    }
}
