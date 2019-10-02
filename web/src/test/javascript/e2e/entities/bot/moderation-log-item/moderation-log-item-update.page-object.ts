import { element, by, ElementFinder } from 'protractor';

export default class ModerationLogItemUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botModerationLogItem.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  channelIdInput: ElementFinder = element(by.css('input#moderation-log-item-channelId'));
  channelNameInput: ElementFinder = element(by.css('input#moderation-log-item-channelName'));
  issuedByIdInput: ElementFinder = element(by.css('input#moderation-log-item-issuedById'));
  issuedByNameInput: ElementFinder = element(by.css('input#moderation-log-item-issuedByName'));
  issuedToIdInput: ElementFinder = element(by.css('input#moderation-log-item-issuedToId'));
  issuedToNameInput: ElementFinder = element(by.css('input#moderation-log-item-issuedToName'));
  reasonInput: ElementFinder = element(by.css('input#moderation-log-item-reason'));
  timeInput: ElementFinder = element(by.css('input#moderation-log-item-time'));
  moderationActionSelect: ElementFinder = element(by.css('select#moderation-log-item-moderationAction'));
  guildIdInput: ElementFinder = element(by.css('input#moderation-log-item-guildId'));
  modItemGuildServerSelect: ElementFinder = element(by.css('select#moderation-log-item-modItemGuildServer'));

  getPageTitle() {
    return this.pageTitle;
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

  async setIssuedByIdInput(issuedById) {
    await this.issuedByIdInput.sendKeys(issuedById);
  }

  async getIssuedByIdInput() {
    return this.issuedByIdInput.getAttribute('value');
  }

  async setIssuedByNameInput(issuedByName) {
    await this.issuedByNameInput.sendKeys(issuedByName);
  }

  async getIssuedByNameInput() {
    return this.issuedByNameInput.getAttribute('value');
  }

  async setIssuedToIdInput(issuedToId) {
    await this.issuedToIdInput.sendKeys(issuedToId);
  }

  async getIssuedToIdInput() {
    return this.issuedToIdInput.getAttribute('value');
  }

  async setIssuedToNameInput(issuedToName) {
    await this.issuedToNameInput.sendKeys(issuedToName);
  }

  async getIssuedToNameInput() {
    return this.issuedToNameInput.getAttribute('value');
  }

  async setReasonInput(reason) {
    await this.reasonInput.sendKeys(reason);
  }

  async getReasonInput() {
    return this.reasonInput.getAttribute('value');
  }

  async setTimeInput(time) {
    await this.timeInput.sendKeys(time);
  }

  async getTimeInput() {
    return this.timeInput.getAttribute('value');
  }

  async setModerationActionSelect(moderationAction) {
    await this.moderationActionSelect.sendKeys(moderationAction);
  }

  async getModerationActionSelect() {
    return this.moderationActionSelect.element(by.css('option:checked')).getText();
  }

  async moderationActionSelectLastOption() {
    await this.moderationActionSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async modItemGuildServerSelectLastOption() {
    await this.modItemGuildServerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async modItemGuildServerSelectOption(option) {
    await this.modItemGuildServerSelect.sendKeys(option);
  }

  getModItemGuildServerSelect() {
    return this.modItemGuildServerSelect;
  }

  async getModItemGuildServerSelectedOption() {
    return this.modItemGuildServerSelect.element(by.css('option:checked')).getText();
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
