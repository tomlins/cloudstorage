package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.page.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.page.SignUpPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private SignUpPage signUpPage;
	private LoginPage loginPage;
	private HomePage homePage;

	public static final String FIRSTNAME = "Peter";
	public static final String LASTNAME = "Cushing";
	public static final String USERNAME = "grand_moff";
	public static final String PASSWORD = "TK421";

	// For Notes Tests
	public static final String NOTETITLE = "Note One";
	public static final String NOTEDESCRIPTION = "I've grown tired of asking, so this is the last time. Where are my slippers?";
	public static final String APPENDTITLE = " Update";
	public static final String APPENDDESC = " Thanks";


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
		signUpPage = new SignUpPage(driver);
		Assertions.assertTrue(signUpPage.isSignUpSuccessful());

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
	public void loginCreateNoteVerifyDisplayed() {

		login();
		displayHomePage("notes");
		homePage.addNote(NOTETITLE, NOTEDESCRIPTION);
		displayHomePage("notes");
		Assertions.assertTrue(doesNoteExist(NOTETITLE, NOTEDESCRIPTION));

	}

	@Test
	@Order(4)
	public void editNoteVerifyChangesDisplayed() {

		login();
		displayHomePage("notes");
		homePage.editNote(1, APPENDTITLE, APPENDDESC); // As we only have one note, we know the id of the note to edit
		displayHomePage("notes");
		Assertions.assertTrue(doesNoteExist(NOTETITLE + APPENDTITLE, NOTEDESCRIPTION + APPENDDESC));

	}


	@Test
	@Order(5)
	public void deleteNoteVerifyNotDisplayed() {

		login();
		displayHomePage("notes");
		homePage.deleteNote(1); // As we only have one note, we know the id of the note to delete
		displayHomePage("notes");
		Assertions.assertFalse(doesNoteExist(NOTETITLE + APPENDTITLE, NOTEDESCRIPTION + APPENDDESC));


	}


	private void login() {
		driver.get("http://localhost:" + this.port + "/login");
		loginPage = new LoginPage(driver);
		loginPage.login(USERNAME, PASSWORD);
	}

	private void displayHomePage(String tab) {
		tab = (tab == null || tab.isEmpty()) ? "files" : tab;
		driver.get("http://localhost:" + this.port + "/home?activeTab=" + tab);
		homePage = new HomePage(driver);
		delay(1);
	}

	/**
	 * Searches for a particular note based on title and description
	 * @throws org.openqa.selenium.NoSuchElementException if no notes exist
	 * @param noteTitle
	 * @param noteDescription
	 * @return true if note exists, otherwise false
	 */
	private boolean doesNoteExist(String noteTitle, String noteDescription) {
		List<WebElement> notesList = driver.findElements(By.id("notes-list"));
		int idx = 1;
		for (WebElement element : notesList) {
			try {
				WebElement noteTitleElement = element.findElement(By.id("noteTitle_" + idx));
				WebElement noteDescriptionElement = element.findElement(By.id("noteDescription_" + idx));
				String noteTitleText = noteTitleElement.getText();
				String noteDescriptionText = noteDescriptionElement.getText();
				if (noteTitle.equals(noteTitleText) && noteDescription.equals(noteDescriptionText))
					return true;
			} catch (NoSuchElementException x) {
				return false;
			}
			idx++;
		}
		return false;
	}

	public static void delay(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
