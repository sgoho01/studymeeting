package com.ghsong.studymeeting.modules.main;

import com.ghsong.studymeeting.modules.account.CurrentUser;
import com.ghsong.studymeeting.modules.account.Account;
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
