import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ScheduledAnnouncementComponentsPage, { ScheduledAnnouncementDeleteDialog } from './scheduled-announcement.page-object';
import ScheduledAnnouncementUpdatePage from './scheduled-announcement-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('ScheduledAnnouncement e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let scheduledAnnouncementUpdatePage: ScheduledAnnouncementUpdatePage;
  let scheduledAnnouncementComponentsPage: ScheduledAnnouncementComponentsPage;
  let scheduledAnnouncementDeleteDialog: ScheduledAnnouncementDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load ScheduledAnnouncements', async () => {
    await navBarPage.getEntityPage('scheduled-announcement');
    scheduledAnnouncementComponentsPage = new ScheduledAnnouncementComponentsPage();
    expect(await scheduledAnnouncementComponentsPage.getTitle().getText()).to.match(/Scheduled Announcements/);
  });

  it('should load create ScheduledAnnouncement page', async () => {
    await scheduledAnnouncementComponentsPage.clickOnCreateButton();
    scheduledAnnouncementUpdatePage = new ScheduledAnnouncementUpdatePage();
    expect(await scheduledAnnouncementUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botScheduledAnnouncement.home.createOrEditLabel/
    );
    await scheduledAnnouncementUpdatePage.cancel();
  });

  it('should create and save ScheduledAnnouncements', async () => {
    async function createScheduledAnnouncement() {
      await scheduledAnnouncementComponentsPage.clickOnCreateButton();
      await scheduledAnnouncementUpdatePage.setAnnoucementTitleInput('annoucementTitle');
      expect(await scheduledAnnouncementUpdatePage.getAnnoucementTitleInput()).to.match(/annoucementTitle/);
      await scheduledAnnouncementUpdatePage.setAnnoucementImgUrlInput('annoucementImgUrl');
      expect(await scheduledAnnouncementUpdatePage.getAnnoucementImgUrlInput()).to.match(/annoucementImgUrl/);
      await scheduledAnnouncementUpdatePage.setAnnoucementMessageInput('annoucementMessage');
      expect(await scheduledAnnouncementUpdatePage.getAnnoucementMessageInput()).to.match(/annoucementMessage/);
      await scheduledAnnouncementUpdatePage.setAnnoucementFireInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
      expect(await scheduledAnnouncementUpdatePage.getAnnoucementFireInput()).to.contain('2001-01-01T02:30');
      await scheduledAnnouncementUpdatePage.setGuildIdInput('5');
      expect(await scheduledAnnouncementUpdatePage.getGuildIdInput()).to.eq('5');
      await scheduledAnnouncementUpdatePage.annouceGuildSelectLastOption();
      await waitUntilDisplayed(scheduledAnnouncementUpdatePage.getSaveButton());
      await scheduledAnnouncementUpdatePage.save();
      await waitUntilHidden(scheduledAnnouncementUpdatePage.getSaveButton());
      expect(await scheduledAnnouncementUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createScheduledAnnouncement();
    await scheduledAnnouncementComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await scheduledAnnouncementComponentsPage.countDeleteButtons();
    await createScheduledAnnouncement();

    await scheduledAnnouncementComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await scheduledAnnouncementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ScheduledAnnouncement', async () => {
    await scheduledAnnouncementComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await scheduledAnnouncementComponentsPage.countDeleteButtons();
    await scheduledAnnouncementComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    scheduledAnnouncementDeleteDialog = new ScheduledAnnouncementDeleteDialog();
    expect(await scheduledAnnouncementDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /webApp.botScheduledAnnouncement.delete.question/
    );
    await scheduledAnnouncementDeleteDialog.clickOnConfirmButton();

    await scheduledAnnouncementComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await scheduledAnnouncementComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
