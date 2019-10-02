import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IAutoModIgnore } from 'app/shared/model/bot/auto-mod-ignore.model';
import { getEntities as getAutoModIgnores } from 'app/entities/bot/auto-mod-ignore/auto-mod-ignore.reducer';
import { IAutoModMentions } from 'app/shared/model/bot/auto-mod-mentions.model';
import { getEntities as getAutoModMentions } from 'app/entities/bot/auto-mod-mentions/auto-mod-mentions.reducer';
import { IAutoModAntiDup } from 'app/shared/model/bot/auto-mod-anti-dup.model';
import { getEntities as getAutoModAntiDups } from 'app/entities/bot/auto-mod-anti-dup/auto-mod-anti-dup.reducer';
import { IAutoModAutoRaid } from 'app/shared/model/bot/auto-mod-auto-raid.model';
import { getEntities as getAutoModAutoRaids } from 'app/entities/bot/auto-mod-auto-raid/auto-mod-auto-raid.reducer';
import { getEntity, updateEntity, createEntity, reset } from './auto-moderation.reducer';
import { IAutoModeration } from 'app/shared/model/bot/auto-moderation.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAutoModerationUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAutoModerationUpdateState {
  isNew: boolean;
  ignoreConfigId: string;
  mentionConfigId: string;
  antiDupConfigId: string;
  autoRaidConfigId: string;
}

export class AutoModerationUpdate extends React.Component<IAutoModerationUpdateProps, IAutoModerationUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      ignoreConfigId: '0',
      mentionConfigId: '0',
      antiDupConfigId: '0',
      autoRaidConfigId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getAutoModIgnores();
    this.props.getAutoModMentions();
    this.props.getAutoModAntiDups();
    this.props.getAutoModAutoRaids();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { autoModerationEntity } = this.props;
      const entity = {
        ...autoModerationEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/auto-moderation');
  };

  render() {
    const { autoModerationEntity, autoModIgnores, autoModMentions, autoModAntiDups, autoModAutoRaids, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botAutoModeration.home.createOrEditLabel">
              <Translate contentKey="webApp.botAutoModeration.home.createOrEditLabel">Create or edit a AutoModeration</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : autoModerationEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="auto-moderation-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="auto-moderation-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="inviteStrikesLabel" for="auto-moderation-inviteStrikes">
                    <Translate contentKey="webApp.botAutoModeration.inviteStrikes">Invite Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-moderation-inviteStrikes"
                    type="string"
                    className="form-control"
                    name="inviteStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="copyPastaStrikesLabel" for="auto-moderation-copyPastaStrikes">
                    <Translate contentKey="webApp.botAutoModeration.copyPastaStrikes">Copy Pasta Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-moderation-copyPastaStrikes"
                    type="string"
                    className="form-control"
                    name="copyPastaStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="everyoneMentionStrikesLabel" for="auto-moderation-everyoneMentionStrikes">
                    <Translate contentKey="webApp.botAutoModeration.everyoneMentionStrikes">Everyone Mention Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-moderation-everyoneMentionStrikes"
                    type="string"
                    className="form-control"
                    name="everyoneMentionStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="referralStrikesLabel" for="auto-moderation-referralStrikes">
                    <Translate contentKey="webApp.botAutoModeration.referralStrikes">Referral Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-moderation-referralStrikes"
                    type="string"
                    className="form-control"
                    name="referralStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="duplicateStrikesLabel" for="auto-moderation-duplicateStrikes">
                    <Translate contentKey="webApp.botAutoModeration.duplicateStrikes">Duplicate Strikes</Translate>
                  </Label>
                  <AvField
                    id="auto-moderation-duplicateStrikes"
                    type="string"
                    className="form-control"
                    name="duplicateStrikes"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="resolveUrlsLabel" check>
                    <AvInput id="auto-moderation-resolveUrls" type="checkbox" className="form-control" name="resolveUrls" />
                    <Translate contentKey="webApp.botAutoModeration.resolveUrls">Resolve Urls</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label id="enabledLabel" check>
                    <AvInput id="auto-moderation-enabled" type="checkbox" className="form-control" name="enabled" />
                    <Translate contentKey="webApp.botAutoModeration.enabled">Enabled</Translate>
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="auto-moderation-ignoreConfig">
                    <Translate contentKey="webApp.botAutoModeration.ignoreConfig">Ignore Config</Translate>
                  </Label>
                  <AvInput id="auto-moderation-ignoreConfig" type="select" className="form-control" name="ignoreConfig.id">
                    <option value="" key="0" />
                    {autoModIgnores
                      ? autoModIgnores.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="auto-moderation-mentionConfig">
                    <Translate contentKey="webApp.botAutoModeration.mentionConfig">Mention Config</Translate>
                  </Label>
                  <AvInput id="auto-moderation-mentionConfig" type="select" className="form-control" name="mentionConfig.id">
                    <option value="" key="0" />
                    {autoModMentions
                      ? autoModMentions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="auto-moderation-antiDupConfig">
                    <Translate contentKey="webApp.botAutoModeration.antiDupConfig">Anti Dup Config</Translate>
                  </Label>
                  <AvInput id="auto-moderation-antiDupConfig" type="select" className="form-control" name="antiDupConfig.id">
                    <option value="" key="0" />
                    {autoModAntiDups
                      ? autoModAntiDups.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="auto-moderation-autoRaidConfig">
                    <Translate contentKey="webApp.botAutoModeration.autoRaidConfig">Auto Raid Config</Translate>
                  </Label>
                  <AvInput id="auto-moderation-autoRaidConfig" type="select" className="form-control" name="autoRaidConfig.id">
                    <option value="" key="0" />
                    {autoModAutoRaids
                      ? autoModAutoRaids.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/auto-moderation" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  autoModIgnores: storeState.autoModIgnore.entities,
  autoModMentions: storeState.autoModMentions.entities,
  autoModAntiDups: storeState.autoModAntiDup.entities,
  autoModAutoRaids: storeState.autoModAutoRaid.entities,
  autoModerationEntity: storeState.autoModeration.entity,
  loading: storeState.autoModeration.loading,
  updating: storeState.autoModeration.updating,
  updateSuccess: storeState.autoModeration.updateSuccess
});

const mapDispatchToProps = {
  getAutoModIgnores,
  getAutoModMentions,
  getAutoModAntiDups,
  getAutoModAutoRaids,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AutoModerationUpdate);
