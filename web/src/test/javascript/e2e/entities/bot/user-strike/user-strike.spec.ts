import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import UserStrikeComponentsPage, { UserStrikeDeleteDialog } from './user-strike.page-object';
import UserStrikeUpdatePage from './user-strike-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('UserStrike e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let userStrikeUpdatePage: UserStrikeUpdatePage;
  let userStrikeComponentsPage: UserStrikeComponentsPage;
  let userStrikeDeleteDialog: UserStrikeDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load UserStrikes', async () => {
    await navBarPage.getEntityPage('user-strike');
    userStrikeComponentsPage = new UserStrikeComponentsPage();
    expect(await userStrikeComponentsPage.getTitle().getText()).to.match(/User Strikes/);
  });

  it('should load create UserStrike page', async () => {
    await userStrikeComponentsPage.clickOnCreateButton();
    userStrikeUpdatePage = new UserStrikeUpdatePage();
    expect(await userStrikeUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botUserStrike.home.createOrEditLabel/);
    await userStrikeUpdatePage.cancel();
  });

  it('should create and save UserStrikes', async () => {
    async function createUserStrike() {
      await userStrikeComponentsPage.clickOnCreateButton();
      await userStrikeUpdatePage.setCountInput('5');
      expect(await userStrikeUpdatePage.getCountInput()).to.eq('5');
      await userStrikeUpdatePage.setUserIdInput('5');
      expect(await userStrikeUpdatePage.getUserIdInput()).to.eq('5');
      await userStrikeUpdatePage.setGuildIdInput('5');
      expect(await userStrikeUpdatePage.getGuildIdInput()).to.eq('5');
      await userStrikeUpdatePage.discordUserSelectLastOption();
      await waitUntilDisplayed(userStrikeUpdatePage.getSaveButton());
      await userStrikeUpdatePage.save();
      await waitUntilHidden(userStrikeUpdatePage.getSaveButton());
      expect(await userStrikeUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createUserStrike();
    await userStrikeComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await userStrikeComponentsPage.countDeleteButtons();
    await createUserStrike();

    await userStrikeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await userStrikeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last UserStrike', async () => {
    await userStrikeComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await userStrikeComponentsPage.countDeleteButtons();
    await userStrikeComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    userStrikeDeleteDialog = new UserStrikeDeleteDialog();
    expect(await userStrikeDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botUserStrike.delete.question/);
    await userStrikeDeleteDialog.clickOnConfirmButton();

    await userStrikeComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await userStrikeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
