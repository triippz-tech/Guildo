import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './auto-mod-ignore.reducer';
import { IAutoModIgnore } from 'app/shared/model/bot/auto-mod-ignore.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModIgnoreProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class AutoModIgnore extends React.Component<IAutoModIgnoreProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { autoModIgnoreList, match } = this.props;
    return (
      <div>
        <h2 id="auto-mod-ignore-heading">
          <Translate contentKey="webApp.botAutoModIgnore.home.title">Auto Mod Ignores</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botAutoModIgnore.home.createLabel">Create a new Auto Mod Ignore</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {autoModIgnoreList && autoModIgnoreList.length > 0 ? (
            <Table responsive aria-describedby="auto-mod-ignore-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModIgnore.roleId">Role Id</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botAutoModIgnore.channelId">Channel Id</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {autoModIgnoreList.map((autoModIgnore, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${autoModIgnore.id}`} color="link" size="sm">
                        {autoModIgnore.id}
                      </Button>
                    </td>
                    <td>{autoModIgnore.roleId}</td>
                    <td>{autoModIgnore.channelId}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${autoModIgnore.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${autoModIgnore.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${autoModIgnore.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botAutoModIgnore.home.notFound">No Auto Mod Ignores found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ autoModIgnore }: IRootState) => ({
  autoModIgnoreList: autoModIgnore.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModIgnore);
