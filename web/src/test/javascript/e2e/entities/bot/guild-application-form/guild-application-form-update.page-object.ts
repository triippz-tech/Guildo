import { element, by, ElementFinder } from 'protractor';

export default class GuildApplicationFormUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildApplicationForm.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  applicationFormInput: ElementFinder = element(by.css('input#file_applicationForm'));
  guildIdInput: ElementFinder = element(by.css('input#guild-application-form-guildId'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setApplicationFormInput(applicationForm) {
    await this.applicationFormInput.sendKeys(applicationForm);
  }

  async getApplicationFormInput() {
    return this.applicationFormInput.getAttribute('value');
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
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
