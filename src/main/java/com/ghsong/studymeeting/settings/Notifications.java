package com.ghsong.studymeeting.settings;

import com.ghsong.studymeeting.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author : song6
 * Date: 2020-04-19
 * Copyright(©) 2020
 */
@Data
@NoArgsConstructor
public class Notifications {

    private boolean studyCreateByEmail;

    private boolean studyCreateByWeb;

    private boolean studyEnrollmentResultByEmail;

    private boolean studyEnrollmentResultByWeb;

    private boolean studyUpdateByEmail;

    private boolean studyUpdateByWeb;

    public Notifications(Account account) {
        this.studyCreateByEmail = account.isStudyCreatedByEmail();
        this.studyCreateByWeb = account.isStudyCreatedByWeb();
        this.studyEnrollmentResultByEmail = account.isStudyEnrollmentResultByEmail();
        this.studyEnrollmentResultByWeb = account.isStudyEnrollmentResultByWeb();
        this.studyUpdateByEmail = account.isStudyUpdatedByEmail();
        this.studyUpdateByWeb = account.isStudyUpdatedByWeb();
    }
}
