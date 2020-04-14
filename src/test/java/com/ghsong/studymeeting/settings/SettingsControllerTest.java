package com.ghsong.studymeeting.settings;

import com.ghsong.studymeeting.WithAccount;
import com.ghsong.studymeeting.account.AccountRepository;
import com.ghsong.studymeeting.account.AccountService;
import com.ghsong.studymeeting.account.SignUpForm;
import com.ghsong.studymeeting.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author : song6
 * Date: 2020-04-14
 * Copyright(©) 2020
 */
@SpringBootTest
@AutoConfigureMockMvc
class SettingsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @DisplayName("프로필 수정 폼")
    @Test
    @WithAccount("ghsong")
    void update_profile_form() throws Exception {
        String bio = "소개수정";
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @DisplayName("프로필 수정 - 입력값 정상")
    @Test
    @WithAccount("ghsong")
    void update_profile_correct_input() throws Exception {
        String bio = "소개수정";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(SettingsController.SETTINGS_PROFILE_URL))
                .andExpect(flash().attributeExists("message"));

        Account ghsong = accountRepository.findByNickname("ghsong");
        assertEquals(bio, ghsong.getBio());
    }

    @DisplayName("프로필 수정 - 입력값 에러")
    @Test
    @WithAccount("ghsong")
    void update_profile_wrong_input() throws Exception {
        String bio = "길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우";
        mockMvc.perform(post(SettingsController.SETTINGS_PROFILE_URL)
                .param("bio", bio)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PROFILE_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(model().hasErrors());

        Account ghsong = accountRepository.findByNickname("ghsong");
        assertNull(ghsong.getBio());
    }

}