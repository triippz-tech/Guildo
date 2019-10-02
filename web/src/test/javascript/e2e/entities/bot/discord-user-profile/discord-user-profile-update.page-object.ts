import { element, by, ElementFinder } from 'protractor';

export default class DiscordUserProfileUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botDiscordUserProfile.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  favoriteGameInput: ElementFinder = element(by.css('input#discord-user-profile-favoriteGame'));
  profilePhotoInput: ElementFinder = element(by.css('input#discord-user-profile-profilePhoto'));
  twitterUrlInput: ElementFinder = element(by.css('input#discord-user-profile-twitterUrl'));
  twitchUrlInput: ElementFinder = element(by.css('input#discord-user-profile-twitchUrl'));
  youtubeUrlInput: ElementFinder = element(by.css('input#discord-user-profile-youtubeUrl'));
  facebookUrlInput: ElementFinder = element(by.css('input#discord-user-profile-facebookUrl'));
  hitboxUrlInput: ElementFinder = element(by.css('input#discord-user-profile-hitboxUrl'));
  beamUrlInput: ElementFinder = element(by.css('input#discord-user-profile-beamUrl'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setFavoriteGameInput(favoriteGame) {
    await this.favoriteGameInput.sendKeys(favoriteGame);
  }

  async getFavoriteGameInput() {
    return this.favoriteGameInput.getAttribute('value');
  }

  async setProfilePhotoInput(profilePhoto) {
    await this.profilePhotoInput.sendKeys(profilePhoto);
  }

  async getProfilePhotoInput() {
    return this.profilePhotoInput.getAttribute('value');
  }

  async setTwitterUrlInput(twitterUrl) {
    await this.twitterUrlInput.sendKeys(twitterUrl);
  }

  async getTwitterUrlInput() {
    return this.twitterUrlInput.getAttribute('value');
  }

  async setTwitchUrlInput(twitchUrl) {
    await this.twitchUrlInput.sendKeys(twitchUrl);
  }

  async getTwitchUrlInput() {
    return this.twitchUrlInput.getAttribute('value');
  }

  async setYoutubeUrlInput(youtubeUrl) {
    await this.youtubeUrlInput.sendKeys(youtubeUrl);
  }

  async getYoutubeUrlInput() {
    return this.youtubeUrlInput.getAttribute('value');
  }

  async setFacebookUrlInput(facebookUrl) {
    await this.facebookUrlInput.sendKeys(facebookUrl);
  }

  async getFacebookUrlInput() {
    return this.facebookUrlInput.getAttribute('value');
  }

  async setHitboxUrlInput(hitboxUrl) {
    await this.hitboxUrlInput.sendKeys(hitboxUrl);
  }

  async getHitboxUrlInput() {
    return this.hitboxUrlInput.getAttribute('value');
  }

  async setBeamUrlInput(beamUrl) {
    await this.beamUrlInput.sendKeys(beamUrl);
  }

  async getBeamUrlInput() {
    return this.beamUrlInput.getAttribute('value');
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
