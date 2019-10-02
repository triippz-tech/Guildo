import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import DiscordUserComponentsPage, { DiscordUserDeleteDialog } from './discord-user.page-object';
import DiscordUserUpdatePage from './discord-user-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('DiscordUser e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let discordUserUpdatePage: DiscordUserUpdatePage;
  let discordUserComponentsPage: DiscordUserComponentsPage;
  let discordUserDeleteDialog: DiscordUserDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load DiscordUsers', async () => {
    await navBarPage.getEntityPage('discord-user');
    discordUserComponentsPage = new DiscordUserComponentsPage();
    expect(await discordUserComponentsPage.getTitle().getText()).to.match(/Discord Users/);
  });

  it('should load create DiscordUser page', async () => {
    await discordUserComponentsPage.clickOnCreateButton();
    discordUserUpdatePage = new DiscordUserUpdatePage();
    expect(await discordUserUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botDiscordUser.home.createOrEditLabel/);
    await discordUserUpdatePage.cancel();
  });

  it('should create and save DiscordUsers', async () => {
    async function createDiscordUser() {
      await discordUserComponentsPage.clickOnCreateButton();
      await discordUserUpdatePage.setUserIdInput('5');
      expect(await discordUserUpdatePage.getUserIdInput()).to.eq('5');
      await discordUserUpdatePage.setUserNameInput('userName');
      expect(await discordUserUpdatePage.getUserNameInput()).to.match(/userName/);
      await discordUserUpdatePage.setIconInput('icon');
      expect(await discordUserUpdatePage.getIconInput()).to.match(/icon/);
      await discordUserUpdatePage.setCommandsIssuedInput('5');
      expect(await discordUserUpdatePage.getCommandsIssuedInput()).to.eq('5');
      const selectedBlacklisted = await discordUserUpdatePage.getBlacklistedInput().isSelected();
      if (selectedBlacklisted) {
        await discordUserUpdatePage.getBlacklistedInput().click();
        expect(await discordUserUpdatePage.getBlacklistedInput().isSelected()).to.be.false;
      } else {
        await discordUserUpdatePage.getBlacklistedInput().click();
        expect(await discordUserUpdatePage.getBlacklistedInput().isSelected()).to.be.true;
      }
      await discordUserUpdatePage.userLevelSelectLastOption();
      await discordUserUpdatePage.userProfileSelectLastOption();
      await waitUntilDisplayed(discordUserUpdatePage.getSaveButton());
      await discordUserUpdatePage.save();
      await waitUntilHidden(discordUserUpdatePage.getSaveButton());
      expect(await discordUserUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createDiscordUser();
    await discordUserComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await discordUserComponentsPage.countDeleteButtons();
    await createDiscordUser();

    await discordUserComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await discordUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last DiscordUser', async () => {
    await discordUserComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await discordUserComponentsPage.countDeleteButtons();
    await discordUserComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    discordUserDeleteDialog = new DiscordUserDeleteDialog();
    expect(await discordUserDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botDiscordUser.delete.question/);
    await discordUserDeleteDialog.clickOnConfirmButton();

    await discordUserComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await discordUserComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
