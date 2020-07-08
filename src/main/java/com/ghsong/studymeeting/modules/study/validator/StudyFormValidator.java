package com.ghsong.studymeeting.modules.study.validator;

import com.ghsong.studymeeting.modules.study.StudyRepository;
import com.ghsong.studymeeting.modules.study.form.StudyForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author : song6
 * Date: 2020-05-31
 * Copyright(©) 2020
 */
@Component
@RequiredArgsConstructor
public class StudyFormValidator implements Validator {

    private final StudyRepository studyRepository;


    @Override
    public boolean supports(Class<?> aClass) {
        return StudyForm.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        StudyForm studyForm = (StudyForm) o;
        if(studyRepository.existsByPath(studyForm.getPath())){
            errors.rejectValue("path", "wrong.path", "스터디 경로값을 사용할 수 없습니다.");
        }
    }
}
