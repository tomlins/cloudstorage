package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class HomePage {

    @FindBy(id = "logout-btn")  private WebElement logoutButton;

    // For Notes
    @FindBy(id = "addNote")             private WebElement addNoteButton;
    @FindBy(id = "note-title")          private WebElement noteTitleInput;
    @FindBy(id = "note-description")    private WebElement noteDescriptionTextArea;
    @FindBy(id = "noteSubmit")          private WebElement noteSubmitButton;
    @FindBy(id = "note_1")              private WebElement deleteNote_1_Link;
    @FindBy(id = "editNote_1")          private WebElement editNote_1_Button;

    // For Credentials
    @FindBy(id = "add-credential-button")   private WebElement addCredentialButton;
    @FindBy(id = "credential-url")          private WebElement credentialUrlInput;
    @FindBy(id = "credential-username")     private WebElement credentialUsernameInput;
    @FindBy(id = "credential-password")     private WebElement credentialPasswordInput;
    @FindBy(id = "credentialSubmit")        private WebElement credentialSubmitButton;

    @FindBy(id = "edit-credential-button_2")        private WebElement editCredentialWithId_2_Button;
    @FindBy(id = "delete-credential-button_2")        private WebElement deleteCredentialWithId_2_Button;

    private WebDriverWait wait;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
        wait = new WebDriverWait(driver, 1000);
    }

    public void logout() {
        logoutButton.submit();
    }

    public void addNote(String title, String description) {
        addNoteButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        noteTitleInput.sendKeys(title);
        noteDescriptionTextArea.sendKeys(description);
        noteSubmitButton.submit();
    }

    public void deleteNoteWithId_1() {
        deleteNote_1_Link.click();
    }

    public void editNote(String title, String description) {
        editNote_1_Button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("noteModal")));

        noteTitleInput.sendKeys(title);
        noteDescriptionTextArea.sendKeys(description);
        noteSubmitButton.submit();
    }

    public void addCredential(String url, String username, String password) {
        addCredentialButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));

        credentialUrlInput.sendKeys(url);
        credentialUsernameInput.sendKeys(username);
        credentialPasswordInput.sendKeys(password);
        credentialSubmitButton.submit();
    }

    public void displayCredentialWithId_2() {
        editCredentialWithId_2_Button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialModal")));
    }

    public void editCredentialWithId_2(String url, String username, String password) {
        credentialUrlInput.clear();
        credentialUrlInput.sendKeys(url);

        credentialUsernameInput.clear();
        credentialUsernameInput.sendKeys(username);

        credentialPasswordInput.clear();
        credentialPasswordInput.sendKeys(password);

        credentialSubmitButton.submit();
    }

    public void deleteCredentialWithId_2() {
        deleteCredentialWithId_2_Button.click();
    }


}
