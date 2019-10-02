export interface IAutoModAntiDup {
  id?: number;
  deleteThreshold?: number;
  dupsToPunish?: number;
}

export const defaultValue: Readonly<IAutoModAntiDup> = {};
