import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildPollItemComponentsPage, { GuildPollItemDeleteDialog } from './guild-poll-item.page-object';
import GuildPollItemUpdatePage from './guild-poll-item-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GuildPollItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildPollItemUpdatePage: GuildPollItemUpdatePage;
  let guildPollItemComponentsPage: GuildPollItemComponentsPage;
  let guildPollItemDeleteDialog: GuildPollItemDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildPollItems', async () => {
    await navBarPage.getEntityPage('guild-poll-item');
    guildPollItemComponentsPage = new GuildPollItemComponentsPage();
    expect(await guildPollItemComponentsPage.getTitle().getText()).to.match(/Guild Poll Items/);
  });

  it('should load create GuildPollItem page', async () => {
    await guildPollItemComponentsPage.clickOnCreateButton();
    guildPollItemUpdatePage = new GuildPollItemUpdatePage();
    expect(await guildPollItemUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botGuildPollItem.home.createOrEditLabel/);
    await guildPollItemUpdatePage.cancel();
  });

  it('should create and save GuildPollItems', async () => {
    async function createGuildPollItem() {
      await guildPollItemComponentsPage.clickOnCreateButton();
      await guildPollItemUpdatePage.setItemNameInput('itemName');
      expect(await guildPollItemUpdatePage.getItemNameInput()).to.match(/itemName/);
      await guildPollItemUpdatePage.setVotesInput('5');
      expect(await guildPollItemUpdatePage.getVotesInput()).to.eq('5');
      await waitUntilDisplayed(guildPollItemUpdatePage.getSaveButton());
      await guildPollItemUpdatePage.save();
      await waitUntilHidden(guildPollItemUpdatePage.getSaveButton());
      expect(await guildPollItemUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildPollItem();
    await guildPollItemComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildPollItemComponentsPage.countDeleteButtons();
    await createGuildPollItem();

    await guildPollItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildPollItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildPollItem', async () => {
    await guildPollItemComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildPollItemComponentsPage.countDeleteButtons();
    await guildPollItemComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildPollItemDeleteDialog = new GuildPollItemDeleteDialog();
    expect(await guildPollItemDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botGuildPollItem.delete.question/);
    await guildPollItemDeleteDialog.clickOnConfirmButton();

    await guildPollItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildPollItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
