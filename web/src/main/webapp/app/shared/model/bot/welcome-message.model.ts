export interface IWelcomeMessage {
  id?: number;
  name?: string;
  messageTitle?: string;
  body?: string;
  footer?: string;
  logoUrl?: string;
  guildId?: number;
}

export const defaultValue: Readonly<IWelcomeMessage> = {};
