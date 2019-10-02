import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './discord-user-profile.reducer';
import { IDiscordUserProfile } from 'app/shared/model/bot/discord-user-profile.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDiscordUserProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class DiscordUserProfile extends React.Component<IDiscordUserProfileProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { discordUserProfileList, match } = this.props;
    return (
      <div>
        <h2 id="discord-user-profile-heading">
          <Translate contentKey="webApp.botDiscordUserProfile.home.title">Discord User Profiles</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botDiscordUserProfile.home.createLabel">Create a new Discord User Profile</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {discordUserProfileList && discordUserProfileList.length > 0 ? (
            <Table responsive aria-describedby="discord-user-profile-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.favoriteGame">Favorite Game</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.profilePhoto">Profile Photo</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.twitterUrl">Twitter Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.twitchUrl">Twitch Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.youtubeUrl">Youtube Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.facebookUrl">Facebook Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.hitboxUrl">Hitbox Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botDiscordUserProfile.beamUrl">Beam Url</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {discordUserProfileList.map((discordUserProfile, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${discordUserProfile.id}`} color="link" size="sm">
                        {discordUserProfile.id}
                      </Button>
                    </td>
                    <td>{discordUserProfile.favoriteGame}</td>
                    <td>{discordUserProfile.profilePhoto}</td>
                    <td>{discordUserProfile.twitterUrl}</td>
                    <td>{discordUserProfile.twitchUrl}</td>
                    <td>{discordUserProfile.youtubeUrl}</td>
                    <td>{discordUserProfile.facebookUrl}</td>
                    <td>{discordUserProfile.hitboxUrl}</td>
                    <td>{discordUserProfile.beamUrl}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${discordUserProfile.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${discordUserProfile.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${discordUserProfile.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="webApp.botDiscordUserProfile.home.notFound">No Discord User Profiles found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ discordUserProfile }: IRootState) => ({
  discordUserProfileList: discordUserProfile.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DiscordUserProfile);
