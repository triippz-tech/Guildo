import { element, by, ElementFinder } from 'protractor';

export default class GuildServerProfileUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildServerProfile.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  guildTypeSelect: ElementFinder = element(by.css('select#guild-server-profile-guildType'));
  playStyleSelect: ElementFinder = element(by.css('select#guild-server-profile-playStyle'));
  descriptionInput: ElementFinder = element(by.css('input#guild-server-profile-description'));
  websiteInput: ElementFinder = element(by.css('input#guild-server-profile-website'));
  discordUrlInput: ElementFinder = element(by.css('input#guild-server-profile-discordUrl'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setGuildTypeSelect(guildType) {
    await this.guildTypeSelect.sendKeys(guildType);
  }

  async getGuildTypeSelect() {
    return this.guildTypeSelect.element(by.css('option:checked')).getText();
  }

  async guildTypeSelectLastOption() {
    await this.guildTypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setPlayStyleSelect(playStyle) {
    await this.playStyleSelect.sendKeys(playStyle);
  }

  async getPlayStyleSelect() {
    return this.playStyleSelect.element(by.css('option:checked')).getText();
  }

  async playStyleSelectLastOption() {
    await this.playStyleSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setWebsiteInput(website) {
    await this.websiteInput.sendKeys(website);
  }

  async getWebsiteInput() {
    return this.websiteInput.getAttribute('value');
  }

  async setDiscordUrlInput(discordUrl) {
    await this.discordUrlInput.sendKeys(discordUrl);
  }

  async getDiscordUrlInput() {
    return this.discordUrlInput.getAttribute('value');
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
