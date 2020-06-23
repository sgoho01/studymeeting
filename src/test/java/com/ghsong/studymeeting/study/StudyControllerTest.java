package com.ghsong.studymeeting.study;

import com.ghsong.studymeeting.WithAccount;
import com.ghsong.studymeeting.account.AccountRepository;
import com.ghsong.studymeeting.account.form.SignUpForm;
import com.ghsong.studymeeting.domain.Account;
import com.ghsong.studymeeting.domain.Study;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class StudyControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    StudyService studyService;
    @Autowired
    StudyRepository studyRepository;
    @Autowired
    AccountRepository accountRepository;


    @AfterEach
    void afterEach() {
        accountRepository.deleteAll();
    }


    @WithAccount("ghsong")
    @Test
    @DisplayName("스터디 개설 폼 조회")
    public void new_study_form() throws Exception {
        mockMvc.perform(get("/new-study"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"))
                .andExpect(model().attributeExists("account"))
                .andExpect(model().attributeExists("studyForm"));
    }

    @WithAccount("ghsong")
    @Test
    @DisplayName("스터디 개설 성공")
    public void create_study_success() throws Exception {
        mockMvc.perform(post("/new-study")
                .param("path", "test")
                .param("title", "테스트")
                .param("shortDescription", "테스트 스터디")
                .param("fullDescription", "테스트 스터디입니다.")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/test"));

        Study study = studyRepository.findByPath("test");
        assertNotNull(study);
        Account account = accountRepository.findByNickname("ghsong");
        assertTrue(study.getManagers().contains(account));
    }

    @WithAccount("ghsong")
    @Test
    @DisplayName("스터디 개설 실패")
    public void create_study_fail() throws Exception {
        mockMvc.perform(post("/new-study")
                .param("path", "test 1")
                .param("title", "테스트")
                .param("shortDescription", "테스트 스터디")
                .param("fullDescription", "테스트 스터디입니다.")
                .with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("study/form"));

        Study study = studyRepository.findByPath("test");
        assertNull(study);
    }

    @WithAccount("ghsong")
    @Test
    @DisplayName("스터디 조회")
    public void view_study() throws Exception {
        Account account = createAccount("song6497");
        Study study = createStudy("test", account);

        mockMvc.perform(get("/study/test"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/view"))
                .andExpect(model().attributeExists("study"))
                .andExpect(model().attributeExists("account"));
    }

    @WithAccount("ghsong")
    @Test
    @DisplayName("스터디 맴버 조회")
    public void view_study_member() throws Exception {
        Account account = createAccount("song6497");
        Study study = createStudy("test", account);

        mockMvc.perform(get("/study/test/members"))
                .andExpect(status().isOk())
                .andExpect(view().name("study/members"))
                .andExpect(model().attributeExists("study"))
                .andExpect(model().attributeExists("account"));
    }

    protected Study createStudy(String path, Account account) {
        Study study = new Study();
        study.setPath(path);
        Study newStudy= studyService.createNewStudy(study, account);
        return newStudy;
    }

    protected Account createAccount(String nickname) {
        Account account = new Account();
        account.setNickname(nickname);
        account.setEmail(nickname + "@gmail.com");
        account.setPassword("12345678");
        Account newAccount = accountRepository.save(account);
        return newAccount;
    }

    @WithAccount("ghsong")
    @Test
    @DisplayName("스터디 가입")
    public void join_study() throws Exception {
        Account account = createAccount("song6497");
        Study study = createStudy("test", account);

        mockMvc.perform(get("/study/" + study.getPath() + "/join"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        Account ghsong = accountRepository.findByNickname("ghsong");
        assertTrue(study.getMembers().contains(ghsong));
    }


    @WithAccount("ghsong")
    @Test
    @DisplayName("스터디 탈퇴")
    public void leave_study() throws Exception {
        Account account = createAccount("song6497");
        Study study = createStudy("test", account);

        Account ghsong = accountRepository.findByNickname("ghsong");
        studyService.addMember(study, ghsong);

        mockMvc.perform(get("/study/" + study.getPath() + "/leave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getPath() + "/members"));

        assertFalse(study.getMembers().contains(ghsong));
    }


}


