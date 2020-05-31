package com.ghsong.studymeeting.study;

import com.ghsong.studymeeting.domain.Account;
import com.ghsong.studymeeting.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : song6
 * Date: 2020-05-31
 * Copyright(©) 2020
 */
@Service
@Transactional
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    public Study createNewStudy(Study study, Account account) {
        Study newStudy = studyRepository.save(study);
        newStudy.addManager(account);
        return newStudy;
    }

}
