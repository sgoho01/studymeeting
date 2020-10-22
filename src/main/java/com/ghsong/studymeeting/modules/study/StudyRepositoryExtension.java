package com.ghsong.studymeeting.modules.study;

import com.ghsong.studymeeting.modules.account.Account;
import com.ghsong.studymeeting.modules.tag.Tag;
import com.ghsong.studymeeting.modules.zone.Zone;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface StudyRepositoryExtension {

    Page<Study> findByKeyword(String keyword, Pageable pageable);

    List<Study> findByAccount(Set<Tag> tags, Set<Zone> zones);
}
