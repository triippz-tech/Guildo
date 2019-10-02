import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { byteSize, Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './give-away.reducer';
import { IGiveAway } from 'app/shared/model/bot/give-away.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGiveAwayProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GiveAway extends React.Component<IGiveAwayProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { giveAwayList, match } = this.props;
    return (
      <div>
        <h2 id="give-away-heading">
          <Translate contentKey="webApp.botGiveAway.home.title">Give Aways</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGiveAway.home.createLabel">Create a new Give Away</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {giveAwayList && giveAwayList.length > 0 ? (
            <Table responsive aria-describedby="give-away-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.image">Image</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.message">Message</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.messageId">Message Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.textChannelId">Text Channel Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.finish">Finish</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.expired">Expired</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.guildId">Guild Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.winner">Winner</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGiveAway.guildGiveAway">Guild Give Away</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {giveAwayList.map((giveAway, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${giveAway.id}`} color="link" size="sm">
                        {giveAway.id}
                      </Button>
                    </td>
                    <td>{giveAway.name}</td>
                    <td>{giveAway.image}</td>
                    <td>{giveAway.message}</td>
                    <td>{giveAway.messageId}</td>
                    <td>{giveAway.textChannelId}</td>
                    <td>
                      <TextFormat type="date" value={giveAway.finish} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{giveAway.expired ? 'true' : 'false'}</td>
                    <td>{giveAway.guildId}</td>
                    <td>{giveAway.winner ? <Link to={`discord-user/${giveAway.winner.id}`}>{giveAway.winner.id}</Link> : ''}</td>
                    <td>
                      {giveAway.guildGiveAway ? (
                        <Link to={`guild-server/${giveAway.guildGiveAway.id}`}>{giveAway.guildGiveAway.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${giveAway.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${giveAway.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${giveAway.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botGiveAway.home.notFound">No Give Aways found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ giveAway }: IRootState) => ({
  giveAwayList: giveAway.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GiveAway);
