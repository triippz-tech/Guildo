export interface IAutoModMentions {
  id?: number;
  maxMentions?: number;
  maxMsgLines?: number;
  maxRoleMentions?: number;
}

export const defaultValue: Readonly<IAutoModMentions> = {};
