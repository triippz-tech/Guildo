import { element, by, ElementFinder } from 'protractor';

export default class GuildApplicationUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildApplication.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  characterNameInput: ElementFinder = element(by.css('input#guild-application-characterName'));
  characterTypeInput: ElementFinder = element(by.css('input#guild-application-characterType'));
  applicationFileInput: ElementFinder = element(by.css('input#file_applicationFile'));
  statusSelect: ElementFinder = element(by.css('select#guild-application-status'));
  acceptedBySelect: ElementFinder = element(by.css('select#guild-application-acceptedBy'));
  appliedUserSelect: ElementFinder = element(by.css('select#guild-application-appliedUser'));
  guildServerSelect: ElementFinder = element(by.css('select#guild-application-guildServer'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setCharacterNameInput(characterName) {
    await this.characterNameInput.sendKeys(characterName);
  }

  async getCharacterNameInput() {
    return this.characterNameInput.getAttribute('value');
  }

  async setCharacterTypeInput(characterType) {
    await this.characterTypeInput.sendKeys(characterType);
  }

  async getCharacterTypeInput() {
    return this.characterTypeInput.getAttribute('value');
  }

  async setApplicationFileInput(applicationFile) {
    await this.applicationFileInput.sendKeys(applicationFile);
  }

  async getApplicationFileInput() {
    return this.applicationFileInput.getAttribute('value');
  }

  async setStatusSelect(status) {
    await this.statusSelect.sendKeys(status);
  }

  async getStatusSelect() {
    return this.statusSelect.element(by.css('option:checked')).getText();
  }

  async statusSelectLastOption() {
    await this.statusSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  async acceptedBySelectLastOption() {
    await this.acceptedBySelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async acceptedBySelectOption(option) {
    await this.acceptedBySelect.sendKeys(option);
  }

  getAcceptedBySelect() {
    return this.acceptedBySelect;
  }

  async getAcceptedBySelectedOption() {
    return this.acceptedBySelect.element(by.css('option:checked')).getText();
  }

  async appliedUserSelectLastOption() {
    await this.appliedUserSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async appliedUserSelectOption(option) {
    await this.appliedUserSelect.sendKeys(option);
  }

  getAppliedUserSelect() {
    return this.appliedUserSelect;
  }

  async getAppliedUserSelectedOption() {
    return this.appliedUserSelect.element(by.css('option:checked')).getText();
  }

  async guildServerSelectLastOption() {
    await this.guildServerSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async guildServerSelectOption(option) {
    await this.guildServerSelect.sendKeys(option);
  }

  getGuildServerSelect() {
    return this.guildServerSelect;
  }

  async getGuildServerSelectedOption() {
    return this.guildServerSelect.element(by.css('option:checked')).getText();
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
