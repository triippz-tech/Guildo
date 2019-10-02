import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './guild-server.reducer';
import { IGuildServer } from 'app/shared/model/bot/guild-server.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IGuildServerProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IGuildServerState = IPaginationBaseState;

export class GuildServer extends React.Component<IGuildServerProps, IGuildServerState> {
  state: IGuildServerState = {
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
    const { guildServerList, match } = this.props;
    return (
      <div>
        <h2 id="guild-server-heading">
          <Translate contentKey="webApp.botGuildServer.home.title">Guild Servers</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildServer.home.createLabel">Create a new Guild Server</Translate>
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
            {guildServerList && guildServerList.length > 0 ? (
              <Table responsive aria-describedby="guild-server-heading">
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('guildId')}>
                      <Translate contentKey="webApp.botGuildServer.guildId">Guild Id</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('guildName')}>
                      <Translate contentKey="webApp.botGuildServer.guildName">Guild Name</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('icon')}>
                      <Translate contentKey="webApp.botGuildServer.icon">Icon</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('owner')}>
                      <Translate contentKey="webApp.botGuildServer.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('serverLevel')}>
                      <Translate contentKey="webApp.botGuildServer.serverLevel">Server Level</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="webApp.botGuildServer.guildProfile">Guild Profile</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="webApp.botGuildServer.applicationForm">Application Form</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="webApp.botGuildServer.guildSettings">Guild Settings</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="webApp.botGuildServer.welcomeMessage">Welcome Message</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {guildServerList.map((guildServer, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${guildServer.id}`} color="link" size="sm">
                          {guildServer.id}
                        </Button>
                      </td>
                      <td>{guildServer.guildId}</td>
                      <td>{guildServer.guildName}</td>
                      <td>{guildServer.icon}</td>
                      <td>{guildServer.owner}</td>
                      <td>
                        <Translate contentKey={`webApp.GuildServerLevel.${guildServer.serverLevel}`} />
                      </td>
                      <td>
                        {guildServer.guildProfile ? (
                          <Link to={`guild-server-profile/${guildServer.guildProfile.id}`}>{guildServer.guildProfile.id}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td>
                        {guildServer.applicationForm ? (
                          <Link to={`guild-application-form/${guildServer.applicationForm.id}`}>{guildServer.applicationForm.id}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td>
                        {guildServer.guildSettings ? (
                          <Link to={`guild-server-settings/${guildServer.guildSettings.id}`}>{guildServer.guildSettings.id}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td>
                        {guildServer.welcomeMessage ? (
                          <Link to={`welcome-message/${guildServer.welcomeMessage.id}`}>{guildServer.welcomeMessage.id}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${guildServer.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.view">View</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${guildServer.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.edit">Edit</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${guildServer.id}/delete`} color="danger" size="sm">
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
                <Translate contentKey="webApp.botGuildServer.home.notFound">No Guild Servers found</Translate>
              </div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildServer }: IRootState) => ({
  guildServerList: guildServer.entities,
  totalItems: guildServer.totalItems,
  links: guildServer.links,
  entity: guildServer.entity,
  updateSuccess: guildServer.updateSuccess
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
)(GuildServer);
