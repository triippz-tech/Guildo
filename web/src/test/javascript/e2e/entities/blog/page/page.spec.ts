import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import PageComponentsPage, { PageDeleteDialog } from './page.page-object';
import PageUpdatePage from './page-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('Page e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let pageUpdatePage: PageUpdatePage;
  let pageComponentsPage: PageComponentsPage;
  let pageDeleteDialog: PageDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Pages', async () => {
    await navBarPage.getEntityPage('page');
    pageComponentsPage = new PageComponentsPage();
    expect(await pageComponentsPage.getTitle().getText()).to.match(/Pages/);
  });

  it('should load create Page page', async () => {
    await pageComponentsPage.clickOnCreateButton();
    pageUpdatePage = new PageUpdatePage();
    expect(await pageUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.blogPage.home.createOrEditLabel/);
    await pageUpdatePage.cancel();
  });

  it('should create and save Pages', async () => {
    async function createPage() {
      await pageComponentsPage.clickOnCreateButton();
      await pageUpdatePage.setTitleInput('title');
      expect(await pageUpdatePage.getTitleInput()).to.match(/title/);
      await pageUpdatePage.setSlugInput('slug');
      expect(await pageUpdatePage.getSlugInput()).to.match(/slug/);
      await pageUpdatePage.setPublishedInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await pageUpdatePage.getPublishedInput()).to.contain('2001-01-01T02:30');
      await pageUpdatePage.setEditedInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await pageUpdatePage.getEditedInput()).to.contain('2001-01-01T02:30');
      await pageUpdatePage.setBodyInput('body');
      expect(await pageUpdatePage.getBodyInput()).to.match(/body/);
      await waitUntilDisplayed(pageUpdatePage.getSaveButton());
      await pageUpdatePage.save();
      await waitUntilHidden(pageUpdatePage.getSaveButton());
      expect(await pageUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createPage();
    await pageComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await pageComponentsPage.countDeleteButtons();
    await createPage();

    await pageComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await pageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Page', async () => {
    await pageComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await pageComponentsPage.countDeleteButtons();
    await pageComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    pageDeleteDialog = new PageDeleteDialog();
    expect(await pageDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.blogPage.delete.question/);
    await pageDeleteDialog.clickOnConfirmButton();

    await pageComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await pageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
