package com.nilhcem.controller;

import static org.junit.Assert.*;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.bind.support.SimpleSessionStatus;
import org.springframework.web.servlet.ModelAndView;
import com.nilhcem.core.hibernate.TransactionalReadWrite;
import com.nilhcem.form.SignUpForm;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/applicationContext-test.xml", "classpath:/META-INF/spring/mvc/mvc-dispatcher-servlet.xml"})
public class SignUpControllerTest {
	@Autowired
	private SignUpController controller;
	private final static String SIGNUP_PAGE = "front/signup";
	private final static String PASSWORD = "myP#ssW0Rd";

	@Test
	public void signUpPageShouldBeCorrectlyInitialized() {
		ModelMap model = new ModelMap();
		assertEquals(SIGNUP_PAGE, controller.initSignUpPage(model));
		assertTrue(model.containsAttribute("signupform"));
	}

	private ModelAndView getModelAndViewSignUpPage(String email, String password, String pwdConfirm) {
		MockHttpServletRequest request = new MockHttpServletRequest("post", "/signup");
		request.setParameter("user.email", email);
		request.setParameter("user.password", password);
		request.setParameter("passwordConfirmation", pwdConfirm);

		SignUpForm signUpForm = new SignUpForm();
		WebDataBinder binder = new WebDataBinder(signUpForm, "signupform");
		binder.bind(new MutablePropertyValues(request.getParameterMap()));
		SessionStatus status = new SimpleSessionStatus();

		return controller.submitSignUpPage(signUpForm, binder.getBindingResult(), status, request);
	}

	@Test
	public void submitSignUpPageWithBadEmailShouldRedirectToInitialForm() {
		ModelAndView modelAndView = getModelAndViewSignUpPage("", PASSWORD, PASSWORD);
		assertEquals(SIGNUP_PAGE, modelAndView.getViewName());
		assertNotNull(modelAndView.getModel());
	}

	@Test
	public void submitSignUpPageWithEmptyPwdShouldRedirectToInitialForm() {
		ModelAndView modelAndView = getModelAndViewSignUpPage("my.email@company.com", "", "");
		assertEquals(SIGNUP_PAGE, modelAndView.getViewName());
		assertNotNull(modelAndView.getModel());
	}

	@Test
	public void submitSignUpPageWithBadPwdConfirmationShouldRedirectToInitialForm() {
		ModelAndView modelAndView = getModelAndViewSignUpPage("my.email@company.com", PASSWORD, "notTheSame");
		assertEquals(SIGNUP_PAGE, modelAndView.getViewName());
		assertNotNull(modelAndView.getModel());
	}

	@Test
	@TransactionalReadWrite
	@Rollback(true)
	public void submitSignUpPageWithValidObjectShouldRedirectToCompletedView() {
		String email = "my.email@company.com";
		ModelAndView modelAndView = getModelAndViewSignUpPage(email, PASSWORD, PASSWORD);
		assertEquals("redirectWithoutModel:signup-completed", modelAndView.getViewName());
		assertNotNull(modelAndView.getModel());
		checkEmailAvailabilityShouldReturnFalse(email);
	}

	@Test
	public void shouldGetCompletedSignUpPage() {
		assertEquals("front/signupCompleted", controller.getSignUpCompletedPage());
	}

	@Test
	public void checkEmailAvailabilityShouldReturnTrue() {
		assertTrue(controller.checkEmailAvailability("###@###.###")); // this email should not be taken by anybody
	}

	public void checkEmailAvailabilityShouldReturnFalse(String email) {
		assertFalse(controller.checkEmailAvailability(email));
	}

	@Test
	public void testJavascriptLocales() {
		final String[] keys = {"err.pwd", "err.pwdConf", "err.mailRegist", "err.mail", "ok.mail", "ok.pwd", "ok.pwdConf"};

		MockHttpServletRequest request = new MockHttpServletRequest("get", "/signup");
		Map<String, String> map = controller.sendI18nToJavascript(request);
		assertNotNull(map);

		for (String key : keys) {
			assertNotNull(map.get(key));
		}
	}
}
