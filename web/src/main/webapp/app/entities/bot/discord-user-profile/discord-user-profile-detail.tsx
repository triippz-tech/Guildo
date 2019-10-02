import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './discord-user-profile.reducer';
import { IDiscordUserProfile } from 'app/shared/model/bot/discord-user-profile.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDiscordUserProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DiscordUserProfileDetail extends React.Component<IDiscordUserProfileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { discordUserProfileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botDiscordUserProfile.detail.title">DiscordUserProfile</Translate> [
            <b>{discordUserProfileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="favoriteGame">
                <Translate contentKey="webApp.botDiscordUserProfile.favoriteGame">Favorite Game</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.favoriteGame}</dd>
            <dt>
              <span id="profilePhoto">
                <Translate contentKey="webApp.botDiscordUserProfile.profilePhoto">Profile Photo</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.profilePhoto}</dd>
            <dt>
              <span id="twitterUrl">
                <Translate contentKey="webApp.botDiscordUserProfile.twitterUrl">Twitter Url</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.twitterUrl}</dd>
            <dt>
              <span id="twitchUrl">
                <Translate contentKey="webApp.botDiscordUserProfile.twitchUrl">Twitch Url</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.twitchUrl}</dd>
            <dt>
              <span id="youtubeUrl">
                <Translate contentKey="webApp.botDiscordUserProfile.youtubeUrl">Youtube Url</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.youtubeUrl}</dd>
            <dt>
              <span id="facebookUrl">
                <Translate contentKey="webApp.botDiscordUserProfile.facebookUrl">Facebook Url</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.facebookUrl}</dd>
            <dt>
              <span id="hitboxUrl">
                <Translate contentKey="webApp.botDiscordUserProfile.hitboxUrl">Hitbox Url</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.hitboxUrl}</dd>
            <dt>
              <span id="beamUrl">
                <Translate contentKey="webApp.botDiscordUserProfile.beamUrl">Beam Url</Translate>
              </span>
            </dt>
            <dd>{discordUserProfileEntity.beamUrl}</dd>
          </dl>
          <Button tag={Link} to="/entity/discord-user-profile" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/discord-user-profile/${discordUserProfileEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ discordUserProfile }: IRootState) => ({
  discordUserProfileEntity: discordUserProfile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscordUserProfileDetail);
