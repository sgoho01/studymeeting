package com.ghsong.studymeeting.modules.study.event;

import com.ghsong.studymeeting.modules.study.Study;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudyCreatedEvent {

    private final Study study;

}
