import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import DiscordUserProfileComponentsPage, { DiscordUserProfileDeleteDialog } from './discord-user-profile.page-object';
import DiscordUserProfileUpdatePage from './discord-user-profile-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../../util/utils';

const expect = chai.expect;

describe('DiscordUserProfile e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let discordUserProfileUpdatePage: DiscordUserProfileUpdatePage;
  let discordUserProfileComponentsPage: DiscordUserProfileComponentsPage;
  let discordUserProfileDeleteDialog: DiscordUserProfileDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth('admin', 'admin');
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load DiscordUserProfiles', async () => {
    await navBarPage.getEntityPage('discord-user-profile');
    discordUserProfileComponentsPage = new DiscordUserProfileComponentsPage();
    expect(await discordUserProfileComponentsPage.getTitle().getText()).to.match(/Discord User Profiles/);
  });

  it('should load create DiscordUserProfile page', async () => {
    await discordUserProfileComponentsPage.clickOnCreateButton();
    discordUserProfileUpdatePage = new DiscordUserProfileUpdatePage();
    expect(await discordUserProfileUpdatePage.getPageTitle().getAttribute('id')).to.match(
      /webApp.botDiscordUserProfile.home.createOrEditLabel/
    );
    await discordUserProfileUpdatePage.cancel();
  });

  it('should create and save DiscordUserProfiles', async () => {
    async function createDiscordUserProfile() {
      await discordUserProfileComponentsPage.clickOnCreateButton();
      await discordUserProfileUpdatePage.setFavoriteGameInput('favoriteGame');
      expect(await discordUserProfileUpdatePage.getFavoriteGameInput()).to.match(/favoriteGame/);
      await discordUserProfileUpdatePage.setProfilePhotoInput('profilePhoto');
      expect(await discordUserProfileUpdatePage.getProfilePhotoInput()).to.match(/profilePhoto/);
      await discordUserProfileUpdatePage.setTwitterUrlInput('twitterUrl');
      expect(await discordUserProfileUpdatePage.getTwitterUrlInput()).to.match(/twitterUrl/);
      await discordUserProfileUpdatePage.setTwitchUrlInput('twitchUrl');
      expect(await discordUserProfileUpdatePage.getTwitchUrlInput()).to.match(/twitchUrl/);
      await discordUserProfileUpdatePage.setYoutubeUrlInput('youtubeUrl');
      expect(await discordUserProfileUpdatePage.getYoutubeUrlInput()).to.match(/youtubeUrl/);
      await discordUserProfileUpdatePage.setFacebookUrlInput('facebookUrl');
      expect(await discordUserProfileUpdatePage.getFacebookUrlInput()).to.match(/facebookUrl/);
      await discordUserProfileUpdatePage.setHitboxUrlInput('hitboxUrl');
      expect(await discordUserProfileUpdatePage.getHitboxUrlInput()).to.match(/hitboxUrl/);
      await discordUserProfileUpdatePage.setBeamUrlInput('beamUrl');
      expect(await discordUserProfileUpdatePage.getBeamUrlInput()).to.match(/beamUrl/);
      await waitUntilDisplayed(discordUserProfileUpdatePage.getSaveButton());
      await discordUserProfileUpdatePage.save();
      await waitUntilHidden(discordUserProfileUpdatePage.getSaveButton());
      expect(await discordUserProfileUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createDiscordUserProfile();
    await discordUserProfileComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await discordUserProfileComponentsPage.countDeleteButtons();
    await createDiscordUserProfile();

    await discordUserProfileComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await discordUserProfileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last DiscordUserProfile', async () => {
    await discordUserProfileComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await discordUserProfileComponentsPage.countDeleteButtons();
    await discordUserProfileComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    discordUserProfileDeleteDialog = new DiscordUserProfileDeleteDialog();
    expect(await discordUserProfileDeleteDialog.getDialogTitle().getAttribute('id')).to.match(
      /webApp.botDiscordUserProfile.delete.question/
    );
    await discordUserProfileDeleteDialog.clickOnConfirmButton();

    await discordUserProfileComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await discordUserProfileComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
