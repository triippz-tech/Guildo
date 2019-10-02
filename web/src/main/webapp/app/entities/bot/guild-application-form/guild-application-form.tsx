import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { openFile, byteSize, Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './guild-application-form.reducer';
import { IGuildApplicationForm } from 'app/shared/model/bot/guild-application-form.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildApplicationFormProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class GuildApplicationForm extends React.Component<IGuildApplicationFormProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { guildApplicationFormList, match } = this.props;
    return (
      <div>
        <h2 id="guild-application-form-heading">
          <Translate contentKey="webApp.botGuildApplicationForm.home.title">Guild Application Forms</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botGuildApplicationForm.home.createLabel">Create a new Guild Application Form</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {guildApplicationFormList && guildApplicationFormList.length > 0 ? (
            <Table responsive aria-describedby="guild-application-form-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplicationForm.applicationForm">Application Form</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botGuildApplicationForm.guildId">Guild Id</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {guildApplicationFormList.map((guildApplicationForm, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${guildApplicationForm.id}`} color="link" size="sm">
                        {guildApplicationForm.id}
                      </Button>
                    </td>
                    <td>
                      {guildApplicationForm.applicationForm ? (
                        <div>
                          <a onClick={openFile(guildApplicationForm.applicationFormContentType, guildApplicationForm.applicationForm)}>
                            <Translate contentKey="entity.action.open">Open</Translate>
                            &nbsp;
                          </a>
                          <span>
                            {guildApplicationForm.applicationFormContentType}, {byteSize(guildApplicationForm.applicationForm)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>{guildApplicationForm.guildId}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${guildApplicationForm.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildApplicationForm.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${guildApplicationForm.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botGuildApplicationForm.home.notFound">No Guild Application Forms found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ guildApplicationForm }: IRootState) => ({
  guildApplicationFormList: guildApplicationForm.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildApplicationForm);
