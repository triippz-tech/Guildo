import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-event.reducer';
import { IGuildEvent } from 'app/shared/model/bot/guild-event.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildEventProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildEvent extends React.Component<IGuildEventProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildEventList, match } = this.props;
    return (
      <div>
        <h2 id="guild-event-heading">
          <Translate contentKey="webApp.botGuildEvent.home.title">Guild Events</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildEvent.home.createLabel">Create a new Guild Event</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {guildEventList && guildEventList.length > 0 ? (
            <Table responsive aria-describedby="guild-event-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildEvent.eventName">Event Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildEvent.eventImageUrl">Event Image Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildEvent.eventMessage">Event Message</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildEvent.eventStart">Event Start</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildEvent.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildEvent.eventGuild">Event Guild</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {guildEventList.map((guildEvent, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${guildEvent.id}`} color="link" size="sm">
                        {guildEvent.id}
                      </Button>
                    </td>
                    <td>{guildEvent.eventName}</td>
                    <td>{guildEvent.eventImageUrl}</td>
                    <td>{guildEvent.eventMessage}</td>
                    <td>
                      <TextFormat type="date" value={guildEvent.eventStart} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{guildEvent.guildId}</td>
                    <td>
                      {guildEvent.eventGuild ? <Link to={`guild-server/${guildEvent.eventGuild.id}`}>{guildEvent.eventGuild.id}</Link> : ''}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${guildEvent.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildEvent.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildEvent.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botGuildEvent.home.notFound">No Guild Events found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildEvent }: IRootState) => ({
  guildEventList: guildEvent.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildEvent);
