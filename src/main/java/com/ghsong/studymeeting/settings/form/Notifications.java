package com.ghsong.studymeeting.settings.form;

import com.ghsong.studymeeting.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : song6
 * Date: 2020-04-19
 * Copyright(©) 2020
 */
@Data
public class Notifications {

    private boolean studyCreatedByEmail;

    private boolean studyCreatedByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdatedByEmail;

    private boolean studyUpdatedByWeb;

}
