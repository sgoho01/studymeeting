package com.ghsong.studymeeting.modules.account.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * @author : song6
 * Date: 2020-04-14
 * Copyright(©) 2020
 */
@Data
public class Profile {

    @Length(max = 35)
    private String bio;

    @Length(max = 50)
    private String url;

    @Length(max = 50)
    private String occupation;

    @Length(max = 50)
    private String location;

    private String profileImage;

}
