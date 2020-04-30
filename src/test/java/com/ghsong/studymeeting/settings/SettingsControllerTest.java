package com.ghsong.studymeeting.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghsong.studymeeting.WithAccount;
import com.ghsong.studymeeting.account.AccountRepository;
import com.ghsong.studymeeting.account.AccountService;
import com.ghsong.studymeeting.domain.Account;
import com.ghsong.studymeeting.domain.Tag;
import com.ghsong.studymeeting.settings.form.TagForm;
import com.ghsong.studymeeting.tag.TagRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
@Transactional
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
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TagRepository tagRepository;


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

    @WithAccount("ghsong")
    @DisplayName("태그 수정 폼")
    @Test
    void update_tags_form() throws Exception {
        mockMvc.perform(get(SettingsController.SETTINGS_TAGS_URL))
                .andExpect(view().name(SettingsController.SETTINGS_TAGS_VIEW_NAME))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("tags"));
    }

    @WithAccount("ghsong")
    @DisplayName("계정에 태그 추가")
    @Test
    void add_tag() throws Exception {
        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        Tag newTag = tagRepository.findByTitle("newTag").orElse(null);
        assertNotNull(newTag);
        assertTrue(accountRepository.findByNickname("ghsong").getTags().contains(newTag));
    }

    @WithAccount("ghsong")
    @DisplayName("태그 삭제")
    @Test
    void remove_tag() throws Exception {
        Account ghsong = accountRepository.findByNickname("ghsong");
        Tag newTag = tagRepository.save(Tag.builder().title("newTag").build());
        accountService.addTag(ghsong, newTag);

        assertTrue(ghsong.getTags().contains(newTag));

        TagForm tagForm = new TagForm();
        tagForm.setTagTitle("newTag");

        mockMvc.perform(post(SettingsController.SETTINGS_TAGS_URL + "/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(ghsong.getTags().contains(newTag));
    }

}
