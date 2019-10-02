import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './auto-mod-mentions.reducer';
import { IAutoModMentions } from 'app/shared/model/bot/auto-mod-mentions.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModMentionsDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AutoModMentionsDetail extends React.Component<IAutoModMentionsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { autoModMentionsEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botAutoModMentions.detail.title">AutoModMentions</Translate> [<b>{autoModMentionsEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="maxMentions">
                <Translate contentKey="webApp.botAutoModMentions.maxMentions">Max Mentions</Translate>
              </span>
            </dt>
            <dd>{autoModMentionsEntity.maxMentions}</dd>
            <dt>
              <span id="maxMsgLines">
                <Translate contentKey="webApp.botAutoModMentions.maxMsgLines">Max Msg Lines</Translate>
              </span>
            </dt>
            <dd>{autoModMentionsEntity.maxMsgLines}</dd>
            <dt>
              <span id="maxRoleMentions">
                <Translate contentKey="webApp.botAutoModMentions.maxRoleMentions">Max Role Mentions</Translate>
              </span>
            </dt>
            <dd>{autoModMentionsEntity.maxRoleMentions}</dd>
          </dl>
          <Button tag={Link} to="/entity/auto-mod-mentions" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/auto-mod-mentions/${autoModMentionsEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ autoModMentions }: IRootState) => ({
  autoModMentionsEntity: autoModMentions.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModMentionsDetail);
