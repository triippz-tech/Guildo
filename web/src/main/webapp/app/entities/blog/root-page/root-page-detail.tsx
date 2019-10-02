import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './root-page.reducer';
import { IRootPage } from 'app/shared/model/blog/root-page.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRootPageDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RootPageDetail extends React.Component<IRootPageDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { rootPageEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.blogRootPage.detail.title">RootPage</Translate> [<b>{rootPageEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="title">
                <Translate contentKey="webApp.blogRootPage.title">Title</Translate>
              </span>
            </dt>
            <dd>{rootPageEntity.title}</dd>
            <dt>
              <span id="slug">
                <Translate contentKey="webApp.blogRootPage.slug">Slug</Translate>
              </span>
            </dt>
            <dd>{rootPageEntity.slug}</dd>
            <dt>
              <Translate contentKey="webApp.blogRootPage.childPages">Child Pages</Translate>
            </dt>
            <dd>{rootPageEntity.childPages ? rootPageEntity.childPages.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/root-page" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/root-page/${rootPageEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ rootPage }: IRootState) => ({
  rootPageEntity: rootPage.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RootPageDetail);
