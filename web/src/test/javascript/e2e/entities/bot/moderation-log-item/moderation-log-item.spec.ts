import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ModerationLogItemComponentsPage, { ModerationLogItemDeleteDialog } from './moderation-log-item.page-object';
import ModerationLogItemUpdatePage from './moderation-log-item-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('ModerationLogItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let moderationLogItemUpdatePage: ModerationLogItemUpdatePage;
  let moderationLogItemComponentsPage: ModerationLogItemComponentsPage;
  let moderationLogItemDeleteDialog: ModerationLogItemDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load ModerationLogItems', async () => {
    await navBarPage.getEntityPage('moderation-log-item');
    moderationLogItemComponentsPage = new ModerationLogItemComponentsPage();
    expect(await moderationLogItemComponentsPage.getTitle().getText()).to.match(/Moderation Log Items/);
  });

  it('should load create ModerationLogItem page', async () => {
    await moderationLogItemComponentsPage.clickOnCreateButton();
    moderationLogItemUpdatePage = new ModerationLogItemUpdatePage();
    expect(await moderationLogItemUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botModerationLogItem.home.createOrEditLabel/
    );
    await moderationLogItemUpdatePage.cancel();
  });

  it('should create and save ModerationLogItems', async () => {
    async function createModerationLogItem() {
      await moderationLogItemComponentsPage.clickOnCreateButton();
      await moderationLogItemUpdatePage.setChannelIdInput('5');
      expect(await moderationLogItemUpdatePage.getChannelIdInput()).to.eq('5');
      await moderationLogItemUpdatePage.setChannelNameInput('channelName');
      expect(await moderationLogItemUpdatePage.getChannelNameInput()).to.match(/channelName/);
      await moderationLogItemUpdatePage.setIssuedByIdInput('5');
      expect(await moderationLogItemUpdatePage.getIssuedByIdInput()).to.eq('5');
      await moderationLogItemUpdatePage.setIssuedByNameInput('issuedByName');
      expect(await moderationLogItemUpdatePage.getIssuedByNameInput()).to.match(/issuedByName/);
      await moderationLogItemUpdatePage.setIssuedToIdInput('5');
      expect(await moderationLogItemUpdatePage.getIssuedToIdInput()).to.eq('5');
      await moderationLogItemUpdatePage.setIssuedToNameInput('issuedToName');
      expect(await moderationLogItemUpdatePage.getIssuedToNameInput()).to.match(/issuedToName/);
      await moderationLogItemUpdatePage.setReasonInput('reason');
      expect(await moderationLogItemUpdatePage.getReasonInput()).to.match(/reason/);
      await moderationLogItemUpdatePage.setTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await moderationLogItemUpdatePage.getTimeInput()).to.contain('2001-01-01T02:30');
      await moderationLogItemUpdatePage.moderationActionSelectLastOption();
      await moderationLogItemUpdatePage.setGuildIdInput('5');
      expect(await moderationLogItemUpdatePage.getGuildIdInput()).to.eq('5');
      await moderationLogItemUpdatePage.modItemGuildServerSelectLastOption();
      await waitUntilDisplayed(moderationLogItemUpdatePage.getSaveButton());
      await moderationLogItemUpdatePage.save();
      await waitUntilHidden(moderationLogItemUpdatePage.getSaveButton());
      expect(await moderationLogItemUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createModerationLogItem();
    await moderationLogItemComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await moderationLogItemComponentsPage.countDeleteButtons();
    await createModerationLogItem();

    await moderationLogItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await moderationLogItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ModerationLogItem', async () => {
    await moderationLogItemComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await moderationLogItemComponentsPage.countDeleteButtons();
    await moderationLogItemComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    moderationLogItemDeleteDialog = new ModerationLogItemDeleteDialog();
    expect(await moderationLogItemDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botModerationLogItem.delete.question/);
    await moderationLogItemDeleteDialog.clickOnConfirmButton();

    await moderationLogItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await moderationLogItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
