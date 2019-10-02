import { element, by, ElementFinder } from 'protractor';

export default class DiscordUserUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botDiscordUser.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  userIdInput: ElementFinder = element(by.css('input#discord-user-userId'));
  userNameInput: ElementFinder = element(by.css('input#discord-user-userName'));
  iconInput: ElementFinder = element(by.css('input#discord-user-icon'));
  commandsIssuedInput: ElementFinder = element(by.css('input#discord-user-commandsIssued'));
  blacklistedInput: ElementFinder = element(by.css('input#discord-user-blacklisted'));
  userLevelSelect: ElementFinder = element(by.css('select#discord-user-userLevel'));
  userProfileSelect: ElementFinder = element(by.css('select#discord-user-userProfile'));

  getPageTitle() {
    return this.pageTitle;
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

  async setIconInput(icon) {
    await this.iconInput.sendKeys(icon);
  }

  async getIconInput() {
    return this.iconInput.getAttribute('value');
  }

  async setCommandsIssuedInput(commandsIssued) {
    await this.commandsIssuedInput.sendKeys(commandsIssued);
  }

  async getCommandsIssuedInput() {
    return this.commandsIssuedInput.getAttribute('value');
  }

  getBlacklistedInput() {
    return this.blacklistedInput;
  }
  async setUserLevelSelect(userLevel) {
    await this.userLevelSelect.sendKeys(userLevel);
  }

  async getUserLevelSelect() {
    return this.userLevelSelect.element(by.css('option:checked')).getText();
  }

  async userLevelSelectLastOption() {
    await this.userLevelSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async userProfileSelectLastOption() {
    await this.userProfileSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async userProfileSelectOption(option) {
    await this.userProfileSelect.sendKeys(option);
  }

  getUserProfileSelect() {
    return this.userProfileSelect;
  }

  async getUserProfileSelectedOption() {
    return this.userProfileSelect.element(by.css('option:checked')).getText();
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
