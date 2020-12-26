package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    @FindBy(id = "inputUsername")   private WebElement usernameTextField;
    @FindBy(id = "inputPassword")   private WebElement passwordTextField;
    @FindBy(id = "loginButton")     private WebElement loginButton;
    @FindBy(id = "success-msg")     private WebElement signUpSuccessMessage;

    public LoginPage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void login(String username, String password) {
        usernameTextField.sendKeys(username);
        passwordTextField.sendKeys(password);
        loginButton.click();
    }

    public boolean isSignUpSuccessful() {
        if (signUpSuccessMessage.getText().contains("You successfully signed up"))
            return true;
        return false;
    }

}
