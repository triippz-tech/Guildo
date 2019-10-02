import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './welcome-message.reducer';
import { IWelcomeMessage } from 'app/shared/model/bot/welcome-message.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IWelcomeMessageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class WelcomeMessageDetail extends React.Component<IWelcomeMessageDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { welcomeMessageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botWelcomeMessage.detail.title">WelcomeMessage</Translate> [<b>{welcomeMessageEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="webApp.botWelcomeMessage.name">Name</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.name}</dd>
            <dt>
              <span id="messageTitle">
                <Translate contentKey="webApp.botWelcomeMessage.messageTitle">Message Title</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.messageTitle}</dd>
            <dt>
              <span id="body">
                <Translate contentKey="webApp.botWelcomeMessage.body">Body</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.body}</dd>
            <dt>
              <span id="footer">
                <Translate contentKey="webApp.botWelcomeMessage.footer">Footer</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.footer}</dd>
            <dt>
              <span id="logoUrl">
                <Translate contentKey="webApp.botWelcomeMessage.logoUrl">Logo Url</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.logoUrl}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botWelcomeMessage.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{welcomeMessageEntity.guildId}</dd>
          </dl>
          <Button tag={Link} to="/entity/welcome-message" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/welcome-message/${welcomeMessageEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ welcomeMessage }: IRootState) => ({
  welcomeMessageEntity: welcomeMessage.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(WelcomeMessageDetail);
