import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import WelcomeMessageComponentsPage, { WelcomeMessageDeleteDialog } from './welcome-message.page-object';
import WelcomeMessageUpdatePage from './welcome-message-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('WelcomeMessage e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let welcomeMessageUpdatePage: WelcomeMessageUpdatePage;
  let welcomeMessageComponentsPage: WelcomeMessageComponentsPage;
  let welcomeMessageDeleteDialog: WelcomeMessageDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load WelcomeMessages', async () => {
    await navBarPage.getEntityPage('welcome-message');
    welcomeMessageComponentsPage = new WelcomeMessageComponentsPage();
    expect(await welcomeMessageComponentsPage.getTitle().getText()).to.match(/Welcome Messages/);
  });

  it('should load create WelcomeMessage page', async () => {
    await welcomeMessageComponentsPage.clickOnCreateButton();
    welcomeMessageUpdatePage = new WelcomeMessageUpdatePage();
    expect(await welcomeMessageUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botWelcomeMessage.home.createOrEditLabel/);
    await welcomeMessageUpdatePage.cancel();
  });

  it('should create and save WelcomeMessages', async () => {
    async function createWelcomeMessage() {
      await welcomeMessageComponentsPage.clickOnCreateButton();
      await welcomeMessageUpdatePage.setNameInput('name');
      expect(await welcomeMessageUpdatePage.getNameInput()).to.match(/name/);
      await welcomeMessageUpdatePage.setMessageTitleInput('messageTitle');
      expect(await welcomeMessageUpdatePage.getMessageTitleInput()).to.match(/messageTitle/);
      await welcomeMessageUpdatePage.setBodyInput('body');
      expect(await welcomeMessageUpdatePage.getBodyInput()).to.match(/body/);
      await welcomeMessageUpdatePage.setFooterInput('footer');
      expect(await welcomeMessageUpdatePage.getFooterInput()).to.match(/footer/);
      await welcomeMessageUpdatePage.setLogoUrlInput('logoUrl');
      expect(await welcomeMessageUpdatePage.getLogoUrlInput()).to.match(/logoUrl/);
      await welcomeMessageUpdatePage.setGuildIdInput('5');
      expect(await welcomeMessageUpdatePage.getGuildIdInput()).to.eq('5');
      await waitUntilDisplayed(welcomeMessageUpdatePage.getSaveButton());
      await welcomeMessageUpdatePage.save();
      await waitUntilHidden(welcomeMessageUpdatePage.getSaveButton());
      expect(await welcomeMessageUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createWelcomeMessage();
    await welcomeMessageComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await welcomeMessageComponentsPage.countDeleteButtons();
    await createWelcomeMessage();

    await welcomeMessageComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await welcomeMessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last WelcomeMessage', async () => {
    await welcomeMessageComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await welcomeMessageComponentsPage.countDeleteButtons();
    await welcomeMessageComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    welcomeMessageDeleteDialog = new WelcomeMessageDeleteDialog();
    expect(await welcomeMessageDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botWelcomeMessage.delete.question/);
    await welcomeMessageDeleteDialog.clickOnConfirmButton();

    await welcomeMessageComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await welcomeMessageComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
