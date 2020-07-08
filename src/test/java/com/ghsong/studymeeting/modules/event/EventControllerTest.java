package com.ghsong.studymeeting.modules.event;


import com.ghsong.studymeeting.modules.account.WithAccount;
import com.ghsong.studymeeting.modules.account.Account;
import com.ghsong.studymeeting.modules.study.Study;
import com.ghsong.studymeeting.modules.study.StudyControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest extends StudyControllerTest {


    @Autowired
    EventService eventService;
    @Autowired
    EnrollmentRepository enrollmentRepository;

    private Event createEvent(String eventTitle, EventType eventType, int limit, Study study, Account account) {
        Event event = new Event();
        event.setEventType(eventType);
        event.setLimitOfEnrollments(limit);
        event.setTitle(eventTitle);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setEndEnrollmentDateTime(LocalDateTime.now().plusDays(1));
        event.setStartDateTime(LocalDateTime.now().plusDays(1).plusHours(5));
        event.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(7));
        return eventService.createEvent(event, study, account);
    }


    private void isNotAccepted(Account song6497, Event event) {
        assertFalse(enrollmentRepository.findByEventAndAccount(event, song6497).isAccepted());
    }

    private void isAccepted(Event event, Account jun) {
        assertTrue(enrollmentRepository.findByEventAndAccount(event, jun).isAccepted());
    }


    @Test
    @WithAccount("song6497")
    @DisplayName("선착순 모임에 참가 신청 - 자동 수락")
    void newEnrollment_FCFS_accept() throws Exception {
        Account ghsong = createAccount("ghsong");
        Study study = createStudy("test-study", ghsong);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, ghsong);

        mockMvc.perform(post("/study/" + study.getEncodePath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodePath() + "/events/" + event.getId()));

        Account song = accountRepository.findByNickname("song6497");
        isAccepted(event, song);
    }


    @Test
    @WithAccount("song6497")
    @DisplayName("선착순 모임에 참가 신청 - 대기중 (이미 인원이 꽉차서)")
    void newEnrollment_FCFS_not_accept() throws Exception {
        Account ghsong = createAccount("ghsong");
        Study study = createStudy("test-study", ghsong);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, ghsong);

        Account jun = createAccount("jun");
        Account jin = createAccount("jin");

        eventService.newEnrollment(event, jun);
        eventService.newEnrollment(event, jin);

        isAccepted(event, jun);
        isAccepted(event, jin);

        mockMvc.perform(post("/study/" + study.getEncodePath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodePath() + "/events/" + event.getId()));

        Account song = accountRepository.findByNickname("song6497");
        isNotAccepted(song, event);
    }


    @Test
    @WithAccount("song6497")
    @DisplayName("선착순 모임에 참가 신청 - 취소")
    void cancelEnrollment_FCFS() throws Exception {
        Account song = accountRepository.findByNickname("song6497");
        Account ghsong = createAccount("ghsong");
        Study study = createStudy("test-study", ghsong);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, ghsong);

        eventService.newEnrollment(event, song);
        eventService.newEnrollment(event, ghsong);

        mockMvc.perform(post("/study/" + study.getEncodePath() + "/events/" + event.getId() + "/disenroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodePath() + "/events/" + event.getId()));

        assertNull(enrollmentRepository.findByEventAndAccount(event, song));
    }


    @Test
    @WithAccount("song6497")
    @DisplayName("선착순 모임에 참가 신청 - 대기중")
    void newEnrollment_FCFS_wait_accept() throws Exception {
        Account ghsong = createAccount("ghsong");
        Study study = createStudy("test-study", ghsong);
        Event event = createEvent("test-event", EventType.FCFS, 2, study, ghsong);

        Account jun = createAccount("jun");
        Account jin = createAccount("jin");

        eventService.newEnrollment(event, jun);
        eventService.newEnrollment(event, jin);

        mockMvc.perform(post("/study/" + study.getEncodePath() + "/events/" + event.getId() + "/enroll")
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/study/" + study.getEncodePath() + "/events/" + event.getId()));

        Account song = accountRepository.findByNickname("song6497");
        isNotAccepted(song, event);
    }

}
