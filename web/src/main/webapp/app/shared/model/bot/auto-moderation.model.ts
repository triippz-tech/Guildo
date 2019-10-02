import { IAutoModIgnore } from 'app/shared/model/bot/auto-mod-ignore.model';
import { IAutoModMentions } from 'app/shared/model/bot/auto-mod-mentions.model';
import { IAutoModAntiDup } from 'app/shared/model/bot/auto-mod-anti-dup.model';
import { IAutoModAutoRaid } from 'app/shared/model/bot/auto-mod-auto-raid.model';

export interface IAutoModeration {
  id?: number;
  inviteStrikes?: number;
  copyPastaStrikes?: number;
  everyoneMentionStrikes?: number;
  referralStrikes?: number;
  duplicateStrikes?: number;
  resolveUrls?: boolean;
  enabled?: boolean;
  ignoreConfig?: IAutoModIgnore;
  mentionConfig?: IAutoModMentions;
  antiDupConfig?: IAutoModAntiDup;
  autoRaidConfig?: IAutoModAutoRaid;
}

export const defaultValue: Readonly<IAutoModeration> = {
  resolveUrls: false,
  enabled: false
};
