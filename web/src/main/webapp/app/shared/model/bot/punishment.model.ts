import { Moment } from 'moment';
import { PunishmentType } from 'app/shared/model/enumerations/punishment-type.model';

export interface IPunishment {
  id?: number;
  maxStrikes?: number;
  action?: PunishmentType;
  punishmentDuration?: Moment;
  guildId?: number;
}

export const defaultValue: Readonly<IPunishment> = {};
