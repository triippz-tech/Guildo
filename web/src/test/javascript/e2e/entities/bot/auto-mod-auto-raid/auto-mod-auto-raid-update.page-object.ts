import { element, by, ElementFinder } from 'protractor';

export default class AutoModAutoRaidUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botAutoModAutoRaid.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  autoRaidEnabledInput: ElementFinder = element(by.css('input#auto-mod-auto-raid-autoRaidEnabled'));
  autoRaidTimeThresholdInput: ElementFinder = element(by.css('input#auto-mod-auto-raid-autoRaidTimeThreshold'));

  getPageTitle() {
    return this.pageTitle;
  }

  getAutoRaidEnabledInput() {
    return this.autoRaidEnabledInput;
  }
  async setAutoRaidTimeThresholdInput(autoRaidTimeThreshold) {
    await this.autoRaidTimeThresholdInput.sendKeys(autoRaidTimeThreshold);
  }

  async getAutoRaidTimeThresholdInput() {
    return this.autoRaidTimeThresholdInput.getAttribute('value');
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
