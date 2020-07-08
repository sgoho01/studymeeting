package com.ghsong.studymeeting.modules.account.form;

import lombok.Data;

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
