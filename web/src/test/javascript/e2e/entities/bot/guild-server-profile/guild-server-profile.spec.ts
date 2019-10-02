import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildServerProfileComponentsPage, { GuildServerProfileDeleteDialog } from './guild-server-profile.page-object';
import GuildServerProfileUpdatePage from './guild-server-profile-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GuildServerProfile e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildServerProfileUpdatePage: GuildServerProfileUpdatePage;
  let guildServerProfileComponentsPage: GuildServerProfileComponentsPage;
  let guildServerProfileDeleteDialog: GuildServerProfileDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildServerProfiles', async () => {
    await navBarPage.getEntityPage('guild-server-profile');
    guildServerProfileComponentsPage = new GuildServerProfileComponentsPage();
    expect(await guildServerProfileComponentsPage.getTitle().getText()).to.match(/Guild Server Profiles/);
  });

  it('should load create GuildServerProfile page', async () => {
    await guildServerProfileComponentsPage.clickOnCreateButton();
    guildServerProfileUpdatePage = new GuildServerProfileUpdatePage();
    expect(await guildServerProfileUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botGuildServerProfile.home.createOrEditLabel/
    );
    await guildServerProfileUpdatePage.cancel();
  });

  it('should create and save GuildServerProfiles', async () => {
    async function createGuildServerProfile() {
      await guildServerProfileComponentsPage.clickOnCreateButton();
      await guildServerProfileUpdatePage.guildTypeSelectLastOption();
      await guildServerProfileUpdatePage.playStyleSelectLastOption();
      await guildServerProfileUpdatePage.setDescriptionInput('description');
      expect(await guildServerProfileUpdatePage.getDescriptionInput()).to.match(/description/);
      await guildServerProfileUpdatePage.setWebsiteInput('website');
      expect(await guildServerProfileUpdatePage.getWebsiteInput()).to.match(/website/);
      await guildServerProfileUpdatePage.setDiscordUrlInput('discordUrl');
      expect(await guildServerProfileUpdatePage.getDiscordUrlInput()).to.match(/discordUrl/);
      await waitUntilDisplayed(guildServerProfileUpdatePage.getSaveButton());
      await guildServerProfileUpdatePage.save();
      await waitUntilHidden(guildServerProfileUpdatePage.getSaveButton());
      expect(await guildServerProfileUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildServerProfile();
    await guildServerProfileComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildServerProfileComponentsPage.countDeleteButtons();
    await createGuildServerProfile();

    await guildServerProfileComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildServerProfileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildServerProfile', async () => {
    await guildServerProfileComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildServerProfileComponentsPage.countDeleteButtons();
    await guildServerProfileComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildServerProfileDeleteDialog = new GuildServerProfileDeleteDialog();
    expect(await guildServerProfileDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /webApp.botGuildServerProfile.delete.question/
    );
    await guildServerProfileDeleteDialog.clickOnConfirmButton();

    await guildServerProfileComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildServerProfileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
