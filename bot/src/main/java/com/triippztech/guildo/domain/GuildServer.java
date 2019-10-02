package com.triippztech.guildo.domain;
import io.swagger.annotations.ApiModel;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.triippztech.guildo.domain.enumeration.GuildServerLevel;

/**
 * Represents a Guild Server (Discord Server)\n@author Mark Tripoli
 */
@ApiModel(description = "Represents a Guild Server (Discord Server)\n@author Mark Tripoli")
@Entity
@Table(name = "guild_server")
public class GuildServer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "guild_id", nullable = false, unique = true)
    private Long guildId;

    @NotNull
    @Column(name = "guild_name", nullable = false)
    private String guildName;

    @Column(name = "icon")
    private String icon;

    @NotNull
    @Column(name = "owner", nullable = false)
    private Long owner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "server_level", nullable = false)
    private GuildServerLevel serverLevel;

    @OneToOne
    @JoinColumn(unique = true)
    private GuildServerProfile guildProfile;

    @OneToOne
    @JoinColumn(unique = true)
    private GuildApplicationForm applicationForm;

    @OneToOne
    @JoinColumn(unique = true)
    private GuildServerSettings guildSettings;

    @OneToOne
    @JoinColumn(unique = true)
    private WelcomeMessage welcomeMessage;

    @OneToMany(mappedBy = "pollServer")
    private Set<GuildPoll> serverPolls = new HashSet<>();

    @OneToMany(mappedBy = "annouceGuild")
    private Set<ScheduledAnnouncement> guildAnnoucements = new HashSet<>();

    @OneToMany(mappedBy = "eventGuild")
    private Set<GuildEvent> guildEvents = new HashSet<>();

    @OneToMany(mappedBy = "guildGiveAway")
    private Set<GiveAway> giveAways = new HashSet<>();

    @OneToMany(mappedBy = "modItemGuildServer")
    private Set<ModerationLogItem> modLogItems = new HashSet<>();

    @OneToMany(mappedBy = "serverItemGuildServer")
    private Set<ServerLogItem> serverLogItems = new HashSet<>();

    @OneToMany(mappedBy = "tempBanGuildServer")
    private Set<TempBan> guildTempBans = new HashSet<>();

    @OneToMany(mappedBy = "mutedGuildServer")
    private Set<Mute> mutedUsers = new HashSet<>();

    @OneToMany(mappedBy = "guildServer")
    private Set<GuildApplication> guildApplications = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public GuildServer guildId(Long guildId) {
        this.guildId = guildId;
        return this;
    }

    public void setGuildId(Long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public GuildServer guildName(String guildName) {
        this.guildName = guildName;
        return this;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public String getIcon() {
        return icon;
    }

    public GuildServer icon(String icon) {
        this.icon = icon;
        return this;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Long getOwner() {
        return owner;
    }

    public GuildServer owner(Long owner) {
        this.owner = owner;
        return this;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public GuildServerLevel getServerLevel() {
        return serverLevel;
    }

    public GuildServer serverLevel(GuildServerLevel serverLevel) {
        this.serverLevel = serverLevel;
        return this;
    }

    public void setServerLevel(GuildServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    public GuildServerProfile getGuildProfile() {
        return guildProfile;
    }

    public GuildServer guildProfile(GuildServerProfile guildServerProfile) {
        this.guildProfile = guildServerProfile;
        return this;
    }

    public void setGuildProfile(GuildServerProfile guildServerProfile) {
        this.guildProfile = guildServerProfile;
    }

    public GuildApplicationForm getApplicationForm() {
        return applicationForm;
    }

    public GuildServer applicationForm(GuildApplicationForm guildApplicationForm) {
        this.applicationForm = guildApplicationForm;
        return this;
    }

    public void setApplicationForm(GuildApplicationForm guildApplicationForm) {
        this.applicationForm = guildApplicationForm;
    }

    public GuildServerSettings getGuildSettings() {
        return guildSettings;
    }

    public GuildServer guildSettings(GuildServerSettings guildServerSettings) {
        this.guildSettings = guildServerSettings;
        return this;
    }

    public void setGuildSettings(GuildServerSettings guildServerSettings) {
        this.guildSettings = guildServerSettings;
    }

    public WelcomeMessage getWelcomeMessage() {
        return welcomeMessage;
    }

    public GuildServer welcomeMessage(WelcomeMessage welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
        return this;
    }

    public void setWelcomeMessage(WelcomeMessage welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public Set<GuildPoll> getServerPolls() {
        return serverPolls;
    }

    public GuildServer serverPolls(Set<GuildPoll> guildPolls) {
        this.serverPolls = guildPolls;
        return this;
    }

    public GuildServer addServerPolls(GuildPoll guildPoll) {
        this.serverPolls.add(guildPoll);
        guildPoll.setPollServer(this);
        return this;
    }

    public GuildServer removeServerPolls(GuildPoll guildPoll) {
        this.serverPolls.remove(guildPoll);
        guildPoll.setPollServer(null);
        return this;
    }

    public void setServerPolls(Set<GuildPoll> guildPolls) {
        this.serverPolls = guildPolls;
    }

    public Set<ScheduledAnnouncement> getGuildAnnoucements() {
        return guildAnnoucements;
    }

    public GuildServer guildAnnoucements(Set<ScheduledAnnouncement> scheduledAnnouncements) {
        this.guildAnnoucements = scheduledAnnouncements;
        return this;
    }

    public GuildServer addGuildAnnoucements(ScheduledAnnouncement scheduledAnnouncement) {
        this.guildAnnoucements.add(scheduledAnnouncement);
        scheduledAnnouncement.setAnnouceGuild(this);
        return this;
    }

    public GuildServer removeGuildAnnoucements(ScheduledAnnouncement scheduledAnnouncement) {
        this.guildAnnoucements.remove(scheduledAnnouncement);
        scheduledAnnouncement.setAnnouceGuild(null);
        return this;
    }

    public void setGuildAnnoucements(Set<ScheduledAnnouncement> scheduledAnnouncements) {
        this.guildAnnoucements = scheduledAnnouncements;
    }

    public Set<GuildEvent> getGuildEvents() {
        return guildEvents;
    }

    public GuildServer guildEvents(Set<GuildEvent> guildEvents) {
        this.guildEvents = guildEvents;
        return this;
    }

    public GuildServer addGuildEvents(GuildEvent guildEvent) {
        this.guildEvents.add(guildEvent);
        guildEvent.setEventGuild(this);
        return this;
    }

    public GuildServer removeGuildEvents(GuildEvent guildEvent) {
        this.guildEvents.remove(guildEvent);
        guildEvent.setEventGuild(null);
        return this;
    }

    public void setGuildEvents(Set<GuildEvent> guildEvents) {
        this.guildEvents = guildEvents;
    }

    public Set<GiveAway> getGiveAways() {
        return giveAways;
    }

    public GuildServer giveAways(Set<GiveAway> giveAways) {
        this.giveAways = giveAways;
        return this;
    }

    public GuildServer addGiveAways(GiveAway giveAway) {
        this.giveAways.add(giveAway);
        giveAway.setGuildGiveAway(this);
        return this;
    }

    public GuildServer removeGiveAways(GiveAway giveAway) {
        this.giveAways.remove(giveAway);
        giveAway.setGuildGiveAway(null);
        return this;
    }

    public void setGiveAways(Set<GiveAway> giveAways) {
        this.giveAways = giveAways;
    }

    public Set<ModerationLogItem> getModLogItems() {
        return modLogItems;
    }

    public GuildServer modLogItems(Set<ModerationLogItem> moderationLogItems) {
        this.modLogItems = moderationLogItems;
        return this;
    }

    public GuildServer addModLogItems(ModerationLogItem moderationLogItem) {
        this.modLogItems.add(moderationLogItem);
        moderationLogItem.setModItemGuildServer(this);
        return this;
    }

    public GuildServer removeModLogItems(ModerationLogItem moderationLogItem) {
        this.modLogItems.remove(moderationLogItem);
        moderationLogItem.setModItemGuildServer(null);
        return this;
    }

    public void setModLogItems(Set<ModerationLogItem> moderationLogItems) {
        this.modLogItems = moderationLogItems;
    }

    public Set<ServerLogItem> getServerLogItems() {
        return serverLogItems;
    }

    public GuildServer serverLogItems(Set<ServerLogItem> serverLogItems) {
        this.serverLogItems = serverLogItems;
        return this;
    }

    public GuildServer addServerLogItems(ServerLogItem serverLogItem) {
        this.serverLogItems.add(serverLogItem);
        serverLogItem.setServerItemGuildServer(this);
        return this;
    }

    public GuildServer removeServerLogItems(ServerLogItem serverLogItem) {
        this.serverLogItems.remove(serverLogItem);
        serverLogItem.setServerItemGuildServer(null);
        return this;
    }

    public void setServerLogItems(Set<ServerLogItem> serverLogItems) {
        this.serverLogItems = serverLogItems;
    }

    public Set<TempBan> getGuildTempBans() {
        return guildTempBans;
    }

    public GuildServer guildTempBans(Set<TempBan> tempBans) {
        this.guildTempBans = tempBans;
        return this;
    }

    public GuildServer addGuildTempBans(TempBan tempBan) {
        this.guildTempBans.add(tempBan);
        tempBan.setTempBanGuildServer(this);
        return this;
    }

    public GuildServer removeGuildTempBans(TempBan tempBan) {
        this.guildTempBans.remove(tempBan);
        tempBan.setTempBanGuildServer(null);
        return this;
    }

    public void setGuildTempBans(Set<TempBan> tempBans) {
        this.guildTempBans = tempBans;
    }

    public Set<Mute> getMutedUsers() {
        return mutedUsers;
    }

    public GuildServer mutedUsers(Set<Mute> mutes) {
        this.mutedUsers = mutes;
        return this;
    }

    public GuildServer addMutedUsers(Mute mute) {
        this.mutedUsers.add(mute);
        mute.setMutedGuildServer(this);
        return this;
    }

    public GuildServer removeMutedUsers(Mute mute) {
        this.mutedUsers.remove(mute);
        mute.setMutedGuildServer(null);
        return this;
    }

    public void setMutedUsers(Set<Mute> mutes) {
        this.mutedUsers = mutes;
    }

    public Set<GuildApplication> getGuildApplications() {
        return guildApplications;
    }

    public GuildServer guildApplications(Set<GuildApplication> guildApplications) {
        this.guildApplications = guildApplications;
        return this;
    }

    public GuildServer addGuildApplications(GuildApplication guildApplication) {
        this.guildApplications.add(guildApplication);
        guildApplication.setGuildServer(this);
        return this;
    }

    public GuildServer removeGuildApplications(GuildApplication guildApplication) {
        this.guildApplications.remove(guildApplication);
        guildApplication.setGuildServer(null);
        return this;
    }

    public void setGuildApplications(Set<GuildApplication> guildApplications) {
        this.guildApplications = guildApplications;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuildServer)) {
            return false;
        }
        return id != null && id.equals(((GuildServer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GuildServer{" +
            "id=" + getId() +
            ", guildId=" + getGuildId() +
            ", guildName='" + getGuildName() + "'" +
            ", icon='" + getIcon() + "'" +
            ", owner=" + getOwner() +
            ", serverLevel='" + getServerLevel() + "'" +
            "}";
    }

    public GuildServer() {
    }

    public GuildServer(@NotNull Long guildId, @NotNull String guildName, @NotNull Long ownerId, String icon) {
        this.guildId = guildId;
        this.guildName = guildName;
        this.owner = ownerId;
        this.icon = icon;
        this.serverLevel = GuildServerLevel.STANDARD;
    }
}
