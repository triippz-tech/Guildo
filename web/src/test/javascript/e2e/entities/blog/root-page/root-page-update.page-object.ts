import { element, by, ElementFinder } from 'protractor';

export default class RootPageUpdatePage {
  pageTitle: ElementFinder = element(by.id('webApp.blogRootPage.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  titleInput: ElementFinder = element(by.css('input#root-page-title'));
  slugInput: ElementFinder = element(by.css('input#root-page-slug'));
  childPagesSelect: ElementFinder = element(by.css('select#root-page-childPages'));

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

  async childPagesSelectLastOption() {
    await this.childPagesSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async childPagesSelectOption(option) {
    await this.childPagesSelect.sendKeys(option);
  }

  getChildPagesSelect() {
    return this.childPagesSelect;
  }

  async getChildPagesSelectedOption() {
    return this.childPagesSelect.element(by.css('option:checked')).getText();
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
