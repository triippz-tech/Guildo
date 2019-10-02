import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import GiveAwayComponentsPage, { GiveAwayDeleteDialog } from './give-away.page-object';
import GiveAwayUpdatePage from './give-away-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('GiveAway e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let giveAwayUpdatePage: GiveAwayUpdatePage;
  let giveAwayComponentsPage: GiveAwayComponentsPage;
  let giveAwayDeleteDialog: GiveAwayDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load GiveAways', async () => {
    await navBarPage.getEntityPage('give-away');
    giveAwayComponentsPage = new GiveAwayComponentsPage();
    expect(await giveAwayComponentsPage.getTitle().getText()).to.match(/Give Aways/);
  });

  it('should load create GiveAway page', async () => {
    await giveAwayComponentsPage.clickOnCreateButton();
    giveAwayUpdatePage = new GiveAwayUpdatePage();
    expect(await giveAwayUpdatePage.getPageTitle().getAttribute('id')).to.match(/webApp.botGiveAway.home.createOrEditLabel/);
    await giveAwayUpdatePage.cancel();
  });

  it('should create and save GiveAways', async () => {
    async function createGiveAway() {
      await giveAwayComponentsPage.clickOnCreateButton();
      await giveAwayUpdatePage.setNameInput('name');
      expect(await giveAwayUpdatePage.getNameInput()).to.match(/name/);
      await giveAwayUpdatePage.setImageInput('image');
      expect(await giveAwayUpdatePage.getImageInput()).to.match(/image/);
      await giveAwayUpdatePage.setMessageInput('message');
      expect(await giveAwayUpdatePage.getMessageInput()).to.match(/message/);
      await giveAwayUpdatePage.setMessageIdInput('5');
      expect(await giveAwayUpdatePage.getMessageIdInput()).to.eq('5');
      await giveAwayUpdatePage.setTextChannelIdInput('5');
      expect(await giveAwayUpdatePage.getTextChannelIdInput()).to.eq('5');
      await giveAwayUpdatePage.setFinishInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await giveAwayUpdatePage.getFinishInput()).to.contain('2001-01-01T02:30');
      const selectedExpired = await giveAwayUpdatePage.getExpiredInput().isSelected();
      if (selectedExpired) {
        await giveAwayUpdatePage.getExpiredInput().click();
        expect(await giveAwayUpdatePage.getExpiredInput().isSelected()).to.be.false;
      } else {
        await giveAwayUpdatePage.getExpiredInput().click();
        expect(await giveAwayUpdatePage.getExpiredInput().isSelected()).to.be.true;
      }
      await giveAwayUpdatePage.setGuildIdInput('5');
      expect(await giveAwayUpdatePage.getGuildIdInput()).to.eq('5');
      await giveAwayUpdatePage.winnerSelectLastOption();
      await giveAwayUpdatePage.guildGiveAwaySelectLastOption();
      await waitUntilDisplayed(giveAwayUpdatePage.getSaveButton());
      await giveAwayUpdatePage.save();
      await waitUntilHidden(giveAwayUpdatePage.getSaveButton());
      expect(await giveAwayUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createGiveAway();
    await giveAwayComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await giveAwayComponentsPage.countDeleteButtons();
    await createGiveAway();

    await giveAwayComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await giveAwayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last GiveAway', async () => {
    await giveAwayComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await giveAwayComponentsPage.countDeleteButtons();
    await giveAwayComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    giveAwayDeleteDialog = new GiveAwayDeleteDialog();
    expect(await giveAwayDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/webApp.botGiveAway.delete.question/);
    await giveAwayDeleteDialog.clickOnConfirmButton();

    await giveAwayComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await giveAwayComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
