import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GuildServerSettingsComponentsPage, { GuildServerSettingsDeleteDialog } from './guild-server-settings.page-object';
import GuildServerSettingsUpdatePage from './guild-server-settings-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GuildServerSettings e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let guildServerSettingsUpdatePage: GuildServerSettingsUpdatePage;
  let guildServerSettingsComponentsPage: GuildServerSettingsComponentsPage;
  let guildServerSettingsDeleteDialog: GuildServerSettingsDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GuildServerSettings', async () => {
    await navBarPage.getEntityPage('guild-server-settings');
    guildServerSettingsComponentsPage = new GuildServerSettingsComponentsPage();
    expect(await guildServerSettingsComponentsPage.getTitle().getText()).to.match(/Guild Server Settings/);
  });

  it('should load create GuildServerSettings page', async () => {
    await guildServerSettingsComponentsPage.clickOnCreateButton();
    guildServerSettingsUpdatePage = new GuildServerSettingsUpdatePage();
    expect(await guildServerSettingsUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botGuildServerSettings.home.createOrEditLabel/
    );
    await guildServerSettingsUpdatePage.cancel();
  });

  it('should create and save GuildServerSettings', async () => {
    async function createGuildServerSettings() {
      await guildServerSettingsComponentsPage.clickOnCreateButton();
      await guildServerSettingsUpdatePage.setPrefixInput('prefix');
      expect(await guildServerSettingsUpdatePage.getPrefixInput()).to.match(/prefix/);
      await guildServerSettingsUpdatePage.setTimezoneInput('timezone');
      expect(await guildServerSettingsUpdatePage.getTimezoneInput()).to.match(/timezone/);
      const selectedRaidModeEnabled = await guildServerSettingsUpdatePage.getRaidModeEnabledInput().isSelected();
      if (selectedRaidModeEnabled) {
        await guildServerSettingsUpdatePage.getRaidModeEnabledInput().click();
        expect(await guildServerSettingsUpdatePage.getRaidModeEnabledInput().isSelected()).to.be.false;
      } else {
        await guildServerSettingsUpdatePage.getRaidModeEnabledInput().click();
        expect(await guildServerSettingsUpdatePage.getRaidModeEnabledInput().isSelected()).to.be.true;
      }
      await guildServerSettingsUpdatePage.setRaidModeReasonInput('raidModeReason');
      expect(await guildServerSettingsUpdatePage.getRaidModeReasonInput()).to.match(/raidModeReason/);
      await guildServerSettingsUpdatePage.setMaxStrikesInput('5');
      expect(await guildServerSettingsUpdatePage.getMaxStrikesInput()).to.eq('5');
      const selectedAcceptingApplications = await guildServerSettingsUpdatePage.getAcceptingApplicationsInput().isSelected();
      if (selectedAcceptingApplications) {
        await guildServerSettingsUpdatePage.getAcceptingApplicationsInput().click();
        expect(await guildServerSettingsUpdatePage.getAcceptingApplicationsInput().isSelected()).to.be.false;
      } else {
        await guildServerSettingsUpdatePage.getAcceptingApplicationsInput().click();
        expect(await guildServerSettingsUpdatePage.getAcceptingApplicationsInput().isSelected()).to.be.true;
      }
      await guildServerSettingsUpdatePage.autoModConfigSelectLastOption();
      await guildServerSettingsUpdatePage.punishmentConfigSelectLastOption();
      await waitUntilDisplayed(guildServerSettingsUpdatePage.getSaveButton());
      await guildServerSettingsUpdatePage.save();
      await waitUntilHidden(guildServerSettingsUpdatePage.getSaveButton());
      expect(await guildServerSettingsUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGuildServerSettings();
    await guildServerSettingsComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await guildServerSettingsComponentsPage.countDeleteButtons();
    await createGuildServerSettings();

    await guildServerSettingsComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await guildServerSettingsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GuildServerSettings', async () => {
    await guildServerSettingsComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await guildServerSettingsComponentsPage.countDeleteButtons();
    await guildServerSettingsComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    guildServerSettingsDeleteDialog = new GuildServerSettingsDeleteDialog();
    expect(await guildServerSettingsDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /webApp.botGuildServerSettings.delete.question/
    );
    await guildServerSettingsDeleteDialog.clickOnConfirmButton();

    await guildServerSettingsComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await guildServerSettingsComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
