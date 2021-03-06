package com.nilhcem.clearbrain.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import com.nilhcem.clearbrain.business.UserBo;
import com.nilhcem.clearbrain.core.spring.UserDetailsAdapter;
import com.nilhcem.clearbrain.form.SettingsForm;
import com.nilhcem.clearbrain.model.User;

/**
 * Validates data from a {@code SettingsForm} object.
 *
 * @author Nilhcem
 * @since 1.0
 * @see SettingsForm
 */
public final class SettingsValidator implements Validator {
	@Autowired
	private UserBo userBo;

	/**
	 * Only supports {@code SettingsForm} class.
	 *
	 * @param clazz the class which should be supported.
	 * @return {@code true} if this validator supports the class passed in parameter.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return SettingsForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validates data in a {@code SettingsForm} object.
	 *
	 * @param target the {@code SettingsForm} object.
	 * @param errors contains validation errors if any.
	 */
	@Override
	public void validate(Object target, Errors errors) {
		SettingsForm form = (SettingsForm) target;
		User currentUser = ((UserDetailsAdapter) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getHibernateUser();

		// Check password
		if (form.getEditPassword().equals("yes")) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "currentPassword", "settings.err.pwd");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "newPassword", "settings.err.pwd");

			// Check password confirmation
			if (!form.getNewPassword().equals(form.getConfirmPassword())) {
				errors.rejectValue("confirmPassword", "settings.err.pwdConf");
			}

			// Check if current password is OK
			String hashedPassword = userBo.hashPassword(currentUser, form.getCurrentPassword());
			if (!hashedPassword.equals(currentUser.getPassword())) {
				errors.rejectValue("currentPassword", "settings.err.curPwd");
			}
		}

		// Check if email is valid
		final Pattern pattern = Pattern.compile("\\S+@\\S+"); // If this change, see also SignUpValidator
		Matcher matcher = pattern.matcher(form.getEmail());
		if (matcher.find()) {
			// Check if email has changed (compared to current email) and email is already registered
			if ((!form.getEmail().equalsIgnoreCase(currentUser.getEmail())) && (userBo.findByEmail(form.getEmail()) != null)) {
				errors.rejectValue("email", "settings.err.mailRegist");
			}
		} else {
			errors.rejectValue("email", "settings.err.mail");
		}
	}
}
