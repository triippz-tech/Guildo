import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildServerComponentsPage, { GuildServerDeleteDialog } from './guild-server.page-object';
import GuildServerUpdatePage from './guild-server-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GuildServer e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildServerUpdatePage: GuildServerUpdatePage;
  let guildServerComponentsPage: GuildServerComponentsPage;
  let guildServerDeleteDialog: GuildServerDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildServers', async () => {
    await navBarPage.getEntityPage('guild-server');
    guildServerComponentsPage = new GuildServerComponentsPage();
    expect(await guildServerComponentsPage.getTitle().getText()).to.match(/Guild Servers/);
  });

  it('should load create GuildServer page', async () => {
    await guildServerComponentsPage.clickOnCreateButton();
    guildServerUpdatePage = new GuildServerUpdatePage();
    expect(await guildServerUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botGuildServer.home.createOrEditLabel/);
    await guildServerUpdatePage.cancel();
  });

  it('should create and save GuildServers', async () => {
    async function createGuildServer() {
      await guildServerComponentsPage.clickOnCreateButton();
      await guildServerUpdatePage.setGuildIdInput('5');
      expect(await guildServerUpdatePage.getGuildIdInput()).to.eq('5');
      await guildServerUpdatePage.setGuildNameInput('guildName');
      expect(await guildServerUpdatePage.getGuildNameInput()).to.match(/guildName/);
      await guildServerUpdatePage.setIconInput('icon');
      expect(await guildServerUpdatePage.getIconInput()).to.match(/icon/);
      await guildServerUpdatePage.setOwnerInput('5');
      expect(await guildServerUpdatePage.getOwnerInput()).to.eq('5');
      await guildServerUpdatePage.serverLevelSelectLastOption();
      await guildServerUpdatePage.guildProfileSelectLastOption();
      await guildServerUpdatePage.applicationFormSelectLastOption();
      await guildServerUpdatePage.guildSettingsSelectLastOption();
      await guildServerUpdatePage.welcomeMessageSelectLastOption();
      await waitUntilDisplayed(guildServerUpdatePage.getSaveButton());
      await guildServerUpdatePage.save();
      await waitUntilHidden(guildServerUpdatePage.getSaveButton());
      expect(await guildServerUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildServer();
    await guildServerComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildServerComponentsPage.countDeleteButtons();
    await createGuildServer();

    await guildServerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildServerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildServer', async () => {
    await guildServerComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildServerComponentsPage.countDeleteButtons();
    await guildServerComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildServerDeleteDialog = new GuildServerDeleteDialog();
    expect(await guildServerDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botGuildServer.delete.question/);
    await guildServerDeleteDialog.clickOnConfirmButton();

    await guildServerComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildServerComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
