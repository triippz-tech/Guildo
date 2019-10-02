import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-application-form.reducer';
import { IGuildApplicationForm } from 'app/shared/model/bot/guild-application-form.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildApplicationFormDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildApplicationFormDetail extends React.Component<IGuildApplicationFormDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildApplicationFormEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildApplicationForm.detail.title">GuildApplicationForm</Translate> [
            <b>{guildApplicationFormEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="applicationForm">
                <Translate contentKey="webApp.botGuildApplicationForm.applicationForm">Application Form</Translate>
              </span>
            </dt>
            <dd>
              {guildApplicationFormEntity.applicationForm ? (
                <div>
                  <a onClick={openFile(guildApplicationFormEntity.applicationFormContentType, guildApplicationFormEntity.applicationForm)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                  <span>
                    {guildApplicationFormEntity.applicationFormContentType}, {byteSize(guildApplicationFormEntity.applicationForm)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="guildId">
                <Translate contentKey="webApp.botGuildApplicationForm.guildId">Guild Id</Translate>
              </span>
            </dt>
            <dd>{guildApplicationFormEntity.guildId}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-application-form" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-application-form/${guildApplicationFormEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildApplicationForm }: IRootState) => ({
  guildApplicationFormEntity: guildApplicationForm.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildApplicationFormDetail);
