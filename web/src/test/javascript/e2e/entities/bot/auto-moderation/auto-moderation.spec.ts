import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import AutoModerationComponentsPage, { AutoModerationDeleteDialog } from './auto-moderation.page-object';
import AutoModerationUpdatePage from './auto-moderation-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('AutoModeration e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let autoModerationUpdatePage: AutoModerationUpdatePage;
  let autoModerationComponentsPage: AutoModerationComponentsPage;
  let autoModerationDeleteDialog: AutoModerationDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load AutoModerations', async () => {
    await navBarPage.getEntityPage('auto-moderation');
    autoModerationComponentsPage = new AutoModerationComponentsPage();
    expect(await autoModerationComponentsPage.getTitle().getText()).to.match(/Auto Moderations/);
  });

  it('should load create AutoModeration page', async () => {
    await autoModerationComponentsPage.clickOnCreateButton();
    autoModerationUpdatePage = new AutoModerationUpdatePage();
    expect(await autoModerationUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botAutoModeration.home.createOrEditLabel/);
    await autoModerationUpdatePage.cancel();
  });

  it('should create and save AutoModerations', async () => {
    async function createAutoModeration() {
      await autoModerationComponentsPage.clickOnCreateButton();
      await autoModerationUpdatePage.setInviteStrikesInput('5');
      expect(await autoModerationUpdatePage.getInviteStrikesInput()).to.eq('5');
      await autoModerationUpdatePage.setCopyPastaStrikesInput('5');
      expect(await autoModerationUpdatePage.getCopyPastaStrikesInput()).to.eq('5');
      await autoModerationUpdatePage.setEveryoneMentionStrikesInput('5');
      expect(await autoModerationUpdatePage.getEveryoneMentionStrikesInput()).to.eq('5');
      await autoModerationUpdatePage.setReferralStrikesInput('5');
      expect(await autoModerationUpdatePage.getReferralStrikesInput()).to.eq('5');
      await autoModerationUpdatePage.setDuplicateStrikesInput('5');
      expect(await autoModerationUpdatePage.getDuplicateStrikesInput()).to.eq('5');
      const selectedResolveUrls = await autoModerationUpdatePage.getResolveUrlsInput().isSelected();
      if (selectedResolveUrls) {
        await autoModerationUpdatePage.getResolveUrlsInput().click();
        expect(await autoModerationUpdatePage.getResolveUrlsInput().isSelected()).to.be.false;
      } else {
        await autoModerationUpdatePage.getResolveUrlsInput().click();
        expect(await autoModerationUpdatePage.getResolveUrlsInput().isSelected()).to.be.true;
      }
      const selectedEnabled = await autoModerationUpdatePage.getEnabledInput().isSelected();
      if (selectedEnabled) {
        await autoModerationUpdatePage.getEnabledInput().click();
        expect(await autoModerationUpdatePage.getEnabledInput().isSelected()).to.be.false;
      } else {
        await autoModerationUpdatePage.getEnabledInput().click();
        expect(await autoModerationUpdatePage.getEnabledInput().isSelected()).to.be.true;
      }
      await autoModerationUpdatePage.ignoreConfigSelectLastOption();
      await autoModerationUpdatePage.mentionConfigSelectLastOption();
      await autoModerationUpdatePage.antiDupConfigSelectLastOption();
      await autoModerationUpdatePage.autoRaidConfigSelectLastOption();
      await waitUntilDisplayed(autoModerationUpdatePage.getSaveButton());
      await autoModerationUpdatePage.save();
      await waitUntilHidden(autoModerationUpdatePage.getSaveButton());
      expect(await autoModerationUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createAutoModeration();
    await autoModerationComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await autoModerationComponentsPage.countDeleteButtons();
    await createAutoModeration();

    await autoModerationComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await autoModerationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last AutoModeration', async () => {
    await autoModerationComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await autoModerationComponentsPage.countDeleteButtons();
    await autoModerationComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    autoModerationDeleteDialog = new AutoModerationDeleteDialog();
    expect(await autoModerationDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botAutoModeration.delete.question/);
    await autoModerationDeleteDialog.clickOnConfirmButton();

    await autoModerationComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await autoModerationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
