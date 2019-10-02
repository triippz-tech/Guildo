import { element, by, ElementFinder } from 'protractor';

export default class AutoModIgnoreUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botAutoModIgnore.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  roleIdInput: ElementFinder = element(by.css('input#auto-mod-ignore-roleId'));
  channelIdInput: ElementFinder = element(by.css('input#auto-mod-ignore-channelId'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setRoleIdInput(roleId) {
    await this.roleIdInput.sendKeys(roleId);
  }

  async getRoleIdInput() {
    return this.roleIdInput.getAttribute('value');
  }

  async setChannelIdInput(channelId) {
    await this.channelIdInput.sendKeys(channelId);
  }

  async getChannelIdInput() {
    return this.channelIdInput.getAttribute('value');
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
