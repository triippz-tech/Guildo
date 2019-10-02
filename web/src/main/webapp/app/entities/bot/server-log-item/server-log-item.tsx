import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './server-log-item.reducer';
import { IServerLogItem } from 'app/shared/model/bot/server-log-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServerLogItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ServerLogItem extends React.Component<IServerLogItemProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { serverLogItemList, match } = this.props;
    return (
      <div>
        <h2 id="server-log-item-heading">
          <Translate contentKey="webApp.botServerLogItem.home.title">Server Log Items</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botServerLogItem.home.createLabel">Create a new Server Log Item</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {serverLogItemList && serverLogItemList.length > 0 ? (
            <Table responsive aria-describedby="server-log-item-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.activity">Activity</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.channelId">Channel Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.channelName">Channel Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.time">Time</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.userId">User Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.userName">User Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botServerLogItem.serverItemGuildServer">Server Item Guild Server</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {serverLogItemList.map((serverLogItem, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${serverLogItem.id}`} color="link" size="sm">
                        {serverLogItem.id}
                      </Button>
                    </td>
                    <td>
                      <Translate contentKey={`webApp.Activity.${serverLogItem.activity}`} />
                    </td>
                    <td>{serverLogItem.channelId}</td>
                    <td>{serverLogItem.channelName}</td>
                    <td>
                      <TextFormat type="date" value={serverLogItem.time} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{serverLogItem.userId}</td>
                    <td>{serverLogItem.userName}</td>
                    <td>{serverLogItem.guildId}</td>
                    <td>
                      {serverLogItem.serverItemGuildServer ? (
                        <Link to={`guild-server/${serverLogItem.serverItemGuildServer.id}`}>{serverLogItem.serverItemGuildServer.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${serverLogItem.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${serverLogItem.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${serverLogItem.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botServerLogItem.home.notFound">No Server Log Items found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ serverLogItem }: IRootState) => ({
  serverLogItemList: serverLogItem.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServerLogItem);
