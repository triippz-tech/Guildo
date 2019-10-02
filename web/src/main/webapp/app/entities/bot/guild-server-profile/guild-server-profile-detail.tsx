import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './guild-server-profile.reducer';
import { IGuildServerProfile } from 'app/shared/model/bot/guild-server-profile.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGuildServerProfileDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class GuildServerProfileDetail extends React.Component<IGuildServerProfileDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { guildServerProfileEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            <Translate contentKey="webApp.botGuildServerProfile.detail.title">GuildServerProfile</Translate> [
            <b>{guildServerProfileEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="guildType">
                <Translate contentKey="webApp.botGuildServerProfile.guildType">Guild Type</Translate>
              </span>
            </dt>
            <dd>{guildServerProfileEntity.guildType}</dd>
            <dt>
              <span id="playStyle">
                <Translate contentKey="webApp.botGuildServerProfile.playStyle">Play Style</Translate>
              </span>
            </dt>
            <dd>{guildServerProfileEntity.playStyle}</dd>
            <dt>
              <span id="description">
                <Translate contentKey="webApp.botGuildServerProfile.description">Description</Translate>
              </span>
            </dt>
            <dd>{guildServerProfileEntity.description}</dd>
            <dt>
              <span id="website">
                <Translate contentKey="webApp.botGuildServerProfile.website">Website</Translate>
              </span>
            </dt>
            <dd>{guildServerProfileEntity.website}</dd>
            <dt>
              <span id="discordUrl">
                <Translate contentKey="webApp.botGuildServerProfile.discordUrl">Discord Url</Translate>
              </span>
            </dt>
            <dd>{guildServerProfileEntity.discordUrl}</dd>
          </dl>
          <Button tag={Link} to="/entity/guild-server-profile" replace color="info">
            <FontAwesomeIcon icon="arrow-left" />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/guild-server-profile/${guildServerProfileEntity.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ guildServerProfile }: IRootState) => ({
  guildServerProfileEntity: guildServerProfile.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(GuildServerProfileDetail);
