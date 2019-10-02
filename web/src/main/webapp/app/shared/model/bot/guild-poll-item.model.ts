export interface IGuildPollItem {
  id?: number;
  itemName?: string;
  votes?: number;
}

export const defaultValue: Readonly<IGuildPollItem> = {};
