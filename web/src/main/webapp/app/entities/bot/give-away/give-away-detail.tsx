import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './give-away.reducer';
import { IGiveAway } from 'app/shared/model/bot/give-away.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGiveAwayDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GiveAwayDetail extends React.Component<IGiveAwayDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { giveAwayEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGiveAway.detail.title">GiveAway</Translate> [<b>{giveAwayEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="webApp.botGiveAway.name">Name</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.name}</dd>
            <dt>
              <span id="image">
                <Translate contentKey="webApp.botGiveAway.image">Image</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.image}</dd>
            <dt>
              <span id="message">
                <Translate contentKey="webApp.botGiveAway.message">Message</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.message}</dd>
            <dt>
              <span id="messageId">
                <Translate contentKey="webApp.botGiveAway.messageId">Message Id</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.messageId}</dd>
            <dt>
              <span id="textChannelId">
                <Translate contentKey="webApp.botGiveAway.textChannelId">Text Channel Id</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.textChannelId}</dd>
            <dt>
              <span id="finish">
                <Translate contentKey="webApp.botGiveAway.finish">Finish</Translate>
              </span>
            </dt>
            <dd>
              <TextFormat value={giveAwayEntity.finish} type="date" format={APP_DATE_FORMAT} />
            </dd>
            <dt>
              <span id="expired">
                <Translate contentKey="webApp.botGiveAway.expired">Expired</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.expired ? 'true' : 'false'}</dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botGiveAway.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{giveAwayEntity.guildId}</dd>
            <dt>
              <Translate contentKey="webApp.botGiveAway.winner">Winner</Translate>
            </dt>
            <dd>{giveAwayEntity.winner ? giveAwayEntity.winner.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGiveAway.guildGiveAway">Guild Give Away</Translate>
            </dt>
            <dd>{giveAwayEntity.guildGiveAway ? giveAwayEntity.guildGiveAway.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/give-away" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/give-away/${giveAwayEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ giveAway }: IRootState) => ({
  giveAwayEntity: giveAway.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GiveAwayDetail);
