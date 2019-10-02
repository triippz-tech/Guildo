import { Moment } from 'moment';

export interface IPage {
  id?: number;
  title?: string;
  slug?: string;
  published?: Moment;
  edited?: Moment;
  body?: any;
}

export const defaultValue: Readonly<IPage> = {};
