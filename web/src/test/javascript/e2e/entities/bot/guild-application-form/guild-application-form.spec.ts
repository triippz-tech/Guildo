import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildApplicationFormComponentsPage, { GuildApplicationFormDeleteDialog } from './guild-application-form.page-object';
import GuildApplicationFormUpdatePage from './guild-application-form-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('GuildApplicationForm e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildApplicationFormUpdatePage: GuildApplicationFormUpdatePage;
  let guildApplicationFormComponentsPage: GuildApplicationFormComponentsPage;
  let guildApplicationFormDeleteDialog: GuildApplicationFormDeleteDialog;
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/logo-jhipster.png';
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildApplicationForms', async () => {
    await navBarPage.getEntityPage('guild-application-form');
    guildApplicationFormComponentsPage = new GuildApplicationFormComponentsPage();
    expect(await guildApplicationFormComponentsPage.getTitle().getText()).to.match(/Guild Application Forms/);
  });

  it('should load create GuildApplicationForm page', async () => {
    await guildApplicationFormComponentsPage.clickOnCreateButton();
    guildApplicationFormUpdatePage = new GuildApplicationFormUpdatePage();
    expect(await guildApplicationFormUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botGuildApplicationForm.home.createOrEditLabel/
    );
    await guildApplicationFormUpdatePage.cancel();
  });

  it('should create and save GuildApplicationForms', async () => {
    async function createGuildApplicationForm() {
      await guildApplicationFormComponentsPage.clickOnCreateButton();
      await guildApplicationFormUpdatePage.setApplicationFormInput(absolutePath);
      await guildApplicationFormUpdatePage.setGuildIdInput('5');
      expect(await guildApplicationFormUpdatePage.getGuildIdInput()).to.eq('5');
      await waitUntilDisplayed(guildApplicationFormUpdatePage.getSaveButton());
      await guildApplicationFormUpdatePage.save();
      await waitUntilHidden(guildApplicationFormUpdatePage.getSaveButton());
      expect(await guildApplicationFormUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildApplicationForm();
    await guildApplicationFormComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildApplicationFormComponentsPage.countDeleteButtons();
    await createGuildApplicationForm();

    await guildApplicationFormComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildApplicationFormComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildApplicationForm', async () => {
    await guildApplicationFormComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildApplicationFormComponentsPage.countDeleteButtons();
    await guildApplicationFormComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildApplicationFormDeleteDialog = new GuildApplicationFormDeleteDialog();
    expect(await guildApplicationFormDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /webApp.botGuildApplicationForm.delete.question/
    );
    await guildApplicationFormDeleteDialog.clickOnConfirmButton();

    await guildApplicationFormComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildApplicationFormComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
