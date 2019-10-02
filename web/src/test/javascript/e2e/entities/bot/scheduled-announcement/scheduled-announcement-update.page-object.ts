import { element, by, ElementFinder } from 'protractor';

export default class ScheduledAnnouncementUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botScheduledAnnouncement.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  annoucementTitleInput: ElementFinder = element(by.css('input#scheduled-announcement-annoucementTitle'));
  annoucementImgUrlInput: ElementFinder = element(by.css('input#scheduled-announcement-annoucementImgUrl'));
  annoucementMessageInput: ElementFinder = element(by.css('textarea#scheduled-announcement-annoucementMessage'));
  annoucementFireInput: ElementFinder = element(by.css('input#scheduled-announcement-annoucementFire'));
  guildIdInput: ElementFinder = element(by.css('input#scheduled-announcement-guildId'));
  annouceGuildSelect: ElementFinder = element(by.css('select#scheduled-announcement-annouceGuild'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setAnnoucementTitleInput(annoucementTitle) {
    await this.annoucementTitleInput.sendKeys(annoucementTitle);
  }

  async getAnnoucementTitleInput() {
    return this.annoucementTitleInput.getAttribute('value');
  }

  async setAnnoucementImgUrlInput(annoucementImgUrl) {
    await this.annoucementImgUrlInput.sendKeys(annoucementImgUrl);
  }

  async getAnnoucementImgUrlInput() {
    return this.annoucementImgUrlInput.getAttribute('value');
  }

  async setAnnoucementMessageInput(annoucementMessage) {
    await this.annoucementMessageInput.sendKeys(annoucementMessage);
  }

  async getAnnoucementMessageInput() {
    return this.annoucementMessageInput.getAttribute('value');
  }

  async setAnnoucementFireInput(annoucementFire) {
    await this.annoucementFireInput.sendKeys(annoucementFire);
  }

  async getAnnoucementFireInput() {
    return this.annoucementFireInput.getAttribute('value');
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async annouceGuildSelectLastOption() {
    await this.annouceGuildSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async annouceGuildSelectOption(option) {
    await this.annouceGuildSelect.sendKeys(option);
  }

  getAnnouceGuildSelect() {
    return this.annouceGuildSelect;
  }

  async getAnnouceGuildSelectedOption() {
    return this.annouceGuildSelect.element(by.css('option:checked')).getText();
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
