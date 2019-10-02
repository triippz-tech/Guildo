export interface IAutoModAutoRaid {
  id?: number;
  autoRaidEnabled?: boolean;
  autoRaidTimeThreshold?: number;
}

export const defaultValue: Readonly<IAutoModAutoRaid> = {
  autoRaidEnabled: false
};
