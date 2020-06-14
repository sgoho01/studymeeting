package com.ghsong.studymeeting.settings;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghsong.studymeeting.WithAccount;
import com.ghsong.studymeeting.account.AccountRepository;
import com.ghsong.studymeeting.account.AccountService;
import com.ghsong.studymeeting.domain.Account;
import com.ghsong.studymeeting.domain.Tag;
import com.ghsong.studymeeting.domain.Zone;
import com.ghsong.studymeeting.tag.form.TagForm;
import com.ghsong.studymeeting.zone.form.ZoneForm;
import com.ghsong.studymeeting.tag.TagRepository;
import com.ghsong.studymeeting.zone.ZoneRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.ghsong.studymeeting.settings.SettingsController.*;
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
    @Autowired
    ZoneRepository zoneRepository;

    private Zone testZone = Zone.builder().city("test").localNameOfCity("테스트시").province("테스트주").build();

    @BeforeEach
    void beforeEach() {
        zoneRepository.save(testZone);
    }

    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
        zoneRepository.deleteAll();
    }

    @WithAccount("ghsong")
    @DisplayName("프로필 수정 폼")
    @Test
    void update_profile_form() throws Exception {
        String bio = "소개수정";
        mockMvc.perform(get(ROOT + SETTINGS + PROFILE))
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
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                .param("bio", bio)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ROOT + SETTINGS + PROFILE))
                .andExpect(flash().attributeExists("message"));

        Account ghsong = accountRepository.findByNickname("ghsong");
        assertEquals(bio, ghsong.getBio());
    }

    @WithAccount("ghsong")
    @DisplayName("프로필 수정 - 입력값 에러")
    @Test
    void update_profile_wrong_input() throws Exception {
        String bio = "길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우,길게 수정하는 경우";
        mockMvc.perform(post(ROOT + SETTINGS + PROFILE)
                .param("bio", bio)
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PROFILE))
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
        mockMvc.perform(get(ROOT + SETTINGS + PASSWORD))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("passwordForm"));
    }

    @WithAccount("ghsong")
    @DisplayName("패스워드 수정 - 입력값 정상")
    @Test
    void update_password_correct_input() throws Exception {
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
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
        mockMvc.perform(post(ROOT + SETTINGS + PASSWORD)
                .param("newPassword", "12345678")
                .param("newPasswordConfirm", "12345679")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name(SETTINGS + PASSWORD))
                .andExpect(model().attributeExists("passwordForm"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().hasErrors());
    }

    @WithAccount("ghsong")
    @DisplayName("태그 수정 폼")
    @Test
    void update_tags_form() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + TAGS))
                .andExpect(view().name(SETTINGS + TAGS))
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

        mockMvc.perform(post(ROOT + SETTINGS + TAGS + ADD)
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

        mockMvc.perform(post(ROOT + SETTINGS + TAGS + REMOVE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagForm))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(ghsong.getTags().contains(newTag));
    }

    @WithAccount("ghsong")
    @DisplayName("관심지역 수정 폼")
    @Test
    void update_zones_form() throws Exception {
        mockMvc.perform(get(ROOT + SETTINGS + ZONES))
                .andExpect(view().name(SETTINGS + ZONES))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("whitelist"))
                .andExpect(model().attributeExists("zones"));
    }

    @WithAccount("ghsong")
    @DisplayName("계정에 관심지역 추가")
    @Test
    void add_zone() throws Exception {
        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post(ROOT + SETTINGS + ZONES + ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        Account ghsong = accountRepository.findByNickname("ghsong");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince()).orElseThrow();
        assertTrue(ghsong.getZones().contains(zone));
    }

    @WithAccount("ghsong")
    @DisplayName("관심지역 삭제")
    @Test
    void remove_zone() throws Exception {
        Account ghsong = accountRepository.findByNickname("ghsong");
        Zone zone = zoneRepository.findByCityAndProvince(testZone.getCity(), testZone.getProvince()).orElseThrow();
        accountService.addZone(ghsong, zone);

        assertTrue(ghsong.getZones().contains(zone));

        ZoneForm zoneForm = new ZoneForm();
        zoneForm.setZoneName(testZone.toString());

        mockMvc.perform(post(ROOT + SETTINGS + ZONES + REMOVE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(zoneForm))
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk());

        assertFalse(ghsong.getTags().contains(zone));
    }

}
