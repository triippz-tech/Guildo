import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './auto-mod-auto-raid.reducer';
import { IAutoModAutoRaid } from 'app/shared/model/bot/auto-mod-auto-raid.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModAutoRaidProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class AutoModAutoRaid extends React.Component<IAutoModAutoRaidProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { autoModAutoRaidList, match } = this.props;
    return (
      <div>
        <h2 id="auto-mod-auto-raid-heading">
          <Translate contentKey="webApp.botAutoModAutoRaid.home.title">Auto Mod Auto Raids</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botAutoModAutoRaid.home.createLabel">Create a new Auto Mod Auto Raid</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {autoModAutoRaidList && autoModAutoRaidList.length > 0 ? (
            <Table responsive aria-describedby="auto-mod-auto-raid-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModAutoRaid.autoRaidEnabled">Auto Raid Enabled</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModAutoRaid.autoRaidTimeThreshold">Auto Raid Time Threshold</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {autoModAutoRaidList.map((autoModAutoRaid, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${autoModAutoRaid.id}`} color="link" size="sm">
                        {autoModAutoRaid.id}
                      </Button>
                    </td>
                    <td>{autoModAutoRaid.autoRaidEnabled ? 'true' : 'false'}</td>
                    <td>{autoModAutoRaid.autoRaidTimeThreshold}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${autoModAutoRaid.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${autoModAutoRaid.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${autoModAutoRaid.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botAutoModAutoRaid.home.notFound">No Auto Mod Auto Raids found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ autoModAutoRaid }: IRootState) => ({
  autoModAutoRaidList: autoModAutoRaid.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModAutoRaid);
