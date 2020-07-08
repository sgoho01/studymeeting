package com.ghsong.studymeeting.modules.account.validator;

import com.ghsong.studymeeting.modules.account.AccountRepository;
import com.ghsong.studymeeting.modules.account.Account;
import com.ghsong.studymeeting.modules.account.form.NicknameForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author : song6
 * Date: 2020-04-22
 * Copyright(©) 2020
 */
@Component
@RequiredArgsConstructor
public class NIcknameFormValidator implements Validator {

    private final AccountRepository accountRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.isAssignableFrom(NicknameForm.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        NicknameForm nicknameForm = (NicknameForm) o;
        Account account = accountRepository.findByNickname(nicknameForm.getNickname());
        if (account != null) {
            errors.rejectValue("nickname", "wrong.nickname", "입력하신 닉네임은 사용할 수 없습니다.");
        }
    }
}
