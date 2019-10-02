import { element, by, ElementFinder } from 'protractor';

export default class GuildPollItemUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.botGuildPollItem.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  itemNameInput: ElementFinder = element(by.css('input#guild-poll-item-itemName'));
  votesInput: ElementFinder = element(by.css('input#guild-poll-item-votes'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setItemNameInput(itemName) {
    await this.itemNameInput.sendKeys(itemName);
  }

  async getItemNameInput() {
    return this.itemNameInput.getAttribute('value');
  }

  async setVotesInput(votes) {
    await this.votesInput.sendKeys(votes);
  }

  async getVotesInput() {
    return this.votesInput.getAttribute('value');
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
