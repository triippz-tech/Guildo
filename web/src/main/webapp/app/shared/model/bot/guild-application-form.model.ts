export interface IGuildApplicationForm {
  id?: number;
  applicationFormContentType?: string;
  applicationForm?: any;
  guildId?: number;
}

export const defaultValue: Readonly<IGuildApplicationForm> = {};
