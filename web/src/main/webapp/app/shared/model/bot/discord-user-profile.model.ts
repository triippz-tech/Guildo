export interface IDiscordUserProfile {
  id?: number;
  favoriteGame?: string;
  profilePhoto?: string;
  twitterUrl?: string;
  twitchUrl?: string;
  youtubeUrl?: string;
  facebookUrl?: string;
  hitboxUrl?: string;
  beamUrl?: string;
}

export const defaultValue: Readonly<IDiscordUserProfile> = {};
