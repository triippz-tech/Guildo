import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildEventComponentsPage, { GuildEventDeleteDialog } from './guild-event.page-object';
import GuildEventUpdatePage from './guild-event-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GuildEvent e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildEventUpdatePage: GuildEventUpdatePage;
  let guildEventComponentsPage: GuildEventComponentsPage;
  let guildEventDeleteDialog: GuildEventDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildEvents', async () => {
    await navBarPage.getEntityPage('guild-event');
    guildEventComponentsPage = new GuildEventComponentsPage();
    expect(await guildEventComponentsPage.getTitle().getText()).to.match(/Guild Events/);
  });

  it('should load create GuildEvent page', async () => {
    await guildEventComponentsPage.clickOnCreateButton();
    guildEventUpdatePage = new GuildEventUpdatePage();
    expect(await guildEventUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botGuildEvent.home.createOrEditLabel/);
    await guildEventUpdatePage.cancel();
  });

  it('should create and save GuildEvents', async () => {
    async function createGuildEvent() {
      await guildEventComponentsPage.clickOnCreateButton();
      await guildEventUpdatePage.setEventNameInput('eventName');
      expect(await guildEventUpdatePage.getEventNameInput()).to.match(/eventName/);
      await guildEventUpdatePage.setEventImageUrlInput('eventImageUrl');
      expect(await guildEventUpdatePage.getEventImageUrlInput()).to.match(/eventImageUrl/);
      await guildEventUpdatePage.setEventMessageInput('eventMessage');
      expect(await guildEventUpdatePage.getEventMessageInput()).to.match(/eventMessage/);
      await guildEventUpdatePage.setEventStartInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await guildEventUpdatePage.getEventStartInput()).to.contain('2001-01-01T02:30');
      await guildEventUpdatePage.setGuildIdInput('5');
      expect(await guildEventUpdatePage.getGuildIdInput()).to.eq('5');
      await guildEventUpdatePage.eventGuildSelectLastOption();
      await waitUntilDisplayed(guildEventUpdatePage.getSaveButton());
      await guildEventUpdatePage.save();
      await waitUntilHidden(guildEventUpdatePage.getSaveButton());
      expect(await guildEventUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildEvent();
    await guildEventComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildEventComponentsPage.countDeleteButtons();
    await createGuildEvent();

    await guildEventComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildEventComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildEvent', async () => {
    await guildEventComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildEventComponentsPage.countDeleteButtons();
    await guildEventComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildEventDeleteDialog = new GuildEventDeleteDialog();
    expect(await guildEventDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botGuildEvent.delete.question/);
    await guildEventDeleteDialog.clickOnConfirmButton();

    await guildEventComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildEventComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
