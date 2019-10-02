import { element, by, ElementFinder } from 'protractor';

export default class WelcomeMessageUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botWelcomeMessage.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#welcome-message-name'));
  messageTitleInput: ElementFinder = element(by.css('input#welcome-message-messageTitle'));
  bodyInput: ElementFinder = element(by.css('input#welcome-message-body'));
  footerInput: ElementFinder = element(by.css('input#welcome-message-footer'));
  logoUrlInput: ElementFinder = element(by.css('input#welcome-message-logoUrl'));
  guildIdInput: ElementFinder = element(by.css('input#welcome-message-guildId'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setMessageTitleInput(messageTitle) {
    await this.messageTitleInput.sendKeys(messageTitle);
  }

  async getMessageTitleInput() {
    return this.messageTitleInput.getAttribute('value');
  }

  async setBodyInput(body) {
    await this.bodyInput.sendKeys(body);
  }

  async getBodyInput() {
    return this.bodyInput.getAttribute('value');
  }

  async setFooterInput(footer) {
    await this.footerInput.sendKeys(footer);
  }

  async getFooterInput() {
    return this.footerInput.getAttribute('value');
  }

  async setLogoUrlInput(logoUrl) {
    await this.logoUrlInput.sendKeys(logoUrl);
  }

  async getLogoUrlInput() {
    return this.logoUrlInput.getAttribute('value');
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
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
