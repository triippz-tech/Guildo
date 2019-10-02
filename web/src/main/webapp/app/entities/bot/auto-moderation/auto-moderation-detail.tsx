import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './auto-moderation.reducer';
import { IAutoModeration } from 'app/shared/model/bot/auto-moderation.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAutoModerationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AutoModerationDetail extends React.Component<IAutoModerationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { autoModerationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botAutoModeration.detail.title">AutoModeration</Translate> [<b>{autoModerationEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="inviteStrikes">
                <Translate contentKey="webApp.botAutoModeration.inviteStrikes">Invite Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModerationEntity.inviteStrikes}</dd>
            <dt>
              <span id="copyPastaStrikes">
                <Translate contentKey="webApp.botAutoModeration.copyPastaStrikes">Copy Pasta Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModerationEntity.copyPastaStrikes}</dd>
            <dt>
              <span id="everyoneMentionStrikes">
                <Translate contentKey="webApp.botAutoModeration.everyoneMentionStrikes">Everyone Mention Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModerationEntity.everyoneMentionStrikes}</dd>
            <dt>
              <span id="referralStrikes">
                <Translate contentKey="webApp.botAutoModeration.referralStrikes">Referral Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModerationEntity.referralStrikes}</dd>
            <dt>
              <span id="duplicateStrikes">
                <Translate contentKey="webApp.botAutoModeration.duplicateStrikes">Duplicate Strikes</Translate>
              </span>
            </dt>
            <dd>{autoModerationEntity.duplicateStrikes}</dd>
            <dt>
              <span id="resolveUrls">
                <Translate contentKey="webApp.botAutoModeration.resolveUrls">Resolve Urls</Translate>
              </span>
            </dt>
            <dd>{autoModerationEntity.resolveUrls ? 'true' : 'false'}</dd>
            <dt>
              <span id="enabled">
                <Translate contentKey="webApp.botAutoModeration.enabled">Enabled</Translate>
              </span>
            </dt>
            <dd>{autoModerationEntity.enabled ? 'true' : 'false'}</dd>
            <dt>
              <Translate contentKey="webApp.botAutoModeration.ignoreConfig">Ignore Config</Translate>
            </dt>
            <dd>{autoModerationEntity.ignoreConfig ? autoModerationEntity.ignoreConfig.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botAutoModeration.mentionConfig">Mention Config</Translate>
            </dt>
            <dd>{autoModerationEntity.mentionConfig ? autoModerationEntity.mentionConfig.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botAutoModeration.antiDupConfig">Anti Dup Config</Translate>
            </dt>
            <dd>{autoModerationEntity.antiDupConfig ? autoModerationEntity.antiDupConfig.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botAutoModeration.autoRaidConfig">Auto Raid Config</Translate>
            </dt>
            <dd>{autoModerationEntity.autoRaidConfig ? autoModerationEntity.autoRaidConfig.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/auto-moderation" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/auto-moderation/${autoModerationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ autoModeration }: IRootState) => ({
  autoModerationEntity: autoModeration.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModerationDetail);
