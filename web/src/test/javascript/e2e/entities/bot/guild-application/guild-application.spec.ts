import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildApplicationComponentsPage, { GuildApplicationDeleteDialog } from './guild-application.page-object';
import GuildApplicationUpdatePage from './guild-application-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('GuildApplication e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildApplicationUpdatePage: GuildApplicationUpdatePage;
  let guildApplicationComponentsPage: GuildApplicationComponentsPage;
  let guildApplicationDeleteDialog: GuildApplicationDeleteDialog;
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/logo-jhipster.png';
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildApplications', async () => {
    await navBarPage.getEntityPage('guild-application');
    guildApplicationComponentsPage = new GuildApplicationComponentsPage();
    expect(await guildApplicationComponentsPage.getTitle().getText()).to.match(/Guild Applications/);
  });

  it('should load create GuildApplication page', async () => {
    await guildApplicationComponentsPage.clickOnCreateButton();
    guildApplicationUpdatePage = new GuildApplicationUpdatePage();
    expect(await guildApplicationUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botGuildApplication.home.createOrEditLabel/
    );
    await guildApplicationUpdatePage.cancel();
  });

  it('should create and save GuildApplications', async () => {
    async function createGuildApplication() {
      await guildApplicationComponentsPage.clickOnCreateButton();
      await guildApplicationUpdatePage.setCharacterNameInput('characterName');
      expect(await guildApplicationUpdatePage.getCharacterNameInput()).to.match(/characterName/);
      await guildApplicationUpdatePage.setCharacterTypeInput('characterType');
      expect(await guildApplicationUpdatePage.getCharacterTypeInput()).to.match(/characterType/);
      await guildApplicationUpdatePage.setApplicationFileInput(absolutePath);
      await guildApplicationUpdatePage.statusSelectLastOption();
      await guildApplicationUpdatePage.acceptedBySelectLastOption();
      await guildApplicationUpdatePage.appliedUserSelectLastOption();
      await guildApplicationUpdatePage.guildServerSelectLastOption();
      await waitUntilDisplayed(guildApplicationUpdatePage.getSaveButton());
      await guildApplicationUpdatePage.save();
      await waitUntilHidden(guildApplicationUpdatePage.getSaveButton());
      expect(await guildApplicationUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildApplication();
    await guildApplicationComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildApplicationComponentsPage.countDeleteButtons();
    await createGuildApplication();

    await guildApplicationComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildApplicationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildApplication', async () => {
    await guildApplicationComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildApplicationComponentsPage.countDeleteButtons();
    await guildApplicationComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildApplicationDeleteDialog = new GuildApplicationDeleteDialog();
    expect(await guildApplicationDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botGuildApplication.delete.question/);
    await guildApplicationDeleteDialog.clickOnConfirmButton();

    await guildApplicationComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildApplicationComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
