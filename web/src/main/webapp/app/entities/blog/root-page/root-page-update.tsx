import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IPage } from 'app/shared/model/blog/page.model';
import { getEntities as getPages } from 'app/entities/blog/page/page.reducer';
import { getEntity, updateEntity, createEntity, reset } from './root-page.reducer';
import { IRootPage } from 'app/shared/model/blog/root-page.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRootPageUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRootPageUpdateState {
  isNew: boolean;
  childPagesId: string;
}

export class RootPageUpdate extends React.Component<IRootPageUpdateProps, IRootPageUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      childPagesId: '0',
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

    this.props.getPages();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { rootPageEntity } = this.props;
      const entity = {
        ...rootPageEntity,
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
    this.props.history.push('/entity/root-page');
  };

  render() {
    const { rootPageEntity, pages, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.blogRootPage.home.createOrEditLabel">
              <Translate contentKey="webApp.blogRootPage.home.createOrEditLabel">Create or edit a RootPage</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : rootPageEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="root-page-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="root-page-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="titleLabel" for="root-page-title">
                    <Translate contentKey="webApp.blogRootPage.title">Title</Translate>
                  </Label>
                  <AvField
                    id="root-page-title"
                    type="text"
                    name="title"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 10, errorMessage: translate('entity.validation.maxlength', { max: 10 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="slugLabel" for="root-page-slug">
                    <Translate contentKey="webApp.blogRootPage.slug">Slug</Translate>
                  </Label>
                  <AvField
                    id="root-page-slug"
                    type="text"
                    name="slug"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label for="root-page-childPages">
                    <Translate contentKey="webApp.blogRootPage.childPages">Child Pages</Translate>
                  </Label>
                  <AvInput id="root-page-childPages" type="select" className="form-control" name="childPages.id">
                    <option value="" key="0" />
                    {pages
                      ? pages.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/root-page" replace color="info">
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
  pages: storeState.page.entities,
  rootPageEntity: storeState.rootPage.entity,
  loading: storeState.rootPage.loading,
  updating: storeState.rootPage.updating,
  updateSuccess: storeState.rootPage.updateSuccess
});

const mapDispatchToProps = {
  getPages,
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
)(RootPageUpdate);
