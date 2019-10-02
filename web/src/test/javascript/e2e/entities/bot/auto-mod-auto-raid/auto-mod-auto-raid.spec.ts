import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import AutoModAutoRaidComponentsPage, { AutoModAutoRaidDeleteDialog } from './auto-mod-auto-raid.page-object';
import AutoModAutoRaidUpdatePage from './auto-mod-auto-raid-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('AutoModAutoRaid e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let autoModAutoRaidUpdatePage: AutoModAutoRaidUpdatePage;
  let autoModAutoRaidComponentsPage: AutoModAutoRaidComponentsPage;
  let autoModAutoRaidDeleteDialog: AutoModAutoRaidDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load AutoModAutoRaids', async () => {
    await navBarPage.getEntityPage('auto-mod-auto-raid');
    autoModAutoRaidComponentsPage = new AutoModAutoRaidComponentsPage();
    expect(await autoModAutoRaidComponentsPage.getTitle().getText()).to.match(/Auto Mod Auto Raids/);
  });

  it('should load create AutoModAutoRaid page', async () => {
    await autoModAutoRaidComponentsPage.clickOnCreateButton();
    autoModAutoRaidUpdatePage = new AutoModAutoRaidUpdatePage();
    expect(await autoModAutoRaidUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botAutoModAutoRaid.home.createOrEditLabel/);
    await autoModAutoRaidUpdatePage.cancel();
  });

  it('should create and save AutoModAutoRaids', async () => {
    async function createAutoModAutoRaid() {
      await autoModAutoRaidComponentsPage.clickOnCreateButton();
      const selectedAutoRaidEnabled = await autoModAutoRaidUpdatePage.getAutoRaidEnabledInput().isSelected();
      if (selectedAutoRaidEnabled) {
        await autoModAutoRaidUpdatePage.getAutoRaidEnabledInput().click();
        expect(await autoModAutoRaidUpdatePage.getAutoRaidEnabledInput().isSelected()).to.be.false;
      } else {
        await autoModAutoRaidUpdatePage.getAutoRaidEnabledInput().click();
        expect(await autoModAutoRaidUpdatePage.getAutoRaidEnabledInput().isSelected()).to.be.true;
      }
      await autoModAutoRaidUpdatePage.setAutoRaidTimeThresholdInput('5');
      expect(await autoModAutoRaidUpdatePage.getAutoRaidTimeThresholdInput()).to.eq('5');
      await waitUntilDisplayed(autoModAutoRaidUpdatePage.getSaveButton());
      await autoModAutoRaidUpdatePage.save();
      await waitUntilHidden(autoModAutoRaidUpdatePage.getSaveButton());
      expect(await autoModAutoRaidUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createAutoModAutoRaid();
    await autoModAutoRaidComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await autoModAutoRaidComponentsPage.countDeleteButtons();
    await createAutoModAutoRaid();

    await autoModAutoRaidComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await autoModAutoRaidComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last AutoModAutoRaid', async () => {
    await autoModAutoRaidComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await autoModAutoRaidComponentsPage.countDeleteButtons();
    await autoModAutoRaidComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    autoModAutoRaidDeleteDialog = new AutoModAutoRaidDeleteDialog();
    expect(await autoModAutoRaidDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botAutoModAutoRaid.delete.question/);
    await autoModAutoRaidDeleteDialog.clickOnConfirmButton();

    await autoModAutoRaidComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await autoModAutoRaidComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
