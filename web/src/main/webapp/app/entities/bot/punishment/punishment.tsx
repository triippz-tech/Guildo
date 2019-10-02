import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './punishment.reducer';
import { IPunishment } from 'app/shared/model/bot/punishment.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPunishmentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Punishment extends React.Component<IPunishmentProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { punishmentList, match } = this.props;
    return (
      <div>
        <h2 id="punishment-heading">
          <Translate contentKey="webApp.botPunishment.home.title">Punishments</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botPunishment.home.createLabel">Create a new Punishment</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {punishmentList && punishmentList.length > 0 ? (
            <Table responsive aria-describedby="punishment-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botPunishment.maxStrikes">Max Strikes</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botPunishment.action">Action</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botPunishment.punishmentDuration">Punishment Duration</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botPunishment.guildId">Guild Id</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {punishmentList.map((punishment, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${punishment.id}`} color="link" size="sm">
                        {punishment.id}
                      </Button>
                    </td>
                    <td>{punishment.maxStrikes}</td>
                    <td>
                      <Translate contentKey={`webApp.PunishmentType.${punishment.action}`} />
                    </td>
                    <td>
                      <TextFormat type="date" value={punishment.punishmentDuration} format={APP_DATE_FORMAT} />
                    </td>
                    <td>{punishment.guildId}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${punishment.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${punishment.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${punishment.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botPunishment.home.notFound">No Punishments found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ punishment }: IRootState) => ({
  punishmentList: punishment.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Punishment);
