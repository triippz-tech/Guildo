import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import AutoModMentionsComponentsPage, { AutoModMentionsDeleteDialog } from './auto-mod-mentions.page-object';
import AutoModMentionsUpdatePage from './auto-mod-mentions-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('AutoModMentions e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let autoModMentionsUpdatePage: AutoModMentionsUpdatePage;
  let autoModMentionsComponentsPage: AutoModMentionsComponentsPage;
  let autoModMentionsDeleteDialog: AutoModMentionsDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load AutoModMentions', async () => {
    await navBarPage.getEntityPage('auto-mod-mentions');
    autoModMentionsComponentsPage = new AutoModMentionsComponentsPage();
    expect(await autoModMentionsComponentsPage.getTitle().getText()).to.match(/Auto Mod Mentions/);
  });

  it('should load create AutoModMentions page', async () => {
    await autoModMentionsComponentsPage.clickOnCreateButton();
    autoModMentionsUpdatePage = new AutoModMentionsUpdatePage();
    expect(await autoModMentionsUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botAutoModMentions.home.createOrEditLabel/);
    await autoModMentionsUpdatePage.cancel();
  });

  it('should create and save AutoModMentions', async () => {
    async function createAutoModMentions() {
      await autoModMentionsComponentsPage.clickOnCreateButton();
      await autoModMentionsUpdatePage.setMaxMentionsInput('5');
      expect(await autoModMentionsUpdatePage.getMaxMentionsInput()).to.eq('5');
      await autoModMentionsUpdatePage.setMaxMsgLinesInput('5');
      expect(await autoModMentionsUpdatePage.getMaxMsgLinesInput()).to.eq('5');
      await autoModMentionsUpdatePage.setMaxRoleMentionsInput('5');
      expect(await autoModMentionsUpdatePage.getMaxRoleMentionsInput()).to.eq('5');
      await waitUntilDisplayed(autoModMentionsUpdatePage.getSaveButton());
      await autoModMentionsUpdatePage.save();
      await waitUntilHidden(autoModMentionsUpdatePage.getSaveButton());
      expect(await autoModMentionsUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createAutoModMentions();
    await autoModMentionsComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await autoModMentionsComponentsPage.countDeleteButtons();
    await createAutoModMentions();

    await autoModMentionsComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await autoModMentionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last AutoModMentions', async () => {
    await autoModMentionsComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await autoModMentionsComponentsPage.countDeleteButtons();
    await autoModMentionsComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    autoModMentionsDeleteDialog = new AutoModMentionsDeleteDialog();
    expect(await autoModMentionsDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botAutoModMentions.delete.question/);
    await autoModMentionsDeleteDialog.clickOnConfirmButton();

    await autoModMentionsComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await autoModMentionsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
