import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './mute.reducer';
import { IMute } from 'app/shared/model/bot/mute.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IMuteProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Mute extends React.Component<IMuteProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { muteList, match } = this.props;
    return (
      <div>
        <h2 id="mute-heading">
          <Translate contentKey="webApp.botMute.home.title">Mutes</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botMute.home.createLabel">Create a new Mute</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {muteList && muteList.length > 0 ? (
            <Table responsive aria-describedby="mute-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botMute.reason">Reason</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botMute.endTime">End Time</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botMute.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botMute.userId">User Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botMute.mutedUser">Muted User</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botMute.mutedGuildServer">Muted Guild Server</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {muteList.map((mute, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${mute.id}`} color="link" size="sm">
                        {mute.id}
                      </Button>
                    </td>
                    <td>{mute.reason}</td>
                    <td>{mute.endTime}</td>
                    <td>{mute.guildId}</td>
                    <td>{mute.userId}</td>
                    <td>{mute.mutedUser ? <Link to={`discord-user/${mute.mutedUser.id}`}>{mute.mutedUser.id}</Link> : ''}</td>
                    <td>
                      {mute.mutedGuildServer ? <Link to={`guild-server/${mute.mutedGuildServer.id}`}>{mute.mutedGuildServer.id}</Link> : ''}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${mute.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${mute.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${mute.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botMute.home.notFound">No Mutes found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ mute }: IRootState) => ({
  muteList: mute.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Mute);
