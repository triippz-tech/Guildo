import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './guild-server-strike.reducer';
import { IGuildServerStrike } from 'app/shared/model/bot/guild-server-strike.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IGuildServerStrikeProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IGuildServerStrikeState = IPaginationBaseState;

export class GuildServerStrike extends React.Component<IGuildServerStrikeProps, IGuildServerStrikeState> {
  state: IGuildServerStrikeState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.reset();
  }

  componentDidUpdate() {
    if (this.props.updateSuccess) {
      this.reset();
    }
  }

  reset = () => {
    this.props.reset();
    this.setState({ activePage: 1 }, () => {
      this.getEntities();
    });
  };

  handleLoadMore = () => {
    if (window.pageYOffset > 0) {
      this.setState({ activePage: this.state.activePage + 1 }, () => this.getEntities());
    }
  };

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => {
        this.reset();
      }
    );
  };

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { guildServerStrikeList, match } = this.props;
    return (
      <div>
        <h2 id="guild-server-strike-heading">
          <Translate contentKey="webApp.botGuildServerStrike.home.title">Guild Server Strikes</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildServerStrike.home.createLabel">Create a new Guild Server Strike</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          <InfiniteScroll
            pageStart={this.state.activePage}
            loadMore={this.handleLoadMore}
            hasMore={this.state.activePage - 1 < this.props.links.next}
            loader={<div className="loader">Loading ...</div>}
            threshold={0}
            initialLoad={false}
          >
            {guildServerStrikeList && guildServerStrikeList.length > 0 ? (
              <Table responsive aria-describedby="guild-server-strike-heading">
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('count')}>
                      <Translate contentKey="webApp.botGuildServerStrike.count">Count</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('userId')}>
                      <Translate contentKey="webApp.botGuildServerStrike.userId">User Id</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('guildId')}>
                      <Translate contentKey="webApp.botGuildServerStrike.guildId">Guild Id</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="webApp.botGuildServerStrike.discordUser">Discord User</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {guildServerStrikeList.map((guildServerStrike, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${guildServerStrike.id}`} color="link" size="sm">
                          {guildServerStrike.id}
                        </Button>
                      </td>
                      <td>{guildServerStrike.count}</td>
                      <td>{guildServerStrike.userId}</td>
                      <td>{guildServerStrike.guildId}</td>
                      <td>
                        {guildServerStrike.discordUser ? (
                          <Link to={`discord-user/${guildServerStrike.discordUser.id}`}>{guildServerStrike.discordUser.userName}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${guildServerStrike.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.view">View</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${guildServerStrike.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.edit">Edit</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${guildServerStrike.id}/delete`} color="danger" size="sm">
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
                <Translate contentKey="webApp.botGuildServerStrike.home.notFound">No Guild Server Strikes found</Translate>
              </div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildServerStrike }: IRootState) => ({
  guildServerStrikeList: guildServerStrike.entities,
  totalItems: guildServerStrike.totalItems,
  links: guildServerStrike.links,
  entity: guildServerStrike.entity,
  updateSuccess: guildServerStrike.updateSuccess
});

const mapDispatchToProps = {
  getEntities,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerStrike);
