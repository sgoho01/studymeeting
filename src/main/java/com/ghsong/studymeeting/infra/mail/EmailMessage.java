package com.ghsong.studymeeting.infra.mail;

import lombok.Builder;
import lombok.Data;

/**
 * @author : song6
 * Date: 2020-05-17
 * Copyright(©) 2020
 */
@Data
@Builder
public class EmailMessage {

    private String to;
    private String subject;
    private String message;
}
