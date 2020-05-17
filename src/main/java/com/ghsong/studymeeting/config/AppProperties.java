package com.ghsong.studymeeting.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author : song6
 * Date: 2020-05-17
 * Copyright(©) 2020
 */
@Data
@Component
@ConfigurationProperties("app")
public class AppProperties {

    private String host;
}
