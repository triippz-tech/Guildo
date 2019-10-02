import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './moderation-log-item.reducer';
import { IModerationLogItem } from 'app/shared/model/bot/moderation-log-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IModerationLogItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class ModerationLogItem extends React.Component<IModerationLogItemProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { moderationLogItemList, match } = this.props;
    return (
      <div>
        <h2 id="moderation-log-item-heading">
          <Translate contentKey="webApp.botModerationLogItem.home.title">Moderation Log Items</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botModerationLogItem.home.createLabel">Create a new Moderation Log Item</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {moderationLogItemList && moderationLogItemList.length > 0 ? (
            <Table responsive aria-describedby="moderation-log-item-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.channelId">Channel Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.channelName">Channel Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.issuedById">Issued By Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.issuedByName">Issued By Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.issuedToId">Issued To Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.issuedToName">Issued To Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.reason">Reason</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.time">Time</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.moderationAction">Moderation Action</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botModerationLogItem.modItemGuildServer">Mod Item Guild Server</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {moderationLogItemList.map((moderationLogItem, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${moderationLogItem.id}`} color="link" size="sm">
                        {moderationLogItem.id}
                      </Button>
                    </td>
                    <td>{moderationLogItem.channelId}</td>
                    <td>{moderationLogItem.channelName}</td>
                    <td>{moderationLogItem.issuedById}</td>
                    <td>{moderationLogItem.issuedByName}</td>
                    <td>{moderationLogItem.issuedToId}</td>
                    <td>{moderationLogItem.issuedToName}</td>
                    <td>{moderationLogItem.reason}</td>
                    <td>
                      <TextFormat type="date" value={moderationLogItem.time} format={APP_DATE_FORMAT} />
                    </td>
                    <td>
                      <Translate contentKey={`webApp.PunishmentType.${moderationLogItem.moderationAction}`} />
                    </td>
                    <td>{moderationLogItem.guildId}</td>
                    <td>
                      {moderationLogItem.modItemGuildServer ? (
                        <Link to={`guild-server/${moderationLogItem.modItemGuildServer.id}`}>
                          {moderationLogItem.modItemGuildServer.id}
                        </Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${moderationLogItem.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${moderationLogItem.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${moderationLogItem.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botModerationLogItem.home.notFound">No Moderation Log Items found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ moderationLogItem }: IRootState) => ({
  moderationLogItemList: moderationLogItem.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ModerationLogItem);
