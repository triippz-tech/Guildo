import { element, by, ElementFinder } from 'protractor';

export default class UserStrikeUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botUserStrike.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  countInput: ElementFinder = element(by.css('input#user-strike-count'));
  userIdInput: ElementFinder = element(by.css('input#user-strike-userId'));
  guildIdInput: ElementFinder = element(by.css('input#user-strike-guildId'));
  discordUserSelect: ElementFinder = element(by.css('select#user-strike-discordUser'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setCountInput(count) {
    await this.countInput.sendKeys(count);
  }

  async getCountInput() {
    return this.countInput.getAttribute('value');
  }

  async setUserIdInput(userId) {
    await this.userIdInput.sendKeys(userId);
  }

  async getUserIdInput() {
    return this.userIdInput.getAttribute('value');
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async discordUserSelectLastOption() {
    await this.discordUserSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async discordUserSelectOption(option) {
    await this.discordUserSelect.sendKeys(option);
  }

  getDiscordUserSelect() {
    return this.discordUserSelect;
  }

  async getDiscordUserSelectedOption() {
    return this.discordUserSelect.element(by.css('option:checked')).getText();
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
