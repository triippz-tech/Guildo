import { element, by, ElementFinder } from 'protractor';

export default class PageUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.blogPage.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  titleInput: ElementFinder = element(by.css('input#page-title'));
  slugInput: ElementFinder = element(by.css('input#page-slug'));
  publishedInput: ElementFinder = element(by.css('input#page-published'));
  editedInput: ElementFinder = element(by.css('input#page-edited'));
  bodyInput: ElementFinder = element(by.css('textarea#page-body'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setTitleInput(title) {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput() {
    return this.titleInput.getAttribute('value');
  }

  async setSlugInput(slug) {
    await this.slugInput.sendKeys(slug);
  }

  async getSlugInput() {
    return this.slugInput.getAttribute('value');
  }

  async setPublishedInput(published) {
    await this.publishedInput.sendKeys(published);
  }

  async getPublishedInput() {
    return this.publishedInput.getAttribute('value');
  }

  async setEditedInput(edited) {
    await this.editedInput.sendKeys(edited);
  }

  async getEditedInput() {
    return this.editedInput.getAttribute('value');
  }

  async setBodyInput(body) {
    await this.bodyInput.sendKeys(body);
  }

  async getBodyInput() {
    return this.bodyInput.getAttribute('value');
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
