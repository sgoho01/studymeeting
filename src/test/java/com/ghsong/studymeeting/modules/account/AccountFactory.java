package com.ghsong.studymeeting.modules.account;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountFactory {

    @Autowired AccountRepository accountRepository;

    public Account createAccount(String nickname) {
        Account ghsong = new Account();
        ghsong.setNickname(nickname);
        ghsong.setEmail(nickname + "@gmail.com");
        accountRepository.save(ghsong);
        return ghsong;
    }
}
