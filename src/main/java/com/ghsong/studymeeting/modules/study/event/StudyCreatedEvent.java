package com.ghsong.studymeeting.modules.study.event;

import com.ghsong.studymeeting.modules.study.Study;
import lombok.Getter;

@Getter
public class StudyCreatedEvent {

    private Study study;

    public StudyCreatedEvent(Study newStudy) {
        this.study = newStudy;
    }




}
