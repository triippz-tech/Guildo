import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-poll-item.reducer';
import { IGuildPollItem } from 'app/shared/model/bot/guild-poll-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildPollItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildPollItem extends React.Component<IGuildPollItemProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildPollItemList, match } = this.props;
    return (
      <div>
        <h2 id="guild-poll-item-heading">
          <Translate contentKey="webApp.botGuildPollItem.home.title">Guild Poll Items</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildPollItem.home.createLabel">Create a new Guild Poll Item</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {guildPollItemList && guildPollItemList.length > 0 ? (
            <Table responsive aria-describedby="guild-poll-item-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPollItem.itemName">Item Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildPollItem.votes">Votes</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {guildPollItemList.map((guildPollItem, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${guildPollItem.id}`} color="link" size="sm">
                        {guildPollItem.id}
                      </Button>
                    </td>
                    <td>{guildPollItem.itemName}</td>
                    <td>{guildPollItem.votes}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${guildPollItem.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildPollItem.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildPollItem.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botGuildPollItem.home.notFound">No Guild Poll Items found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildPollItem }: IRootState) => ({
  guildPollItemList: guildPollItem.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildPollItem);
