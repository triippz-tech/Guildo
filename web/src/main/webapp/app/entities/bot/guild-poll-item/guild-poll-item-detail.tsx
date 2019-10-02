import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-poll-item.reducer';
import { IGuildPollItem } from 'app/shared/model/bot/guild-poll-item.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildPollItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildPollItemDetail extends React.Component<IGuildPollItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildPollItemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildPollItem.detail.title">GuildPollItem</Translate> [<b>{guildPollItemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="itemName">
                <Translate contentKey="webApp.botGuildPollItem.itemName">Item Name</Translate>
              </span>
            </dt>
            <dd>{guildPollItemEntity.itemName}</dd>
            <dt>
              <span id="votes">
                <Translate contentKey="webApp.botGuildPollItem.votes">Votes</Translate>
              </span>
            </dt>
            <dd>{guildPollItemEntity.votes}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-poll-item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-poll-item/${guildPollItemEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildPollItem }: IRootState) => ({
  guildPollItemEntity: guildPollItem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildPollItemDetail);
