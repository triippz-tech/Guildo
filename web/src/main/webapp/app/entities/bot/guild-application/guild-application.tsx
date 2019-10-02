import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { openFile, byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-application.reducer';
import { IGuildApplication } from 'app/shared/model/bot/guild-application.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildApplicationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildApplication extends React.Component<IGuildApplicationProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildApplicationList, match } = this.props;
    return (
      <div>
        <h2 id="guild-application-heading">
          <Translate contentKey="webApp.botGuildApplication.home.title">Guild Applications</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildApplication.home.createLabel">Create a new Guild Application</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {guildApplicationList && guildApplicationList.length > 0 ? (
            <Table responsive aria-describedby="guild-application-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplication.characterName">Character Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplication.characterType">Character Type</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplication.applicationFile">Application File</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplication.status">Status</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplication.acceptedBy">Accepted By</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplication.appliedUser">Applied User</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplication.guildServer">Guild Server</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {guildApplicationList.map((guildApplication, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${guildApplication.id}`} color="link" size="sm">
                        {guildApplication.id}
                      </Button>
                    </td>
                    <td>{guildApplication.characterName}</td>
                    <td>{guildApplication.characterType}</td>
                    <td>
                      {guildApplication.applicationFile ? (
                        <div>
                          <a onClick={openFile(guildApplication.applicationFileContentType, guildApplication.applicationFile)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                          <span>
                            {guildApplication.applicationFileContentType}, {byteSize(guildApplication.applicationFile)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>
                      <Translate contentKey={`webApp.ApplicationStatus.${guildApplication.status}`} />
                    </td>
                    <td>
                      {guildApplication.acceptedBy ? (
                        <Link to={`discord-user/${guildApplication.acceptedBy.id}`}>{guildApplication.acceptedBy.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {guildApplication.appliedUser ? (
                        <Link to={`discord-user/${guildApplication.appliedUser.id}`}>{guildApplication.appliedUser.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {guildApplication.guildServer ? (
                        <Link to={`guild-server/${guildApplication.guildServer.id}`}>{guildApplication.guildServer.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${guildApplication.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildApplication.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildApplication.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botGuildApplication.home.notFound">No Guild Applications found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildApplication }: IRootState) => ({
  guildApplicationList: guildApplication.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildApplication);
