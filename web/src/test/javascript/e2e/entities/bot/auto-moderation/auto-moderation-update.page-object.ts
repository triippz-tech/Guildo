import { element, by, ElementFinder } from 'protractor';

export default class AutoModerationUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botAutoModeration.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  inviteStrikesInput: ElementFinder = element(by.css('input#auto-moderation-inviteStrikes'));
  copyPastaStrikesInput: ElementFinder = element(by.css('input#auto-moderation-copyPastaStrikes'));
  everyoneMentionStrikesInput: ElementFinder = element(by.css('input#auto-moderation-everyoneMentionStrikes'));
  referralStrikesInput: ElementFinder = element(by.css('input#auto-moderation-referralStrikes'));
  duplicateStrikesInput: ElementFinder = element(by.css('input#auto-moderation-duplicateStrikes'));
  resolveUrlsInput: ElementFinder = element(by.css('input#auto-moderation-resolveUrls'));
  enabledInput: ElementFinder = element(by.css('input#auto-moderation-enabled'));
  ignoreConfigSelect: ElementFinder = element(by.css('select#auto-moderation-ignoreConfig'));
  mentionConfigSelect: ElementFinder = element(by.css('select#auto-moderation-mentionConfig'));
  antiDupConfigSelect: ElementFinder = element(by.css('select#auto-moderation-antiDupConfig'));
  autoRaidConfigSelect: ElementFinder = element(by.css('select#auto-moderation-autoRaidConfig'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setInviteStrikesInput(inviteStrikes) {
    await this.inviteStrikesInput.sendKeys(inviteStrikes);
  }

  async getInviteStrikesInput() {
    return this.inviteStrikesInput.getAttribute('value');
  }

  async setCopyPastaStrikesInput(copyPastaStrikes) {
    await this.copyPastaStrikesInput.sendKeys(copyPastaStrikes);
  }

  async getCopyPastaStrikesInput() {
    return this.copyPastaStrikesInput.getAttribute('value');
  }

  async setEveryoneMentionStrikesInput(everyoneMentionStrikes) {
    await this.everyoneMentionStrikesInput.sendKeys(everyoneMentionStrikes);
  }

  async getEveryoneMentionStrikesInput() {
    return this.everyoneMentionStrikesInput.getAttribute('value');
  }

  async setReferralStrikesInput(referralStrikes) {
    await this.referralStrikesInput.sendKeys(referralStrikes);
  }

  async getReferralStrikesInput() {
    return this.referralStrikesInput.getAttribute('value');
  }

  async setDuplicateStrikesInput(duplicateStrikes) {
    await this.duplicateStrikesInput.sendKeys(duplicateStrikes);
  }

  async getDuplicateStrikesInput() {
    return this.duplicateStrikesInput.getAttribute('value');
  }

  getResolveUrlsInput() {
    return this.resolveUrlsInput;
  }
  getEnabledInput() {
    return this.enabledInput;
  }
  async ignoreConfigSelectLastOption() {
    await this.ignoreConfigSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async ignoreConfigSelectOption(option) {
    await this.ignoreConfigSelect.sendKeys(option);
  }

  getIgnoreConfigSelect() {
    return this.ignoreConfigSelect;
  }

  async getIgnoreConfigSelectedOption() {
    return this.ignoreConfigSelect.element(by.css('option:checked')).getText();
  }

  async mentionConfigSelectLastOption() {
    await this.mentionConfigSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async mentionConfigSelectOption(option) {
    await this.mentionConfigSelect.sendKeys(option);
  }

  getMentionConfigSelect() {
    return this.mentionConfigSelect;
  }

  async getMentionConfigSelectedOption() {
    return this.mentionConfigSelect.element(by.css('option:checked')).getText();
  }

  async antiDupConfigSelectLastOption() {
    await this.antiDupConfigSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async antiDupConfigSelectOption(option) {
    await this.antiDupConfigSelect.sendKeys(option);
  }

  getAntiDupConfigSelect() {
    return this.antiDupConfigSelect;
  }

  async getAntiDupConfigSelectedOption() {
    return this.antiDupConfigSelect.element(by.css('option:checked')).getText();
  }

  async autoRaidConfigSelectLastOption() {
    await this.autoRaidConfigSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async autoRaidConfigSelectOption(option) {
    await this.autoRaidConfigSelect.sendKeys(option);
  }

  getAutoRaidConfigSelect() {
    return this.autoRaidConfigSelect;
  }

  async getAutoRaidConfigSelectedOption() {
    return this.autoRaidConfigSelect.element(by.css('option:checked')).getText();
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
