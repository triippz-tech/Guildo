import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import PunishmentComponentsPage, { PunishmentDeleteDialog } from './punishment.page-object';
import PunishmentUpdatePage from './punishment-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('Punishment e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let punishmentUpdatePage: PunishmentUpdatePage;
  let punishmentComponentsPage: PunishmentComponentsPage;
  let punishmentDeleteDialog: PunishmentDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load Punishments', async () => {
    await navBarPage.getEntityPage('punishment');
    punishmentComponentsPage = new PunishmentComponentsPage();
    expect(await punishmentComponentsPage.getTitle().getText()).to.match(/Punishments/);
  });

  it('should load create Punishment page', async () => {
    await punishmentComponentsPage.clickOnCreateButton();
    punishmentUpdatePage = new PunishmentUpdatePage();
    expect(await punishmentUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botPunishment.home.createOrEditLabel/);
    await punishmentUpdatePage.cancel();
  });

  it('should create and save Punishments', async () => {
    async function createPunishment() {
      await punishmentComponentsPage.clickOnCreateButton();
      await punishmentUpdatePage.setMaxStrikesInput('5');
      expect(await punishmentUpdatePage.getMaxStrikesInput()).to.eq('5');
      await punishmentUpdatePage.actionSelectLastOption();
      await punishmentUpdatePage.setPunishmentDurationInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await punishmentUpdatePage.getPunishmentDurationInput()).to.contain('2001-01-01T02:30');
      await punishmentUpdatePage.setGuildIdInput('5');
      expect(await punishmentUpdatePage.getGuildIdInput()).to.eq('5');
      await waitUntilDisplayed(punishmentUpdatePage.getSaveButton());
      await punishmentUpdatePage.save();
      await waitUntilHidden(punishmentUpdatePage.getSaveButton());
      expect(await punishmentUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createPunishment();
    await punishmentComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await punishmentComponentsPage.countDeleteButtons();
    await createPunishment();

    await punishmentComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await punishmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Punishment', async () => {
    await punishmentComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await punishmentComponentsPage.countDeleteButtons();
    await punishmentComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    punishmentDeleteDialog = new PunishmentDeleteDialog();
    expect(await punishmentDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botPunishment.delete.question/);
    await punishmentDeleteDialog.clickOnConfirmButton();

    await punishmentComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await punishmentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
