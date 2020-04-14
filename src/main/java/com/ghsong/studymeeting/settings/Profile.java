package com.ghsong.studymeeting.settings;

import com.ghsong.studymeeting.domain.Account;
import lombok.Data;

/**
 * @author : song6
 * Date: 2020-04-14
 * Copyright(©) 2020
 */
@Data
public class Profile {

    private String bio;

    private String url;

    private String occupation;

    private String location;

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }
}
