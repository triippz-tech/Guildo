import { element, by, ElementFinder } from 'protractor';

export default class MuteUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botMute.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  reasonInput: ElementFinder = element(by.css('input#mute-reason'));
  endTimeInput: ElementFinder = element(by.css('input#mute-endTime'));
  guildIdInput: ElementFinder = element(by.css('input#mute-guildId'));
  userIdInput: ElementFinder = element(by.css('input#mute-userId'));
  mutedUserSelect: ElementFinder = element(by.css('select#mute-mutedUser'));
  mutedGuildServerSelect: ElementFinder = element(by.css('select#mute-mutedGuildServer'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setReasonInput(reason) {
    await this.reasonInput.sendKeys(reason);
  }

  async getReasonInput() {
    return this.reasonInput.getAttribute('value');
  }

  async setEndTimeInput(endTime) {
    await this.endTimeInput.sendKeys(endTime);
  }

  async getEndTimeInput() {
    return this.endTimeInput.getAttribute('value');
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async setUserIdInput(userId) {
    await this.userIdInput.sendKeys(userId);
  }

  async getUserIdInput() {
    return this.userIdInput.getAttribute('value');
  }

  async mutedUserSelectLastOption() {
    await this.mutedUserSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async mutedUserSelectOption(option) {
    await this.mutedUserSelect.sendKeys(option);
  }

  getMutedUserSelect() {
    return this.mutedUserSelect;
  }

  async getMutedUserSelectedOption() {
    return this.mutedUserSelect.element(by.css('option:checked')).getText();
  }

  async mutedGuildServerSelectLastOption() {
    await this.mutedGuildServerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async mutedGuildServerSelectOption(option) {
    await this.mutedGuildServerSelect.sendKeys(option);
  }

  getMutedGuildServerSelect() {
    return this.mutedGuildServerSelect;
  }

  async getMutedGuildServerSelectedOption() {
    return this.mutedGuildServerSelect.element(by.css('option:checked')).getText();
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
