import { element, by, ElementFinder } from 'protractor';

export default class AutoModAntiDupUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botAutoModAntiDup.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  deleteThresholdInput: ElementFinder = element(by.css('input#auto-mod-anti-dup-deleteThreshold'));
  dupsToPunishInput: ElementFinder = element(by.css('input#auto-mod-anti-dup-dupsToPunish'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setDeleteThresholdInput(deleteThreshold) {
    await this.deleteThresholdInput.sendKeys(deleteThreshold);
  }

  async getDeleteThresholdInput() {
    return this.deleteThresholdInput.getAttribute('value');
  }

  async setDupsToPunishInput(dupsToPunish) {
    await this.dupsToPunishInput.sendKeys(dupsToPunish);
  }

  async getDupsToPunishInput() {
    return this.dupsToPunishInput.getAttribute('value');
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
