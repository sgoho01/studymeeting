package com.ghsong.studymeeting.modules.zone;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author : song6
 * Date: 2020-04-30
 * Copyright(©) 2020
 */
@Entity
@Getter @Setter
@Builder @AllArgsConstructor @NoArgsConstructor
public class Zone {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String localNameOfCity;
    @Column(nullable = true)
    private String province;

    @Override
    public String toString() {
        return String.format("%s(%s)/%s", city, localNameOfCity, province);
    }
}
