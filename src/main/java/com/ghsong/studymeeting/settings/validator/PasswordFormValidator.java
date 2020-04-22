package com.ghsong.studymeeting.settings.validator;

import com.ghsong.studymeeting.settings.form.PasswordForm;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author : song6
 * Date: 2020-04-15
 * Copyright(©) 2020
 */
public class PasswordFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return PasswordForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        PasswordForm passwordForm = (PasswordForm) o;
        if(!passwordForm.getNewPassword().equals(passwordForm.getNewPasswordConfirm())) {
            errors.rejectValue("newPassword", "wrong.password", "입력한 새 패스워드가 일치하지 않습니다.");
        }
    }
}
