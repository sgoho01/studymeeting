package com.ghsong.studymeeting.modules.main;

import com.ghsong.studymeeting.modules.account.CurrentUser;
import com.ghsong.studymeeting.modules.account.Account;
import com.ghsong.studymeeting.modules.study.Study;
import com.ghsong.studymeeting.modules.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author : song6
 * Date: 2020-04-12
 * Copyright(©) 2020
 */
@Controller
@RequiredArgsConstructor
public class MainController {

    private final StudyRepository studyRepository;

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

    @GetMapping("/search/study")
    public String searchStudy(@PageableDefault(size = 9, page = 0, sort = "publishedDateTime", direction = Sort.Direction.ASC) Pageable pageable, String keyword, Model model) {
        Page<Study> studyPage = studyRepository.findByKeyword(keyword, pageable);
        model.addAttribute("studyPage", studyPage);
        model.addAttribute("keyword", keyword);
        return "search";
    }

}
