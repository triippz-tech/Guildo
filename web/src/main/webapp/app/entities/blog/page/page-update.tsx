import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './page.reducer';
import { IPage } from 'app/shared/model/blog/page.model';
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPageUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IPageUpdateState {
  isNew: boolean;
}

export class PageUpdate extends React.Component<IPageUpdateProps, IPageUpdateState> {
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

  onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => this.props.setBlob(name, data, contentType), isAnImage);
  };

  clearBlob = name => () => {
    this.props.setBlob(name, undefined, undefined);
  };

  saveEntity = (event, errors, values) => {
    values.published = convertDateTimeToServer(values.published);
    values.edited = convertDateTimeToServer(values.edited);

    if (errors.length === 0) {
      const { pageEntity } = this.props;
      const entity = {
        ...pageEntity,
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
    this.props.history.push('/entity/page');
  };

  render() {
    const { pageEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    const { body } = pageEntity;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="webApp.blogPage.home.createOrEditLabel">
              <Translate contentKey="webApp.blogPage.home.createOrEditLabel">Create or edit a Page</Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : pageEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="page-id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput id="page-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="titleLabel" for="page-title">
                    <Translate contentKey="webApp.blogPage.title">Title</Translate>
                  </Label>
                  <AvField
                    id="page-title"
                    type="text"
                    name="title"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="slugLabel" for="page-slug">
                    <Translate contentKey="webApp.blogPage.slug">Slug</Translate>
                  </Label>
                  <AvField
                    id="page-slug"
                    type="text"
                    name="slug"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') },
                      maxLength: { value: 50, errorMessage: translate('entity.validation.maxlength', { max: 50 }) }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="publishedLabel" for="page-published">
                    <Translate contentKey="webApp.blogPage.published">Published</Translate>
                  </Label>
                  <AvInput
                    id="page-published"
                    type="datetime-local"
                    className="form-control"
                    name="published"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.pageEntity.published)}
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="editedLabel" for="page-edited">
                    <Translate contentKey="webApp.blogPage.edited">Edited</Translate>
                  </Label>
                  <AvInput
                    id="page-edited"
                    type="datetime-local"
                    className="form-control"
                    name="edited"
                    placeholder={'YYYY-MM-DD HH:mm'}
                    value={isNew ? null : convertDateTimeFromServer(this.props.pageEntity.edited)}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="bodyLabel" for="page-body">
                    <Translate contentKey="webApp.blogPage.body">Body</Translate>
                  </Label>
                  <AvInput
                    id="page-body"
                    type="textarea"
                    name="body"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/page" replace color="info">
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
  pageEntity: storeState.page.entity,
  loading: storeState.page.loading,
  updating: storeState.page.updating,
  updateSuccess: storeState.page.updateSuccess
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(PageUpdate);
