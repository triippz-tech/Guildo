import { element, by, ElementFinder } from 'protractor';

export default class GuildServerSettingsUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildServerSettings.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  prefixInput: ElementFinder = element(by.css('input#guild-server-settings-prefix'));
  timezoneInput: ElementFinder = element(by.css('input#guild-server-settings-timezone'));
  raidModeEnabledInput: ElementFinder = element(by.css('input#guild-server-settings-raidModeEnabled'));
  raidModeReasonInput: ElementFinder = element(by.css('input#guild-server-settings-raidModeReason'));
  maxStrikesInput: ElementFinder = element(by.css('input#guild-server-settings-maxStrikes'));
  acceptingApplicationsInput: ElementFinder = element(by.css('input#guild-server-settings-acceptingApplications'));
  autoModConfigSelect: ElementFinder = element(by.css('select#guild-server-settings-autoModConfig'));
  punishmentConfigSelect: ElementFinder = element(by.css('select#guild-server-settings-punishmentConfig'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setPrefixInput(prefix) {
    await this.prefixInput.sendKeys(prefix);
  }

  async getPrefixInput() {
    return this.prefixInput.getAttribute('value');
  }

  async setTimezoneInput(timezone) {
    await this.timezoneInput.sendKeys(timezone);
  }

  async getTimezoneInput() {
    return this.timezoneInput.getAttribute('value');
  }

  getRaidModeEnabledInput() {
    return this.raidModeEnabledInput;
  }
  async setRaidModeReasonInput(raidModeReason) {
    await this.raidModeReasonInput.sendKeys(raidModeReason);
  }

  async getRaidModeReasonInput() {
    return this.raidModeReasonInput.getAttribute('value');
  }

  async setMaxStrikesInput(maxStrikes) {
    await this.maxStrikesInput.sendKeys(maxStrikes);
  }

  async getMaxStrikesInput() {
    return this.maxStrikesInput.getAttribute('value');
  }

  getAcceptingApplicationsInput() {
    return this.acceptingApplicationsInput;
  }
  async autoModConfigSelectLastOption() {
    await this.autoModConfigSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async autoModConfigSelectOption(option) {
    await this.autoModConfigSelect.sendKeys(option);
  }

  getAutoModConfigSelect() {
    return this.autoModConfigSelect;
  }

  async getAutoModConfigSelectedOption() {
    return this.autoModConfigSelect.element(by.css('option:checked')).getText();
  }

  async punishmentConfigSelectLastOption() {
    await this.punishmentConfigSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async punishmentConfigSelectOption(option) {
    await this.punishmentConfigSelect.sendKeys(option);
  }

  getPunishmentConfigSelect() {
    return this.punishmentConfigSelect;
  }

  async getPunishmentConfigSelectedOption() {
    return this.punishmentConfigSelect.element(by.css('option:checked')).getText();
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
