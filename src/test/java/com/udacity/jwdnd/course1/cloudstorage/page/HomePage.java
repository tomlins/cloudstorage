package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(id = "logout-btn")  private WebElement logoutButton;

    // For Add Note
    @FindBy(id = "addNote")             private WebElement addNoteButton;
    @FindBy(id = "note-title")          private WebElement noteTitleInput;
    @FindBy(id = "note-description")    private WebElement noteDescriptionTextArea;
    @FindBy(id = "noteSubmit")          private WebElement noteSubmitButton;

    // For Delete Note
    @FindBy(id = "note_1")              private WebElement deleteNote_1_Link;

    // For Delete Note
    @FindBy(id = "editNote_1")              private WebElement editNote_1_Button;

    public HomePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.submit();
    }

    public void addNote(String title, String description) {
        addNoteButton.click();
        delay(2);
        noteTitleInput.sendKeys(title);
        noteDescriptionTextArea.sendKeys(description);
        noteSubmitButton.submit();
    }

    public void deleteNote(Integer noteId) {
        deleteNote_1_Link.click();
    }

    public void editNote(Integer noteId, String title, String description) {
        editNote_1_Button.click();
        delay(2);
        noteTitleInput.sendKeys(title);
        noteDescriptionTextArea.sendKeys(description);
        noteSubmitButton.submit();
    }

    public static void delay(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
