import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-application.reducer';
import { IGuildApplication } from 'app/shared/model/bot/guild-application.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildApplicationDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildApplicationDetail extends React.Component<IGuildApplicationDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildApplicationEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildApplication.detail.title">GuildApplication</Translate> [<b>{guildApplicationEntity.id}</b>
            ]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="characterName">
                <Translate contentKey="webApp.botGuildApplication.characterName">Character Name</Translate>
              </span>
            </dt>
            <dd>{guildApplicationEntity.characterName}</dd>
            <dt>
              <span id="characterType">
                <Translate contentKey="webApp.botGuildApplication.characterType">Character Type</Translate>
              </span>
            </dt>
            <dd>{guildApplicationEntity.characterType}</dd>
            <dt>
              <span id="applicationFile">
                <Translate contentKey="webApp.botGuildApplication.applicationFile">Application File</Translate>
              </span>
            </dt>
            <dd>
              {guildApplicationEntity.applicationFile ? (
                <div>
                  <a onClick={openFile(guildApplicationEntity.applicationFileContentType, guildApplicationEntity.applicationFile)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                  <span>
                    {guildApplicationEntity.applicationFileContentType}, {byteSize(guildApplicationEntity.applicationFile)}
                  </span>
                </div>
              ) : null}
            </dd>
            <dt>
              <span id="status">
                <Translate contentKey="webApp.botGuildApplication.status">Status</Translate>
              </span>
            </dt>
            <dd>{guildApplicationEntity.status}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildApplication.acceptedBy">Accepted By</Translate>
            </dt>
            <dd>{guildApplicationEntity.acceptedBy ? guildApplicationEntity.acceptedBy.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildApplication.appliedUser">Applied User</Translate>
            </dt>
            <dd>{guildApplicationEntity.appliedUser ? guildApplicationEntity.appliedUser.id : ''}</dd>
            <dt>
              <Translate contentKey="webApp.botGuildApplication.guildServer">Guild Server</Translate>
            </dt>
            <dd>{guildApplicationEntity.guildServer ? guildApplicationEntity.guildServer.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-application" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-application/${guildApplicationEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildApplication }: IRootState) => ({
  guildApplicationEntity: guildApplication.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildApplicationDetail);
