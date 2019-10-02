import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './welcome-message.reducer';
import { IWelcomeMessage } from 'app/shared/model/bot/welcome-message.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IWelcomeMessageProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class WelcomeMessage extends React.Component<IWelcomeMessageProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { welcomeMessageList, match } = this.props;
    return (
      <div>
        <h2 id="welcome-message-heading">
          <Translate contentKey="webApp.botWelcomeMessage.home.title">Welcome Messages</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.botWelcomeMessage.home.createLabel">Create a new Welcome Message</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {welcomeMessageList && welcomeMessageList.length > 0 ? (
            <Table responsive aria-describedby="welcome-message-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botWelcomeMessage.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botWelcomeMessage.messageTitle">Message Title</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botWelcomeMessage.body">Body</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botWelcomeMessage.footer">Footer</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botWelcomeMessage.logoUrl">Logo Url</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.botWelcomeMessage.guildId">Guild Id</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {welcomeMessageList.map((welcomeMessage, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${welcomeMessage.id}`} color="link" size="sm">
                        {welcomeMessage.id}
                      </Button>
                    </td>
                    <td>{welcomeMessage.name}</td>
                    <td>{welcomeMessage.messageTitle}</td>
                    <td>{welcomeMessage.body}</td>
                    <td>{welcomeMessage.footer}</td>
                    <td>{welcomeMessage.logoUrl}</td>
                    <td>{welcomeMessage.guildId}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${welcomeMessage.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${welcomeMessage.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${welcomeMessage.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="webApp.botWelcomeMessage.home.notFound">No Welcome Messages found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ welcomeMessage }: IRootState) => ({
  welcomeMessageList: welcomeMessage.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WelcomeMessage);
