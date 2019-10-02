import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './temp-ban.reducer';
import { ITempBan } from 'app/shared/model/bot/temp-ban.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITempBanProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class TempBan extends React.Component<ITempBanProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { tempBanList, match } = this.props;
    return (
      <div>
        <h2 id="temp-ban-heading">
          <Translate contentKey="webApp.botTempBan.home.title">Temp Bans</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botTempBan.home.createLabel">Create a new Temp Ban</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {tempBanList && tempBanList.length > 0 ? (
            <Table responsive aria-describedby="temp-ban-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botTempBan.reason">Reason</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botTempBan.endTime">End Time</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botTempBan.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botTempBan.userId">User Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botTempBan.bannedUser">Banned User</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botTempBan.tempBanGuildServer">Temp Ban Guild Server</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {tempBanList.map((tempBan, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${tempBan.id}`} color="link" size="sm">
                        {tempBan.id}
                      </Button>
                    </td>
                    <td>{tempBan.reason}</td>
                    <td>
                      <TextFormat type="date" value={tempBan.endTime} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{tempBan.guildId}</td>
                    <td>{tempBan.userId}</td>
                    <td>{tempBan.bannedUser ? <Link to={`discord-user/${tempBan.bannedUser.id}`}>{tempBan.bannedUser.id}</Link> : ''}</td>
                    <td>
                      {tempBan.tempBanGuildServer ? (
                        <Link to={`guild-server/${tempBan.tempBanGuildServer.id}`}>{tempBan.tempBanGuildServer.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${tempBan.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${tempBan.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${tempBan.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botTempBan.home.notFound">No Temp Bans found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ tempBan }: IRootState) => ({
  tempBanList: tempBan.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(TempBan);
