import { element, by, ElementFinder } from 'protractor';

export default class PunishmentUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botPunishment.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  maxStrikesInput: ElementFinder = element(by.css('input#punishment-maxStrikes'));
  actionSelect: ElementFinder = element(by.css('select#punishment-action'));
  punishmentDurationInput: ElementFinder = element(by.css('input#punishment-punishmentDuration'));
  guildIdInput: ElementFinder = element(by.css('input#punishment-guildId'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setMaxStrikesInput(maxStrikes) {
    await this.maxStrikesInput.sendKeys(maxStrikes);
  }

  async getMaxStrikesInput() {
    return this.maxStrikesInput.getAttribute('value');
  }

  async setActionSelect(action) {
    await this.actionSelect.sendKeys(action);
  }

  async getActionSelect() {
    return this.actionSelect.element(by.css('option:checked')).getText();
  }

  async actionSelectLastOption() {
    await this.actionSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async setPunishmentDurationInput(punishmentDuration) {
    await this.punishmentDurationInput.sendKeys(punishmentDuration);
  }

  async getPunishmentDurationInput() {
    return this.punishmentDurationInput.getAttribute('value');
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
