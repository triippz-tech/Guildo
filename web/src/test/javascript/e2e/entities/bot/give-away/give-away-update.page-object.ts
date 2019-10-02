import { element, by, ElementFinder } from 'protractor';

export default class GiveAwayUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGiveAway.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#give-away-name'));
  imageInput: ElementFinder = element(by.css('input#give-away-image'));
  messageInput: ElementFinder = element(by.css('textarea#give-away-message'));
  messageIdInput: ElementFinder = element(by.css('input#give-away-messageId'));
  textChannelIdInput: ElementFinder = element(by.css('input#give-away-textChannelId'));
  finishInput: ElementFinder = element(by.css('input#give-away-finish'));
  expiredInput: ElementFinder = element(by.css('input#give-away-expired'));
  guildIdInput: ElementFinder = element(by.css('input#give-away-guildId'));
  winnerSelect: ElementFinder = element(by.css('select#give-away-winner'));
  guildGiveAwaySelect: ElementFinder = element(by.css('select#give-away-guildGiveAway'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setImageInput(image) {
    await this.imageInput.sendKeys(image);
  }

  async getImageInput() {
    return this.imageInput.getAttribute('value');
  }

  async setMessageInput(message) {
    await this.messageInput.sendKeys(message);
  }

  async getMessageInput() {
    return this.messageInput.getAttribute('value');
  }

  async setMessageIdInput(messageId) {
    await this.messageIdInput.sendKeys(messageId);
  }

  async getMessageIdInput() {
    return this.messageIdInput.getAttribute('value');
  }

  async setTextChannelIdInput(textChannelId) {
    await this.textChannelIdInput.sendKeys(textChannelId);
  }

  async getTextChannelIdInput() {
    return this.textChannelIdInput.getAttribute('value');
  }

  async setFinishInput(finish) {
    await this.finishInput.sendKeys(finish);
  }

  async getFinishInput() {
    return this.finishInput.getAttribute('value');
  }

  getExpiredInput() {
    return this.expiredInput;
  }
  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async winnerSelectLastOption() {
    await this.winnerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async winnerSelectOption(option) {
    await this.winnerSelect.sendKeys(option);
  }

  getWinnerSelect() {
    return this.winnerSelect;
  }

  async getWinnerSelectedOption() {
    return this.winnerSelect.element(by.css('option:checked')).getText();
  }

  async guildGiveAwaySelectLastOption() {
    await this.guildGiveAwaySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async guildGiveAwaySelectOption(option) {
    await this.guildGiveAwaySelect.sendKeys(option);
  }

  getGuildGiveAwaySelect() {
    return this.guildGiveAwaySelect;
  }

  async getGuildGiveAwaySelectedOption() {
    return this.guildGiveAwaySelect.element(by.css('option:checked')).getText();
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
