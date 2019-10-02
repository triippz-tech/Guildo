import { element, by, ElementFinder } from 'protractor';

export default class GuildServerUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildServer.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  guildIdInput: ElementFinder = element(by.css('input#guild-server-guildId'));
  guildNameInput: ElementFinder = element(by.css('input#guild-server-guildName'));
  iconInput: ElementFinder = element(by.css('input#guild-server-icon'));
  ownerInput: ElementFinder = element(by.css('input#guild-server-owner'));
  serverLevelSelect: ElementFinder = element(by.css('select#guild-server-serverLevel'));
  guildProfileSelect: ElementFinder = element(by.css('select#guild-server-guildProfile'));
  applicationFormSelect: ElementFinder = element(by.css('select#guild-server-applicationForm'));
  guildSettingsSelect: ElementFinder = element(by.css('select#guild-server-guildSettings'));
  welcomeMessageSelect: ElementFinder = element(by.css('select#guild-server-welcomeMessage'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async setGuildNameInput(guildName) {
    await this.guildNameInput.sendKeys(guildName);
  }

  async getGuildNameInput() {
    return this.guildNameInput.getAttribute('value');
  }

  async setIconInput(icon) {
    await this.iconInput.sendKeys(icon);
  }

  async getIconInput() {
    return this.iconInput.getAttribute('value');
  }

  async setOwnerInput(owner) {
    await this.ownerInput.sendKeys(owner);
  }

  async getOwnerInput() {
    return this.ownerInput.getAttribute('value');
  }

  async setServerLevelSelect(serverLevel) {
    await this.serverLevelSelect.sendKeys(serverLevel);
  }

  async getServerLevelSelect() {
    return this.serverLevelSelect.element(by.css('option:checked')).getText();
  }

  async serverLevelSelectLastOption() {
    await this.serverLevelSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async guildProfileSelectLastOption() {
    await this.guildProfileSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async guildProfileSelectOption(option) {
    await this.guildProfileSelect.sendKeys(option);
  }

  getGuildProfileSelect() {
    return this.guildProfileSelect;
  }

  async getGuildProfileSelectedOption() {
    return this.guildProfileSelect.element(by.css('option:checked')).getText();
  }

  async applicationFormSelectLastOption() {
    await this.applicationFormSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async applicationFormSelectOption(option) {
    await this.applicationFormSelect.sendKeys(option);
  }

  getApplicationFormSelect() {
    return this.applicationFormSelect;
  }

  async getApplicationFormSelectedOption() {
    return this.applicationFormSelect.element(by.css('option:checked')).getText();
  }

  async guildSettingsSelectLastOption() {
    await this.guildSettingsSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async guildSettingsSelectOption(option) {
    await this.guildSettingsSelect.sendKeys(option);
  }

  getGuildSettingsSelect() {
    return this.guildSettingsSelect;
  }

  async getGuildSettingsSelectedOption() {
    return this.guildSettingsSelect.element(by.css('option:checked')).getText();
  }

  async welcomeMessageSelectLastOption() {
    await this.welcomeMessageSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async welcomeMessageSelectOption(option) {
    await this.welcomeMessageSelect.sendKeys(option);
  }

  getWelcomeMessageSelect() {
    return this.welcomeMessageSelect;
  }

  async getWelcomeMessageSelectedOption() {
    return this.welcomeMessageSelect.element(by.css('option:checked')).getText();
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
