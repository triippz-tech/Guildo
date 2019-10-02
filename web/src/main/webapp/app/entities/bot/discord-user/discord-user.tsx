import React from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, getSortState, IPaginationBaseState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities, reset } from './discord-user.reducer';
import { IDiscordUser } from 'app/shared/model/bot/discord-user.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IDiscordUserProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IDiscordUserState = IPaginationBaseState;

export class DiscordUser extends React.Component<IDiscordUserProps, IDiscordUserState> {
  state: IDiscordUserState = {
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
    const { discordUserList, match } = this.props;
    return (
      <div>
        <h2 id="discord-user-heading">
          <Translate contentKey="webApp.botDiscordUser.home.title">Discord Users</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botDiscordUser.home.createLabel">Create a new Discord User</Translate>
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
            {discordUserList && discordUserList.length > 0 ? (
              <Table responsive aria-describedby="discord-user-heading">
                <thead>
                  <tr>
                    <th className="hand" onClick={this.sort('id')}>
                      <Translate contentKey="global.field.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('userId')}>
                      <Translate contentKey="webApp.botDiscordUser.userId">User Id</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('userName')}>
                      <Translate contentKey="webApp.botDiscordUser.userName">User Name</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('icon')}>
                      <Translate contentKey="webApp.botDiscordUser.icon">Icon</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('commandsIssued')}>
                      <Translate contentKey="webApp.botDiscordUser.commandsIssued">Commands Issued</Translate>{' '}
                      <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('blacklisted')}>
                      <Translate contentKey="webApp.botDiscordUser.blacklisted">Blacklisted</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th className="hand" onClick={this.sort('userLevel')}>
                      <Translate contentKey="webApp.botDiscordUser.userLevel">User Level</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th>
                      <Translate contentKey="webApp.botDiscordUser.userProfile">User Profile</Translate> <FontAwesomeIcon icon="sort" />
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {discordUserList.map((discordUser, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${discordUser.id}`} color="link" size="sm">
                          {discordUser.id}
                        </Button>
                      </td>
                      <td>{discordUser.userId}</td>
                      <td>{discordUser.userName}</td>
                      <td>{discordUser.icon}</td>
                      <td>{discordUser.commandsIssued}</td>
                      <td>{discordUser.blacklisted ? 'true' : 'false'}</td>
                      <td>
                        <Translate contentKey={`webApp.DiscordUserLevel.${discordUser.userLevel}`} />
                      </td>
                      <td>
                        {discordUser.userProfile ? (
                          <Link to={`discord-user-profile/${discordUser.userProfile.id}`}>{discordUser.userProfile.id}</Link>
                        ) : (
                          ''
                        )}
                      </td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${discordUser.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.view">View</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${discordUser.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                            <span className="d-none d-md-inline">
                              <Translate contentKey="entity.action.edit">Edit</Translate>
                            </span>
                          </Button>
                          <Button tag={Link} to={`${match.url}/${discordUser.id}/delete`} color="danger" size="sm">
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
                <Translate contentKey="webApp.botDiscordUser.home.notFound">No Discord Users found</Translate>
              </div>
            )}
          </InfiniteScroll>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ discordUser }: IRootState) => ({
  discordUserList: discordUser.entities,
  totalItems: discordUser.totalItems,
  links: discordUser.links,
  entity: discordUser.entity,
  updateSuccess: discordUser.updateSuccess
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
)(DiscordUser);
