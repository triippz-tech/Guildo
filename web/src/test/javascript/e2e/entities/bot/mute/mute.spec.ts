import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import MuteComponentsPage, { MuteDeleteDialog } from './mute.page-object';
import MuteUpdatePage from './mute-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('Mute e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let muteUpdatePage: MuteUpdatePage;
  let muteComponentsPage: MuteComponentsPage;
  let muteDeleteDialog: MuteDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Mutes', async () => {
    await navBarPage.getEntityPage('mute');
    muteComponentsPage = new MuteComponentsPage();
    expect(await muteComponentsPage.getTitle().getText()).to.match(/Mutes/);
  });

  it('should load create Mute page', async () => {
    await muteComponentsPage.clickOnCreateButton();
    muteUpdatePage = new MuteUpdatePage();
    expect(await muteUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botMute.home.createOrEditLabel/);
    await muteUpdatePage.cancel();
  });

  it('should create and save Mutes', async () => {
    async function createMute() {
      await muteComponentsPage.clickOnCreateButton();
      await muteUpdatePage.setReasonInput('reason');
      expect(await muteUpdatePage.getReasonInput()).to.match(/reason/);
      await muteUpdatePage.setEndTimeInput('endTime');
      expect(await muteUpdatePage.getEndTimeInput()).to.match(/endTime/);
      await muteUpdatePage.setGuildIdInput('5');
      expect(await muteUpdatePage.getGuildIdInput()).to.eq('5');
      await muteUpdatePage.setUserIdInput('5');
      expect(await muteUpdatePage.getUserIdInput()).to.eq('5');
      await muteUpdatePage.mutedUserSelectLastOption();
      await muteUpdatePage.mutedGuildServerSelectLastOption();
      await waitUntilDisplayed(muteUpdatePage.getSaveButton());
      await muteUpdatePage.save();
      await waitUntilHidden(muteUpdatePage.getSaveButton());
      expect(await muteUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createMute();
    await muteComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await muteComponentsPage.countDeleteButtons();
    await createMute();

    await muteComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await muteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Mute', async () => {
    await muteComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await muteComponentsPage.countDeleteButtons();
    await muteComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    muteDeleteDialog = new MuteDeleteDialog();
    expect(await muteDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botMute.delete.question/);
    await muteDeleteDialog.clickOnConfirmButton();

    await muteComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await muteComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
