package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class SignUpPage {

    @FindBy(id = "inputFirstName")   private WebElement firstNameTextField;
    @FindBy(id = "inputLastName")   private WebElement lastNameTextField;
    @FindBy(id = "inputUsername")   private WebElement usernameTextField;
    @FindBy(id = "inputPassword")   private WebElement passwordTextField;
    @FindBy(id = "signupButton")   private WebElement signupButton;
    @FindBy(id = "success-msg")   private WebElement signUpSuccessMessage;

    public SignUpPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void signUp(String firstName, String lastName, String username, String password) {
        firstNameTextField.sendKeys(firstName);
        lastNameTextField.sendKeys(lastName);
        usernameTextField.sendKeys(username);
        passwordTextField.sendKeys(password);
        signupButton.click();
    }

    public boolean isSignUpSuccessful() {
        if (signUpSuccessMessage.getText().contains("You successfully signed up"))
            return true;
        return false;
    }

}
