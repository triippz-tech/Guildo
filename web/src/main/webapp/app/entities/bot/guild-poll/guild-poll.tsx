import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-poll.reducer';
import { IGuildPoll } from 'app/shared/model/bot/guild-poll.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildPollProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildPoll extends React.Component<IGuildPollProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildPollList, match } = this.props;
    return (
      <div>
        <h2 id="guild-poll-heading">
          <Translate contentKey="webApp.botGuildPoll.home.title">Guild Polls</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildPoll.home.createLabel">Create a new Guild Poll</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {guildPollList && guildPollList.length > 0 ? (
            <Table responsive aria-describedby="guild-poll-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.pollName">Poll Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.description">Description</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.textChannelId">Text Channel Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.finishTime">Finish Time</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.completed">Completed</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.pollItems">Poll Items</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPoll.pollServer">Poll Server</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {guildPollList.map((guildPoll, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${guildPoll.id}`} color="link" size="sm">
                        {guildPoll.id}
                      </Button>
                    </td>
                    <td>{guildPoll.pollName}</td>
                    <td>{guildPoll.description}</td>
                    <td>{guildPoll.textChannelId}</td>
                    <td>
                      <TextFormat type="date" value={guildPoll.finishTime} format={APP_LOCAL_DATE_FORMAT} />
                    </td>
                    <td>{guildPoll.completed ? 'true' : 'false'}</td>
                    <td>{guildPoll.guildId}</td>
                    <td>
                      {guildPoll.pollItems ? <Link to={`guild-poll-item/${guildPoll.pollItems.id}`}>{guildPoll.pollItems.id}</Link> : ''}
                    </td>
                    <td>
                      {guildPoll.pollServer ? <Link to={`guild-server/${guildPoll.pollServer.id}`}>{guildPoll.pollServer.id}</Link> : ''}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${guildPoll.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildPoll.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildPoll.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botGuildPoll.home.notFound">No Guild Polls found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildPoll }: IRootState) => ({
  guildPollList: guildPoll.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildPoll);
