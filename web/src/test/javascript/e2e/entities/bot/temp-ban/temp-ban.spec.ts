import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import TempBanComponentsPage, { TempBanDeleteDialog } from './temp-ban.page-object';
import TempBanUpdatePage from './temp-ban-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('TempBan e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let tempBanUpdatePage: TempBanUpdatePage;
  let tempBanComponentsPage: TempBanComponentsPage;
  let tempBanDeleteDialog: TempBanDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load TempBans', async () => {
    await navBarPage.getEntityPage('temp-ban');
    tempBanComponentsPage = new TempBanComponentsPage();
    expect(await tempBanComponentsPage.getTitle().getText()).to.match(/Temp Bans/);
  });

  it('should load create TempBan page', async () => {
    await tempBanComponentsPage.clickOnCreateButton();
    tempBanUpdatePage = new TempBanUpdatePage();
    expect(await tempBanUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botTempBan.home.createOrEditLabel/);
    await tempBanUpdatePage.cancel();
  });

  it('should create and save TempBans', async () => {
    async function createTempBan() {
      await tempBanComponentsPage.clickOnCreateButton();
      await tempBanUpdatePage.setReasonInput('reason');
      expect(await tempBanUpdatePage.getReasonInput()).to.match(/reason/);
      await tempBanUpdatePage.setEndTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await tempBanUpdatePage.getEndTimeInput()).to.contain('2001-01-01T02:30');
      await tempBanUpdatePage.setGuildIdInput('5');
      expect(await tempBanUpdatePage.getGuildIdInput()).to.eq('5');
      await tempBanUpdatePage.setUserIdInput('5');
      expect(await tempBanUpdatePage.getUserIdInput()).to.eq('5');
      await tempBanUpdatePage.bannedUserSelectLastOption();
      await tempBanUpdatePage.tempBanGuildServerSelectLastOption();
      await waitUntilDisplayed(tempBanUpdatePage.getSaveButton());
      await tempBanUpdatePage.save();
      await waitUntilHidden(tempBanUpdatePage.getSaveButton());
      expect(await tempBanUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createTempBan();
    await tempBanComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await tempBanComponentsPage.countDeleteButtons();
    await createTempBan();

    await tempBanComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await tempBanComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last TempBan', async () => {
    await tempBanComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await tempBanComponentsPage.countDeleteButtons();
    await tempBanComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    tempBanDeleteDialog = new TempBanDeleteDialog();
    expect(await tempBanDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botTempBan.delete.question/);
    await tempBanDeleteDialog.clickOnConfirmButton();

    await tempBanComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await tempBanComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
