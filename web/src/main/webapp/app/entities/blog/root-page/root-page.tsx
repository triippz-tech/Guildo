import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './root-page.reducer';
import { IRootPage } from 'app/shared/model/blog/root-page.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRootPageProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class RootPage extends React.Component<IRootPageProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { rootPageList, match } = this.props;
    return (
      <div>
        <h2 id="root-page-heading">
          <Translate contentKey="webApp.blogRootPage.home.title">Root Pages</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="webApp.blogRootPage.home.createLabel">Create a new Root Page</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {rootPageList && rootPageList.length > 0 ? (
            <Table responsive aria-describedby="root-page-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.blogRootPage.title">Title</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.blogRootPage.slug">Slug</Translate>
                  </th>
                  <th>
                    <Translate contentKey="webApp.blogRootPage.childPages">Child Pages</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {rootPageList.map((rootPage, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${rootPage.id}`} color="link" size="sm">
                        {rootPage.id}
                      </Button>
                    </td>
                    <td>{rootPage.title}</td>
                    <td>{rootPage.slug}</td>
                    <td>{rootPage.childPages ? <Link to={`page/${rootPage.childPages.id}`}>{rootPage.childPages.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${rootPage.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${rootPage.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${rootPage.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="webApp.blogRootPage.home.notFound">No Root Pages found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ rootPage }: IRootState) => ({
  rootPageList: rootPage.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RootPage);
