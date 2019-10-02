import { element, by, ElementFinder } from 'protractor';

export default class GuildPollUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildPoll.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  pollNameInput: ElementFinder = element(by.css('input#guild-poll-pollName'));
  descriptionInput: ElementFinder = element(by.css('input#guild-poll-description'));
  textChannelIdInput: ElementFinder = element(by.css('input#guild-poll-textChannelId'));
  finishTimeInput: ElementFinder = element(by.css('input#guild-poll-finishTime'));
  completedInput: ElementFinder = element(by.css('input#guild-poll-completed'));
  guildIdInput: ElementFinder = element(by.css('input#guild-poll-guildId'));
  pollItemsSelect: ElementFinder = element(by.css('select#guild-poll-pollItems'));
  pollServerSelect: ElementFinder = element(by.css('select#guild-poll-pollServer'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setPollNameInput(pollName) {
    await this.pollNameInput.sendKeys(pollName);
  }

  async getPollNameInput() {
    return this.pollNameInput.getAttribute('value');
  }

  async setDescriptionInput(description) {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput() {
    return this.descriptionInput.getAttribute('value');
  }

  async setTextChannelIdInput(textChannelId) {
    await this.textChannelIdInput.sendKeys(textChannelId);
  }

  async getTextChannelIdInput() {
    return this.textChannelIdInput.getAttribute('value');
  }

  async setFinishTimeInput(finishTime) {
    await this.finishTimeInput.sendKeys(finishTime);
  }

  async getFinishTimeInput() {
    return this.finishTimeInput.getAttribute('value');
  }

  getCompletedInput() {
    return this.completedInput;
  }
  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async pollItemsSelectLastOption() {
    await this.pollItemsSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async pollItemsSelectOption(option) {
    await this.pollItemsSelect.sendKeys(option);
  }

  getPollItemsSelect() {
    return this.pollItemsSelect;
  }

  async getPollItemsSelectedOption() {
    return this.pollItemsSelect.element(by.css('option:checked')).getText();
  }

  async pollServerSelectLastOption() {
    await this.pollServerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async pollServerSelectOption(option) {
    await this.pollServerSelect.sendKeys(option);
  }

  getPollServerSelect() {
    return this.pollServerSelect;
  }

  async getPollServerSelectedOption() {
    return this.pollServerSelect.element(by.css('option:checked')).getText();
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
