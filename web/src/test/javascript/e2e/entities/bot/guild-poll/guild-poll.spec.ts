import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildPollComponentsPage, { GuildPollDeleteDialog } from './guild-poll.page-object';
import GuildPollUpdatePage from './guild-poll-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GuildPoll e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildPollUpdatePage: GuildPollUpdatePage;
  let guildPollComponentsPage: GuildPollComponentsPage;
  let guildPollDeleteDialog: GuildPollDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildPolls', async () => {
    await navBarPage.getEntityPage('guild-poll');
    guildPollComponentsPage = new GuildPollComponentsPage();
    expect(await guildPollComponentsPage.getTitle().getText()).to.match(/Guild Polls/);
  });

  it('should load create GuildPoll page', async () => {
    await guildPollComponentsPage.clickOnCreateButton();
    guildPollUpdatePage = new GuildPollUpdatePage();
    expect(await guildPollUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botGuildPoll.home.createOrEditLabel/);
    await guildPollUpdatePage.cancel();
  });

  it('should create and save GuildPolls', async () => {
    async function createGuildPoll() {
      await guildPollComponentsPage.clickOnCreateButton();
      await guildPollUpdatePage.setPollNameInput('pollName');
      expect(await guildPollUpdatePage.getPollNameInput()).to.match(/pollName/);
      await guildPollUpdatePage.setDescriptionInput('description');
      expect(await guildPollUpdatePage.getDescriptionInput()).to.match(/description/);
      await guildPollUpdatePage.setTextChannelIdInput('5');
      expect(await guildPollUpdatePage.getTextChannelIdInput()).to.eq('5');
      await guildPollUpdatePage.setFinishTimeInput('01-01-2001');
      expect(await guildPollUpdatePage.getFinishTimeInput()).to.eq('2001-01-01');
      const selectedCompleted = await guildPollUpdatePage.getCompletedInput().isSelected();
      if (selectedCompleted) {
        await guildPollUpdatePage.getCompletedInput().click();
        expect(await guildPollUpdatePage.getCompletedInput().isSelected()).to.be.false;
      } else {
        await guildPollUpdatePage.getCompletedInput().click();
        expect(await guildPollUpdatePage.getCompletedInput().isSelected()).to.be.true;
      }
      await guildPollUpdatePage.setGuildIdInput('5');
      expect(await guildPollUpdatePage.getGuildIdInput()).to.eq('5');
      await guildPollUpdatePage.pollItemsSelectLastOption();
      await guildPollUpdatePage.pollServerSelectLastOption();
      await waitUntilDisplayed(guildPollUpdatePage.getSaveButton());
      await guildPollUpdatePage.save();
      await waitUntilHidden(guildPollUpdatePage.getSaveButton());
      expect(await guildPollUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildPoll();
    await guildPollComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildPollComponentsPage.countDeleteButtons();
    await createGuildPoll();

    await guildPollComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildPollComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildPoll', async () => {
    await guildPollComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildPollComponentsPage.countDeleteButtons();
    await guildPollComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildPollDeleteDialog = new GuildPollDeleteDialog();
    expect(await guildPollDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botGuildPoll.delete.question/);
    await guildPollDeleteDialog.clickOnConfirmButton();

    await guildPollComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildPollComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
