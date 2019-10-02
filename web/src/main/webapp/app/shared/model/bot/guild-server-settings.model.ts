import { IAutoModeration } from 'app/shared/model/bot/auto-moderation.model';
import { IPunishment } from 'app/shared/model/bot/punishment.model';

export interface IGuildServerSettings {
  id?: number;
  prefix?: string;
  timezone?: string;
  raidModeEnabled?: boolean;
  raidModeReason?: string;
  maxStrikes?: number;
  acceptingApplications?: boolean;
  autoModConfig?: IAutoModeration;
  punishmentConfig?: IPunishment;
}

export const defaultValue: Readonly<IGuildServerSettings> = {
  raidModeEnabled: false,
  acceptingApplications: false
};
