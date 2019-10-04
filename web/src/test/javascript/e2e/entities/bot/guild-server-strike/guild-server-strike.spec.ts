import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildServerStrikeComponentsPage, { GuildServerStrikeDeleteDialog } from './guild-server-strike.page-object';
import GuildServerStrikeUpdatePage from './guild-server-strike-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GuildServerStrike e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildServerStrikeUpdatePage: GuildServerStrikeUpdatePage;
  let guildServerStrikeComponentsPage: GuildServerStrikeComponentsPage;
  /* let guildServerStrikeDeleteDialog: GuildServerStrikeDeleteDialog; */

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildServerStrikes', async () => {
    await navBarPage.getEntityPage('guild-server-strike');
    guildServerStrikeComponentsPage = new GuildServerStrikeComponentsPage();
    expect(await guildServerStrikeComponentsPage.getTitle().getText()).to.match(/Guild Server Strikes/);
  });

  it('should load create GuildServerStrike page', async () => {
    await guildServerStrikeComponentsPage.clickOnCreateButton();
    guildServerStrikeUpdatePage = new GuildServerStrikeUpdatePage();
    expect(await guildServerStrikeUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botGuildServerStrike.home.createOrEditLabel/
    );
    await guildServerStrikeUpdatePage.cancel();
  });

  /*  it('should create and save GuildServerStrikes', async () => {
        async function createGuildServerStrike() {
            await guildServerStrikeComponentsPage.clickOnCreateButton();
            await guildServerStrikeUpdatePage.setCountInput('5');
            expect(await guildServerStrikeUpdatePage.getCountInput()).to.eq('5');
            await guildServerStrikeUpdatePage.setUserIdInput('5');
            expect(await guildServerStrikeUpdatePage.getUserIdInput()).to.eq('5');
            await guildServerStrikeUpdatePage.setGuildIdInput('5');
            expect(await guildServerStrikeUpdatePage.getGuildIdInput()).to.eq('5');
            await guildServerStrikeUpdatePage.discordUserSelectLastOption();
            await waitUntilDisplayed(guildServerStrikeUpdatePage.getSaveButton());
            await guildServerStrikeUpdatePage.save();
            await waitUntilHidden(guildServerStrikeUpdatePage.getSaveButton());
            expect(await guildServerStrikeUpdatePage.getSaveButton().isPresent()).to.be.false;
        }

        await createGuildServerStrike();
        await guildServerStrikeComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeCreate = await guildServerStrikeComponentsPage.countDeleteButtons();
        await createGuildServerStrike();

        await guildServerStrikeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
        expect(await guildServerStrikeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    }); */

  /*  it('should delete last GuildServerStrike', async () => {
        await guildServerStrikeComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeDelete = await guildServerStrikeComponentsPage.countDeleteButtons();
        await guildServerStrikeComponentsPage.clickOnLastDeleteButton();

        const deleteModal = element(by.className('modal'));
        await waitUntilDisplayed(deleteModal);

        guildServerStrikeDeleteDialog = new GuildServerStrikeDeleteDialog();
        expect(await guildServerStrikeDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botGuildServerStrike.delete.question/);
        await guildServerStrikeDeleteDialog.clickOnConfirmButton();

        await guildServerStrikeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
        expect(await guildServerStrikeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
