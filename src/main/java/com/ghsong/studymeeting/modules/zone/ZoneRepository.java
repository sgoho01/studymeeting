package com.ghsong.studymeeting.modules.zone;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author : song6
 * Date: 2020-04-30
 * Copyright(©) 2020
 */
@Transactional(readOnly = true)
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findByCityAndProvince(String city, String province);
}
