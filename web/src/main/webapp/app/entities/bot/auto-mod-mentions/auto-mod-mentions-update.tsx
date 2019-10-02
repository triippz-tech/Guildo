import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './auto-mod-mentions.reducer';
import { IAutoModMentions } from 'app/shared/model/bot/auto-mod-mentions.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAutoModMentionsUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAutoModMentionsUpdateState {
  isNew: boolean;
}

export class AutoModMentionsUpdate extends React.Component<IAutoModMentionsUpdateProps, IAutoModMentionsUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { autoModMentionsEntity } = this.props;
      const entity = {
        ...autoModMentionsEntity,
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
    this.props.history.push('/entity/auto-mod-mentions');
  };

  render() {
    const { autoModMentionsEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.botAutoModMentions.home.createOrEditLabel">
              <Translate contentKey="webApp.botAutoModMentions.home.createOrEditLabel">Create or edit a AutoModMentions</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : autoModMentionsEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="auto-mod-mentions-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="auto-mod-mentions-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="maxMentionsLabel" for="auto-mod-mentions-maxMentions">
                    <Translate contentKey="webApp.botAutoModMentions.maxMentions">Max Mentions</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-mentions-maxMentions"
                    type="string"
                    className="form-control"
                    name="maxMentions"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="maxMsgLinesLabel" for="auto-mod-mentions-maxMsgLines">
                    <Translate contentKey="webApp.botAutoModMentions.maxMsgLines">Max Msg Lines</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-mentions-maxMsgLines"
                    type="string"
                    className="form-control"
                    name="maxMsgLines"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="maxRoleMentionsLabel" for="auto-mod-mentions-maxRoleMentions">
                    <Translate contentKey="webApp.botAutoModMentions.maxRoleMentions">Max Role Mentions</Translate>
                  </Label>
                  <AvField
                    id="auto-mod-mentions-maxRoleMentions"
                    type="string"
                    className="form-control"
                    name="maxRoleMentions"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      number: { value: true, errorMessage: translate('entity.validation.number') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/auto-mod-mentions" replace color="info">
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
  autoModMentionsEntity: storeState.autoModMentions.entity,
  loading: storeState.autoModMentions.loading,
  updating: storeState.autoModMentions.updating,
  updateSuccess: storeState.autoModMentions.updateSuccess
});

const mapDispatchToProps = {
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
)(AutoModMentionsUpdate);
