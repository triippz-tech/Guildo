import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name={translate('global.menu.entities.main')} id="entity-menu">
    <MenuItem icon="asterisk" to="/entity/auto-mod-anti-dup">
      <Translate contentKey="global.menu.entities.botAutoModAntiDup" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/auto-mod-auto-raid">
      <Translate contentKey="global.menu.entities.botAutoModAutoRaid" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/auto-mod-ignore">
      <Translate contentKey="global.menu.entities.botAutoModIgnore" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/auto-mod-mentions">
      <Translate contentKey="global.menu.entities.botAutoModMentions" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/auto-moderation">
      <Translate contentKey="global.menu.entities.botAutoModeration" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/discord-user">
      <Translate contentKey="global.menu.entities.botDiscordUser" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/discord-user-profile">
      <Translate contentKey="global.menu.entities.botDiscordUserProfile" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/give-away">
      <Translate contentKey="global.menu.entities.botGiveAway" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-application">
      <Translate contentKey="global.menu.entities.botGuildApplication" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-application-form">
      <Translate contentKey="global.menu.entities.botGuildApplicationForm" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-event">
      <Translate contentKey="global.menu.entities.botGuildEvent" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-poll">
      <Translate contentKey="global.menu.entities.botGuildPoll" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-poll-item">
      <Translate contentKey="global.menu.entities.botGuildPollItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-server">
      <Translate contentKey="global.menu.entities.botGuildServer" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-server-profile">
      <Translate contentKey="global.menu.entities.botGuildServerProfile" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-server-settings">
      <Translate contentKey="global.menu.entities.botGuildServerSettings" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/moderation-log-item">
      <Translate contentKey="global.menu.entities.botModerationLogItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/mute">
      <Translate contentKey="global.menu.entities.botMute" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/punishment">
      <Translate contentKey="global.menu.entities.botPunishment" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/scheduled-announcement">
      <Translate contentKey="global.menu.entities.botScheduledAnnouncement" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/server-log-item">
      <Translate contentKey="global.menu.entities.botServerLogItem" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/temp-ban">
      <Translate contentKey="global.menu.entities.botTempBan" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/welcome-message">
      <Translate contentKey="global.menu.entities.botWelcomeMessage" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/page">
      <Translate contentKey="global.menu.entities.blogPage" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/root-page">
      <Translate contentKey="global.menu.entities.blogRootPage" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/user-strike">
      <Translate contentKey="global.menu.entities.botUserStrike" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/entity/guild-server-strike">
      <Translate contentKey="global.menu.entities.botGuildServerStrike" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
