import { IPage } from 'app/shared/model/blog/page.model';

export interface IRootPage {
  id?: number;
  title?: string;
  slug?: string;
  childPages?: IPage;
}

export const defaultValue: Readonly<IRootPage> = {};
