import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-server-profile.reducer';
import { IGuildServerProfile } from 'app/shared/model/bot/guild-server-profile.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildServerProfileProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildServerProfile extends React.Component<IGuildServerProfileProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildServerProfileList, match } = this.props;
    return (
      <div>
        <h2 id="guild-server-profile-heading">
          <Translate contentKey="webApp.botGuildServerProfile.home.title">Guild Server Profiles</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildServerProfile.home.createLabel">Create a new Guild Server Profile</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {guildServerProfileList && guildServerProfileList.length > 0 ? (
            <Table responsive aria-describedby="guild-server-profile-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerProfile.guildType">Guild Type</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerProfile.playStyle">Play Style</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerProfile.description">Description</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerProfile.website">Website</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildServerProfile.discordUrl">Discord Url</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {guildServerProfileList.map((guildServerProfile, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${guildServerProfile.id}`} color="link" size="sm">
                        {guildServerProfile.id}
                      </Button>
                    </td>
                    <td>
                      <Translate contentKey={`webApp.GuildType.${guildServerProfile.guildType}`} />
                    </td>
                    <td>
                      <Translate contentKey={`webApp.GuildPlayStyle.${guildServerProfile.playStyle}`} />
                    </td>
                    <td>{guildServerProfile.description}</td>
                    <td>{guildServerProfile.website}</td>
                    <td>{guildServerProfile.discordUrl}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${guildServerProfile.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildServerProfile.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildServerProfile.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botGuildServerProfile.home.notFound">No Guild Server Profiles found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildServerProfile }: IRootState) => ({
  guildServerProfileList: guildServerProfile.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerProfile);
