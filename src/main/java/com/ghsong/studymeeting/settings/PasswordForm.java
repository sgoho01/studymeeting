package com.ghsong.studymeeting.settings;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author : song6
 * Date: 2020-04-15
 * Copyright(©) 2020
 */
@Data
public class PasswordForm {

    @Length(min = 8, max = 50)
    private String newPassword;

    @Length(min = 8, max = 50)
    private String newPasswordConfirm;
}
