package com.ghsong.studymeeting.modules.event;

import com.ghsong.studymeeting.modules.account.Account;
import com.ghsong.studymeeting.modules.event.event.EnrollmentAcceptedEvent;
import com.ghsong.studymeeting.modules.event.event.EnrollmentRejectedEvent;
import com.ghsong.studymeeting.modules.study.Study;
import com.ghsong.studymeeting.modules.event.form.EventForm;
import com.ghsong.studymeeting.modules.study.event.StudyUpdateEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    private final EnrollmentRepository enrollmentRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    public Event createEvent(Event event, Study study, Account account) {
        event.setCreatedBy(account);
        event.setCreatedDateTime(LocalDateTime.now());
        event.setStudy(study);
        applicationEventPublisher.publishEvent(new StudyUpdateEvent(study, "모임을 만들었습니다."));
        return eventRepository.save(event);
    }

    public void updateEvent(Event event, EventForm eventForm) {
        modelMapper.map(eventForm, event);
        event.acceptWaitingList();
        applicationEventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "모임 정보가 수정되었습니다."));
    }

    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        applicationEventPublisher.publishEvent(new StudyUpdateEvent(event.getStudy(), "모임이 취소되었습니다."));
    }

    public void newEnrollment(Event event, Account account) {
        if (!enrollmentRepository.existsByEventAndAccount(event, account)) {
            Enrollment enrollment = new Enrollment();
            enrollment.setEnrolledAt(LocalDateTime.now());
            enrollment.setAccepted(event.isAbleToAcceptWaitingEnrollment());
            enrollment.setAccount(account);
            event.addEnrollment(enrollment);
            enrollmentRepository.save(enrollment);
        }
    }

    public void cancelEnrollment(Event event, Account account) {
        Enrollment enrollment = enrollmentRepository.findByEventAndAccount(event, account);
        if (!enrollment.isAttended()) {
            event.removeEnrollment(enrollment);
            enrollmentRepository.delete(enrollment);
            event.acceptNextWaitingEnrollment();
        }
    }

    public void acceptEnrollment(Event event, Enrollment enrollment) {
        event.accept(enrollment);
        applicationEventPublisher.publishEvent(new EnrollmentAcceptedEvent(enrollment));
    }

    public void reject(Event event, Enrollment enrollment) {
        event.reject(enrollment);
        applicationEventPublisher.publishEvent(new EnrollmentRejectedEvent(enrollment));
    }

    public void checkin(Event event, Enrollment enrollment) {
        event.checkin(enrollment);
    }

    public void cancelCheckin(Event event, Enrollment enrollment) {
        event.cancelCheckin(enrollment);
    }
}
