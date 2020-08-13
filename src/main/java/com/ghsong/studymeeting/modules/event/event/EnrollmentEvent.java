package com.ghsong.studymeeting.modules.event.event;

import com.ghsong.studymeeting.modules.event.Enrollment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class EnrollmentEvent {

    private final Enrollment enrollment;

    private final String message;
}
