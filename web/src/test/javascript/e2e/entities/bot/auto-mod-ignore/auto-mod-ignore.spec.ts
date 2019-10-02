import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import AutoModIgnoreComponentsPage, { AutoModIgnoreDeleteDialog } from './auto-mod-ignore.page-object';
import AutoModIgnoreUpdatePage from './auto-mod-ignore-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('AutoModIgnore e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let autoModIgnoreUpdatePage: AutoModIgnoreUpdatePage;
  let autoModIgnoreComponentsPage: AutoModIgnoreComponentsPage;
  let autoModIgnoreDeleteDialog: AutoModIgnoreDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load AutoModIgnores', async () => {
    await navBarPage.getEntityPage('auto-mod-ignore');
    autoModIgnoreComponentsPage = new AutoModIgnoreComponentsPage();
    expect(await autoModIgnoreComponentsPage.getTitle().getText()).to.match(/Auto Mod Ignores/);
  });

  it('should load create AutoModIgnore page', async () => {
    await autoModIgnoreComponentsPage.clickOnCreateButton();
    autoModIgnoreUpdatePage = new AutoModIgnoreUpdatePage();
    expect(await autoModIgnoreUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botAutoModIgnore.home.createOrEditLabel/);
    await autoModIgnoreUpdatePage.cancel();
  });

  it('should create and save AutoModIgnores', async () => {
    async function createAutoModIgnore() {
      await autoModIgnoreComponentsPage.clickOnCreateButton();
      await autoModIgnoreUpdatePage.setRoleIdInput('5');
      expect(await autoModIgnoreUpdatePage.getRoleIdInput()).to.eq('5');
      await autoModIgnoreUpdatePage.setChannelIdInput('5');
      expect(await autoModIgnoreUpdatePage.getChannelIdInput()).to.eq('5');
      await waitUntilDisplayed(autoModIgnoreUpdatePage.getSaveButton());
      await autoModIgnoreUpdatePage.save();
      await waitUntilHidden(autoModIgnoreUpdatePage.getSaveButton());
      expect(await autoModIgnoreUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createAutoModIgnore();
    await autoModIgnoreComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await autoModIgnoreComponentsPage.countDeleteButtons();
    await createAutoModIgnore();

    await autoModIgnoreComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await autoModIgnoreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last AutoModIgnore', async () => {
    await autoModIgnoreComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await autoModIgnoreComponentsPage.countDeleteButtons();
    await autoModIgnoreComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    autoModIgnoreDeleteDialog = new AutoModIgnoreDeleteDialog();
    expect(await autoModIgnoreDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botAutoModIgnore.delete.question/);
    await autoModIgnoreDeleteDialog.clickOnConfirmButton();

    await autoModIgnoreComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await autoModIgnoreComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
