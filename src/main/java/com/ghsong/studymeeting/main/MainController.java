package com.ghsong.studymeeting.main;

import com.ghsong.studymeeting.account.CurrentUser;
import com.ghsong.studymeeting.domain.Account;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author : song6
 * Date: 2020-04-12
 * Copyright(©) 2020
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String home(@CurrentUser Account account, Model model) {
        if (account != null) {
            model.addAttribute(account);
        }

        return "index";
    }
}
