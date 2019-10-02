import { element, by, ElementFinder } from 'protractor';

export default class AutoModMentionsUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botAutoModMentions.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  maxMentionsInput: ElementFinder = element(by.css('input#auto-mod-mentions-maxMentions'));
  maxMsgLinesInput: ElementFinder = element(by.css('input#auto-mod-mentions-maxMsgLines'));
  maxRoleMentionsInput: ElementFinder = element(by.css('input#auto-mod-mentions-maxRoleMentions'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setMaxMentionsInput(maxMentions) {
    await this.maxMentionsInput.sendKeys(maxMentions);
  }

  async getMaxMentionsInput() {
    return this.maxMentionsInput.getAttribute('value');
  }

  async setMaxMsgLinesInput(maxMsgLines) {
    await this.maxMsgLinesInput.sendKeys(maxMsgLines);
  }

  async getMaxMsgLinesInput() {
    return this.maxMsgLinesInput.getAttribute('value');
  }

  async setMaxRoleMentionsInput(maxRoleMentions) {
    await this.maxRoleMentionsInput.sendKeys(maxRoleMentions);
  }

  async getMaxRoleMentionsInput() {
    return this.maxRoleMentionsInput.getAttribute('value');
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
