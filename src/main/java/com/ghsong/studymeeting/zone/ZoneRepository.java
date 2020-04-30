package com.ghsong.studymeeting.zone;

import com.ghsong.studymeeting.domain.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : song6
 * Date: 2020-04-30
 * Copyright(©) 2020
 */
@Transactional(readOnly = true)
public interface ZoneRepository extends JpaRepository<Zone, Long> {
}
