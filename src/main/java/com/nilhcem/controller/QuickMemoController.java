package com.nilhcem.controller;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;
import com.nilhcem.business.QuickMemoBo;
import com.nilhcem.form.QuickMemoForm;
import com.nilhcem.model.QuickMemo;

/**
 * Spring MVC Controller class for displaying quick memo.
 *
 * @author Nilhcem
 * @since 1.0
 */
@Controller
@PreAuthorize("hasRole('RIGHT_USER')")
@RequestMapping("/quick_memo")
public final class QuickMemoController extends AbstractController {
	@Autowired
	private QuickMemoBo quickMemoBo;

	/**
	 * Initialize quick memo form, giving it the QuickMemo model.
	 *
	 * @param model Model map.
	 * @return the QuickMemo view.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String getQuickMemoView(HttpServletRequest request, ModelMap model) {
		QuickMemoForm memoForm = new QuickMemoForm();
		QuickMemo memo = quickMemoBo.getByUser(getCurrentUser());

		//If this is the first time a quick memo is opened, display default text
		if (memo.getSaveDate() == null) {
			Locale locale = RequestContextUtils.getLocale(request);
			memoForm.setInput(message.getMessage("memo.default.text", null, locale));
		}
		else {
			memoForm.setInput(memo.getContent());
		}
		memoForm.setSaveDate(memo.getSaveDate());
		model.addAttribute("memoform", memoForm);
		return "logged/quick-memo";
	}

	/**
	 * Submit a quick memo to save it.
	 *
	 * @param memoForm The quick memo form.
	 * @param result Binding result.
	 * @param status Session status.
	 * @param request HTTP request.
	 * @return A new view (logged/quick-memo).
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView submitSignUpPage(@ModelAttribute("memoform") QuickMemoForm memoForm, BindingResult result,
		SessionStatus status, HttpServletRequest request) {
		status.setComplete();
		quickMemoBo.updateMemo(getCurrentUser(), memoForm.getInput());
		return new ModelAndView("redirectWithoutModel:quick_memo");
	}
}