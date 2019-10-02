import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import RootPageComponentsPage, { RootPageDeleteDialog } from './root-page.page-object';
import RootPageUpdatePage from './root-page-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('RootPage e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let rootPageUpdatePage: RootPageUpdatePage;
  let rootPageComponentsPage: RootPageComponentsPage;
  let rootPageDeleteDialog: RootPageDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load RootPages', async () => {
    await navBarPage.getEntityPage('root-page');
    rootPageComponentsPage = new RootPageComponentsPage();
    expect(await rootPageComponentsPage.getTitle().getText()).to.match(/Root Pages/);
  });

  it('should load create RootPage page', async () => {
    await rootPageComponentsPage.clickOnCreateButton();
    rootPageUpdatePage = new RootPageUpdatePage();
    expect(await rootPageUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.blogRootPage.home.createOrEditLabel/);
    await rootPageUpdatePage.cancel();
  });

  it('should create and save RootPages', async () => {
    async function createRootPage() {
      await rootPageComponentsPage.clickOnCreateButton();
      await rootPageUpdatePage.setTitleInput('title');
      expect(await rootPageUpdatePage.getTitleInput()).to.match(/title/);
      await rootPageUpdatePage.setSlugInput('slug');
      expect(await rootPageUpdatePage.getSlugInput()).to.match(/slug/);
      await rootPageUpdatePage.childPagesSelectLastOption();
      await waitUntilDisplayed(rootPageUpdatePage.getSaveButton());
      await rootPageUpdatePage.save();
      await waitUntilHidden(rootPageUpdatePage.getSaveButton());
      expect(await rootPageUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createRootPage();
    await rootPageComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await rootPageComponentsPage.countDeleteButtons();
    await createRootPage();

    await rootPageComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await rootPageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last RootPage', async () => {
    await rootPageComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await rootPageComponentsPage.countDeleteButtons();
    await rootPageComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    rootPageDeleteDialog = new RootPageDeleteDialog();
    expect(await rootPageDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.blogRootPage.delete.question/);
    await rootPageDeleteDialog.clickOnConfirmButton();

    await rootPageComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await rootPageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
