import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ServerLogItemComponentsPage, { ServerLogItemDeleteDialog } from './server-log-item.page-object';
import ServerLogItemUpdatePage from './server-log-item-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('ServerLogItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let serverLogItemUpdatePage: ServerLogItemUpdatePage;
  let serverLogItemComponentsPage: ServerLogItemComponentsPage;
  let serverLogItemDeleteDialog: ServerLogItemDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load ServerLogItems', async () => {
    await navBarPage.getEntityPage('server-log-item');
    serverLogItemComponentsPage = new ServerLogItemComponentsPage();
    expect(await serverLogItemComponentsPage.getTitle().getText()).to.match(/Server Log Items/);
  });

  it('should load create ServerLogItem page', async () => {
    await serverLogItemComponentsPage.clickOnCreateButton();
    serverLogItemUpdatePage = new ServerLogItemUpdatePage();
    expect(await serverLogItemUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botServerLogItem.home.createOrEditLabel/);
    await serverLogItemUpdatePage.cancel();
  });

  it('should create and save ServerLogItems', async () => {
    async function createServerLogItem() {
      await serverLogItemComponentsPage.clickOnCreateButton();
      await serverLogItemUpdatePage.activitySelectLastOption();
      await serverLogItemUpdatePage.setChannelIdInput('5');
      expect(await serverLogItemUpdatePage.getChannelIdInput()).to.eq('5');
      await serverLogItemUpdatePage.setChannelNameInput('channelName');
      expect(await serverLogItemUpdatePage.getChannelNameInput()).to.match(/channelName/);
      await serverLogItemUpdatePage.setTimeInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await serverLogItemUpdatePage.getTimeInput()).to.contain('2001-01-01T02:30');
      await serverLogItemUpdatePage.setUserIdInput('5');
      expect(await serverLogItemUpdatePage.getUserIdInput()).to.eq('5');
      await serverLogItemUpdatePage.setUserNameInput('userName');
      expect(await serverLogItemUpdatePage.getUserNameInput()).to.match(/userName/);
      await serverLogItemUpdatePage.setGuildIdInput('5');
      expect(await serverLogItemUpdatePage.getGuildIdInput()).to.eq('5');
      await serverLogItemUpdatePage.serverItemGuildServerSelectLastOption();
      await waitUntilDisplayed(serverLogItemUpdatePage.getSaveButton());
      await serverLogItemUpdatePage.save();
      await waitUntilHidden(serverLogItemUpdatePage.getSaveButton());
      expect(await serverLogItemUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createServerLogItem();
    await serverLogItemComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await serverLogItemComponentsPage.countDeleteButtons();
    await createServerLogItem();

    await serverLogItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await serverLogItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ServerLogItem', async () => {
    await serverLogItemComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await serverLogItemComponentsPage.countDeleteButtons();
    await serverLogItemComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    serverLogItemDeleteDialog = new ServerLogItemDeleteDialog();
    expect(await serverLogItemDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botServerLogItem.delete.question/);
    await serverLogItemDeleteDialog.clickOnConfirmButton();

    await serverLogItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await serverLogItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
