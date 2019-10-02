import { element, by, ElementFinder } from 'protractor';

export default class ServerLogItemUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botServerLogItem.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  activitySelect: ElementFinder = element(by.css('select#server-log-item-activity'));
  channelIdInput: ElementFinder = element(by.css('input#server-log-item-channelId'));
  channelNameInput: ElementFinder = element(by.css('input#server-log-item-channelName'));
  timeInput: ElementFinder = element(by.css('input#server-log-item-time'));
  userIdInput: ElementFinder = element(by.css('input#server-log-item-userId'));
  userNameInput: ElementFinder = element(by.css('input#server-log-item-userName'));
  guildIdInput: ElementFinder = element(by.css('input#server-log-item-guildId'));
  serverItemGuildServerSelect: ElementFinder = element(by.css('select#server-log-item-serverItemGuildServer'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setActivitySelect(activity) {
    await this.activitySelect.sendKeys(activity);
  }

  async getActivitySelect() {
    return this.activitySelect.element(by.css('option:checked')).getText();
  }

  async activitySelectLastOption() {
    await this.activitySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setChannelIdInput(channelId) {
    await this.channelIdInput.sendKeys(channelId);
  }

  async getChannelIdInput() {
    return this.channelIdInput.getAttribute('value');
  }

  async setChannelNameInput(channelName) {
    await this.channelNameInput.sendKeys(channelName);
  }

  async getChannelNameInput() {
    return this.channelNameInput.getAttribute('value');
  }

  async setTimeInput(time) {
    await this.timeInput.sendKeys(time);
  }

  async getTimeInput() {
    return this.timeInput.getAttribute('value');
  }

  async setUserIdInput(userId) {
    await this.userIdInput.sendKeys(userId);
  }

  async getUserIdInput() {
    return this.userIdInput.getAttribute('value');
  }

  async setUserNameInput(userName) {
    await this.userNameInput.sendKeys(userName);
  }

  async getUserNameInput() {
    return this.userNameInput.getAttribute('value');
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async serverItemGuildServerSelectLastOption() {
    await this.serverItemGuildServerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async serverItemGuildServerSelectOption(option) {
    await this.serverItemGuildServerSelect.sendKeys(option);
  }

  getServerItemGuildServerSelect() {
    return this.serverItemGuildServerSelect;
  }

  async getServerItemGuildServerSelectedOption() {
    return this.serverItemGuildServerSelect.element(by.css('option:checked')).getText();
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
