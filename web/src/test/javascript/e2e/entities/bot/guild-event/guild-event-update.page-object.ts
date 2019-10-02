import { element, by, ElementFinder } from 'protractor';

export default class GuildEventUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildEvent.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  eventNameInput: ElementFinder = element(by.css('input#guild-event-eventName'));
  eventImageUrlInput: ElementFinder = element(by.css('input#guild-event-eventImageUrl'));
  eventMessageInput: ElementFinder = element(by.css('textarea#guild-event-eventMessage'));
  eventStartInput: ElementFinder = element(by.css('input#guild-event-eventStart'));
  guildIdInput: ElementFinder = element(by.css('input#guild-event-guildId'));
  eventGuildSelect: ElementFinder = element(by.css('select#guild-event-eventGuild'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setEventNameInput(eventName) {
    await this.eventNameInput.sendKeys(eventName);
  }

  async getEventNameInput() {
    return this.eventNameInput.getAttribute('value');
  }

  async setEventImageUrlInput(eventImageUrl) {
    await this.eventImageUrlInput.sendKeys(eventImageUrl);
  }

  async getEventImageUrlInput() {
    return this.eventImageUrlInput.getAttribute('value');
  }

  async setEventMessageInput(eventMessage) {
    await this.eventMessageInput.sendKeys(eventMessage);
  }

  async getEventMessageInput() {
    return this.eventMessageInput.getAttribute('value');
  }

  async setEventStartInput(eventStart) {
    await this.eventStartInput.sendKeys(eventStart);
  }

  async getEventStartInput() {
    return this.eventStartInput.getAttribute('value');
  }

  async setGuildIdInput(guildId) {
    await this.guildIdInput.sendKeys(guildId);
  }

  async getGuildIdInput() {
    return this.guildIdInput.getAttribute('value');
  }

  async eventGuildSelectLastOption() {
    await this.eventGuildSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async eventGuildSelectOption(option) {
    await this.eventGuildSelect.sendKeys(option);
  }

  getEventGuildSelect() {
    return this.eventGuildSelect;
  }

  async getEventGuildSelectedOption() {
    return this.eventGuildSelect.element(by.css('option:checked')).getText();
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
