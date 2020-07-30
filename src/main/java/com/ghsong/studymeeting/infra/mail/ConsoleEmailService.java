package com.ghsong.studymeeting.infra.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * @author : song6
 * Date: 2020-05-17
 * Copyright(©) 2020
 */
@Slf4j
@Profile({"local", "test"})
@Component
public class ConsoleEmailService implements EmailService{

    @Override
    public void sendEmail(EmailMessage emailMessage) {
        log.info("sent email : {}", emailMessage.getMessage());
    }
}
