import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import AutoModAntiDupComponentsPage, { AutoModAntiDupDeleteDialog } from './auto-mod-anti-dup.page-object';
import AutoModAntiDupUpdatePage from './auto-mod-anti-dup-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('AutoModAntiDup e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let autoModAntiDupUpdatePage: AutoModAntiDupUpdatePage;
  let autoModAntiDupComponentsPage: AutoModAntiDupComponentsPage;
  let autoModAntiDupDeleteDialog: AutoModAntiDupDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load AutoModAntiDups', async () => {
    await navBarPage.getEntityPage('auto-mod-anti-dup');
    autoModAntiDupComponentsPage = new AutoModAntiDupComponentsPage();
    expect(await autoModAntiDupComponentsPage.getTitle().getText()).to.match(/Auto Mod Anti Dups/);
  });

  it('should load create AutoModAntiDup page', async () => {
    await autoModAntiDupComponentsPage.clickOnCreateButton();
    autoModAntiDupUpdatePage = new AutoModAntiDupUpdatePage();
    expect(await autoModAntiDupUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botAutoModAntiDup.home.createOrEditLabel/);
    await autoModAntiDupUpdatePage.cancel();
  });

  it('should create and save AutoModAntiDups', async () => {
    async function createAutoModAntiDup() {
      await autoModAntiDupComponentsPage.clickOnCreateButton();
      await autoModAntiDupUpdatePage.setDeleteThresholdInput('5');
      expect(await autoModAntiDupUpdatePage.getDeleteThresholdInput()).to.eq('5');
      await autoModAntiDupUpdatePage.setDupsToPunishInput('5');
      expect(await autoModAntiDupUpdatePage.getDupsToPunishInput()).to.eq('5');
      await waitUntilDisplayed(autoModAntiDupUpdatePage.getSaveButton());
      await autoModAntiDupUpdatePage.save();
      await waitUntilHidden(autoModAntiDupUpdatePage.getSaveButton());
      expect(await autoModAntiDupUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createAutoModAntiDup();
    await autoModAntiDupComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await autoModAntiDupComponentsPage.countDeleteButtons();
    await createAutoModAntiDup();

    await autoModAntiDupComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await autoModAntiDupComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last AutoModAntiDup', async () => {
    await autoModAntiDupComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await autoModAntiDupComponentsPage.countDeleteButtons();
    await autoModAntiDupComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    autoModAntiDupDeleteDialog = new AutoModAntiDupDeleteDialog();
    expect(await autoModAntiDupDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botAutoModAntiDup.delete.question/);
    await autoModAntiDupDeleteDialog.clickOnConfirmButton();

    await autoModAntiDupComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await autoModAntiDupComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
