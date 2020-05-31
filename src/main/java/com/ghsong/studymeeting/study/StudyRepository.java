package com.ghsong.studymeeting.study;

import com.ghsong.studymeeting.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : song6
 * Date: 2020-05-31
 * Copyright(©) 2020
 */
@Transactional(readOnly = true)
public interface StudyRepository extends JpaRepository<Study, Long> {
    boolean existsByPath(String path);
}
