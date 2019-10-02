import { element, by, ElementFinder } from 'protractor';

export default class TempBanUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botTempBan.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  reasonInput: ElementFinder = element(by.css('input#temp-ban-reason'));
  endTimeInput: ElementFinder = element(by.css('input#temp-ban-endTime'));
  guildIdInput: ElementFinder = element(by.css('input#temp-ban-guildId'));
  userIdInput: ElementFinder = element(by.css('input#temp-ban-userId'));
  bannedUserSelect: ElementFinder = element(by.css('select#temp-ban-bannedUser'));
  tempBanGuildServerSelect: ElementFinder = element(by.css('select#temp-ban-tempBanGuildServer'));

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

  async bannedUserSelectLastOption() {
    await this.bannedUserSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async bannedUserSelectOption(option) {
    await this.bannedUserSelect.sendKeys(option);
  }

  getBannedUserSelect() {
    return this.bannedUserSelect;
  }

  async getBannedUserSelectedOption() {
    return this.bannedUserSelect.element(by.css('option:checked')).getText();
  }

  async tempBanGuildServerSelectLastOption() {
    await this.tempBanGuildServerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async tempBanGuildServerSelectOption(option) {
    await this.tempBanGuildServerSelect.sendKeys(option);
  }

  getTempBanGuildServerSelect() {
    return this.tempBanGuildServerSelect;
  }

  async getTempBanGuildServerSelectedOption() {
    return this.tempBanGuildServerSelect.element(by.css('option:checked')).getText();
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
