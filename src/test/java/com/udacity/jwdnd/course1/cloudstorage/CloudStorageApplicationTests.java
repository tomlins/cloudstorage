package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.entity.Credential;
import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignUpPage;
import com.udacity.jwdnd.course1.cloudstorage.service.CredentialService;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private SignUpPage signUpPage;
	private LoginPage loginPage;
	private HomePage homePage;

	// The one and only user, Peter Cushing
	public static final String FIRSTNAME = "Peter";
	public static final String LASTNAME = "Cushing";
	public static final String USERNAME = "grand_moff";
	public static final String PASSWORD = "TK421";

	// For Notes Tests
	public static final String NOTETITLE = "Note One";
	public static final String NOTEDESCRIPTION = "I've grown tired of asking, so this is the last time. Where are my slippers?";
	public static final String APPENDTITLE = " Update";
	public static final String APPENDDESC = " Thanks";

	// Credentials
	public static final String CRED_1_URL = "http://www.google.com";
	public static final String CRED_1_USERNAME = "david";
	public static final String CRED_1_READABLE_PASSWORD = "ziggy";

	public static final String CRED_2_URL = "http://www.starwars.com";
	public static final String CRED_2_USERNAME = "grandmoff";
	public static final String CRED_2_READABLE_PASSWORD = "deathstar";

	public static final String CRED_3_URL = "http://www.youtube.com";
	public static final String CRED_3_USERNAME = "bikerbytes";
	public static final String CRED_3_READABLE_PASSWORD = "adventure";


	@Autowired
	CredentialService credentialService;


	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	@Order(1)
	public void testUnauthorizedUserCanOnlyAccessHomePageAndLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		Assertions.assertEquals("Sign Up", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());

	}

	@Test
	@Order(2)
	public void signUpLoginVerifyHomePageLogout() {
		// Signup new user
		driver.get("http://localhost:" + this.port + "/signup");
		signUpPage = new SignUpPage(driver);
		signUpPage.signUp(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);
		loginPage = new LoginPage(driver);
		Assertions.assertTrue(loginPage.isSignUpSuccessful());

		// Logon new user
		login();

		// Test user is on home page, then logout
		homePage = new HomePage(driver);
		Assertions.assertEquals("Home", driver.getTitle());
		homePage.logout();

		// Test logged out user back on login page
		loginPage = new LoginPage(driver);
		Assertions.assertEquals("Login", driver.getTitle());

		// Test user can not access home page
		driver.get("http://localhost:" + this.port + "/home");
		Assertions.assertNotEquals("Home", driver.getTitle());
	}

	@Test
	@Order(3)
	public void verifyNonExistantNoteNotDisplayed() {
		login();
		displayHomePage("notes");
		Assertions.assertFalse(doesNoteExist(1, NOTETITLE, NOTEDESCRIPTION));
	}

	@Test
	@Order(4)
	public void createNoteVerifyDisplayed() {
		login();
		displayHomePage("notes");
		homePage.addNote(NOTETITLE, NOTEDESCRIPTION);
		displayHomePage("notes");
		Assertions.assertTrue(doesNoteExist(1, NOTETITLE, NOTEDESCRIPTION));
	}

	@Test
	@Order(5)
	public void editNoteVerifyChangesDisplayed() {
		login();
		displayHomePage("notes");
		homePage.editNote(APPENDTITLE, APPENDDESC); // As we only have one note, we know the id of the note to edit
		displayHomePage("notes");
		Assertions.assertTrue(doesNoteExist(1, NOTETITLE + APPENDTITLE, NOTEDESCRIPTION + APPENDDESC));
	}

	@Test
	@Order(6)
	public void deleteNoteVerifyNotDisplayed() {
		login();
		displayHomePage("notes");
		homePage.deleteNoteWithId_1(); // As we only have one note, we know the id of the note to delete
		displayHomePage("notes");
		Assertions.assertFalse(doesNoteExist(1, NOTETITLE + APPENDTITLE, NOTEDESCRIPTION + APPENDDESC));
	}

	@Test
	@Order(7)
	public void createCredentialsVerifyDisplayed() {
		login();
		displayHomePage("creds");

		// This will have a credential ID of 1
		homePage.addCredential(CRED_1_URL, CRED_1_USERNAME, CRED_1_READABLE_PASSWORD);
		displayHomePage("creds");

		// This will have a credential ID of 2
		homePage.addCredential(CRED_2_URL, CRED_2_USERNAME, CRED_2_READABLE_PASSWORD);
		displayHomePage("creds");

		// This will have a credential ID of 3
		homePage.addCredential(CRED_3_URL, CRED_3_USERNAME, CRED_3_READABLE_PASSWORD);
		displayHomePage("creds");

		Assertions.assertTrue(isCredentialDisplayedAndPasswordEncrypted(1, CRED_1_URL, CRED_1_USERNAME, CRED_1_READABLE_PASSWORD));
		Assertions.assertTrue(isCredentialDisplayedAndPasswordEncrypted(2, CRED_2_URL, CRED_2_USERNAME, CRED_2_READABLE_PASSWORD));
		Assertions.assertTrue(isCredentialDisplayedAndPasswordEncrypted(3, CRED_3_URL, CRED_3_USERNAME, CRED_3_READABLE_PASSWORD));
	}


	@Test
	@Order(8)
	public void viewAndEditCredential_2_VerifyUpdateDisplayed() {
		login();
		displayHomePage("creds");

		// Display Credential set 2
		homePage.displayCredentialWithId_2();

		// Grab the readable password thats displayed in the modal that was returned from AJAX JS call
		WebElement cred_2_Password = driver.findElement(By.id("credential-password"));
		String readablePassword = (String) ((JavascriptExecutor) driver).executeScript("return arguments[0].value", cred_2_Password);

		// Verify the modals' readable password matches that of the original
		Assertions.assertTrue(readablePassword.equals(CRED_2_READABLE_PASSWORD));

		// Edit credential set 2 and set new values for each field
		homePage.editCredentialWithId_2("http://www.startrek", "bones", "enterprise");
		displayHomePage("creds");

		// Verify changes to credential set 2 are reflected in the list of stored and displayed credentials
		Assertions.assertTrue(isCredentialDisplayedAndPasswordEncrypted(2, "http://www.startrek", "bones", "enterprise"));
	}

	@Test
	@Order(9)
	public void deleteCredential_2_VerifyNoLongerDisplayed() {
		login();
		displayHomePage("creds");

		homePage.deleteCredentialWithId_2();
		displayHomePage("creds");

		Assertions.assertFalse(isCredentialDisplayedAndPasswordEncrypted(2, "http://www.startrek", "bones", "enterprise"));
	}



	/******************** Helper methods follow *********************/



	private void login() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage = new LoginPage(driver);
		loginPage.login(USERNAME, PASSWORD);
	}

	private void displayHomePage(String tab) {
		tab = (tab == null || tab.isEmpty()) ? "files" : tab;
		driver.get("http://localhost:" + this.port + "/home?activeTab=" + tab);
		homePage = new HomePage(driver);
		new WebDriverWait(driver, 1000).until(
				webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	}

	private boolean doesNoteExist(Integer idx, String noteTitle, String noteDescription) {
		try {
			WebElement noteTitleElement = driver.findElement(By.id("noteTitle_" + idx));
			WebElement noteDescriptionElement = driver.findElement(By.id("noteDescription_" + idx));
			String noteTitleText = noteTitleElement.getText();
			String noteDescriptionText = noteDescriptionElement.getText();
			if (noteTitle.equals(noteTitleText) && noteDescription.equals(noteDescriptionText))
				return true;
		} catch (NoSuchElementException x) {
			// Thrown when element not displayed
			return false;
		}

		return false;
	}

	private boolean isCredentialDisplayedAndPasswordEncrypted(Integer id, String url, String username, String password) {
		try {
			WebElement credURL = driver.findElement(By.id("credURL_" + id));
			WebElement credUsername = driver.findElement(By.id("credUsername_" + id));
			WebElement credPassword = driver.findElement(By.id("credPassword_" + id));

			// These are the displayed credentials for the given ID
			String displayedCredURLText = credURL.getText();
			String displayedCredUsernameText = credUsername.getText();
			String displayedEncryptedPasswordText = credPassword.getText();

			// Get the matching stored credential
			Credential credential = credentialService.getCredential(id);

			// Check that the displayed credentials is same as method args
			// AND same as stored credential except for password as the displayed
			// password should NOT match the stored encrypted password
			if ((displayedCredURLText.equals(credential.getUrl()) && displayedCredURLText.equals(url)) &&
					(displayedCredUsernameText.equals(credential.getUsername()) && displayedCredUsernameText.equals(username)) &&
					(displayedEncryptedPasswordText.equals(credential.getPassword()) && !displayedEncryptedPasswordText.equals(password)))
				return true;
		} catch (NoSuchElementException x) {
			// Thrown when element not displayed
			return false;
		}

		return false;
	}
}
