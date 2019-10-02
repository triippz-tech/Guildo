import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './auto-moderation.reducer';
import { IAutoModeration } from 'app/shared/model/bot/auto-moderation.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModerationProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class AutoModeration extends React.Component<IAutoModerationProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { autoModerationList, match } = this.props;
    return (
      <div>
        <h2 id="auto-moderation-heading">
          <Translate contentKey="webApp.botAutoModeration.home.title">Auto Moderations</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botAutoModeration.home.createLabel">Create a new Auto Moderation</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {autoModerationList && autoModerationList.length > 0 ? (
            <Table responsive aria-describedby="auto-moderation-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.inviteStrikes">Invite Strikes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.copyPastaStrikes">Copy Pasta Strikes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.everyoneMentionStrikes">Everyone Mention Strikes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.referralStrikes">Referral Strikes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.duplicateStrikes">Duplicate Strikes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.resolveUrls">Resolve Urls</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.enabled">Enabled</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.ignoreConfig">Ignore Config</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.mentionConfig">Mention Config</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.antiDupConfig">Anti Dup Config</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModeration.autoRaidConfig">Auto Raid Config</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {autoModerationList.map((autoModeration, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${autoModeration.id}`} color="link" size="sm">
                        {autoModeration.id}
                      </Button>
                    </td>
                    <td>{autoModeration.inviteStrikes}</td>
                    <td>{autoModeration.copyPastaStrikes}</td>
                    <td>{autoModeration.everyoneMentionStrikes}</td>
                    <td>{autoModeration.referralStrikes}</td>
                    <td>{autoModeration.duplicateStrikes}</td>
                    <td>{autoModeration.resolveUrls ? 'true' : 'false'}</td>
                    <td>{autoModeration.enabled ? 'true' : 'false'}</td>
                    <td>
                      {autoModeration.ignoreConfig ? (
                        <Link to={`auto-mod-ignore/${autoModeration.ignoreConfig.id}`}>{autoModeration.ignoreConfig.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {autoModeration.mentionConfig ? (
                        <Link to={`auto-mod-mentions/${autoModeration.mentionConfig.id}`}>{autoModeration.mentionConfig.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {autoModeration.antiDupConfig ? (
                        <Link to={`auto-mod-anti-dup/${autoModeration.antiDupConfig.id}`}>{autoModeration.antiDupConfig.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {autoModeration.autoRaidConfig ? (
                        <Link to={`auto-mod-auto-raid/${autoModeration.autoRaidConfig.id}`}>{autoModeration.autoRaidConfig.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${autoModeration.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${autoModeration.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${autoModeration.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botAutoModeration.home.notFound">No Auto Moderations found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ autoModeration }: IRootState) => ({
  autoModerationList: autoModeration.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModeration);
