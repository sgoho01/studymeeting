package com.ghsong.studymeeting.settings;

import com.ghsong.studymeeting.WithAccount;
import com.ghsong.studymeeting.account.AccountRepository;
import com.ghsong.studymeeting.account.AccountService;
import com.ghsong.studymeeting.domain.Account;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    PasswordEncoder passwordEncoder;

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }

    @WithAccount("ghsong")
    @DisplayName("프로필 수정 폼")
    @Test
    void update_profile_form() throws Exception {
        String bio = "소개수정";
        mockMvc.perform(get(SettingsController.SETTINGS_PROFILE_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("profile"));
    }

    @WithAccount("ghsong")
    @DisplayName("프로필 수정 - 입력값 정상")
    @Test
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

    @WithAccount("ghsong")
    @DisplayName("프로필 수정 - 입력값 에러")
    @Test
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

    @WithAccount("ghsong")
    @DisplayName("패스워드 수정 폼")
    @Test
    void update_password_form() throws Exception {
        mockMvc.perform(get("/settings/password"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("ghsong")
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void update_password_correct_input() throws Exception {
        mockMvc.perform(post("/settings/password")
                .param("newPassword", "12345678")
                .param("newPasswordConfirm", "12345678")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("message"));

        Account ghsong = accountRepository.findByNickname("ghsong");
        assertTrue(passwordEncoder.matches("12345678", ghsong.getPassword()));
    }

    @WithAccount("ghsong")
    @DisplayName("패스워드 수정 - 입력값 에러 - 패스워드 불일치")
    @Test
    void update_password_wrong_input() throws Exception {
        mockMvc.perform(post("/settings/password")
                .param("newPassword", "12345678")
                .param("newPasswordConfirm", "12345679")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(SettingsController.SETTINGS_PASSWORD_VIEW_NAME))
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().hasErrors());
    }

}
