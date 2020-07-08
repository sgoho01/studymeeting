package com.ghsong.studymeeting.modules.zone.form;

import com.ghsong.studymeeting.modules.zone.Zone;
import lombok.Data;

/**
 * @author : song6
 * Date: 2020-04-30
 * Copyright(©) 2020
 */
@Data
public class ZoneForm {

    private String zoneName;

    public String getCityName() {
        return zoneName.substring(0, zoneName.indexOf("("));
    }

    public String getLocalNameOfCity() {
        return zoneName.substring(zoneName.indexOf("(") + 1, zoneName.indexOf("/"));
    }

    public String getProvinceName() {
        return zoneName.substring(zoneName.indexOf("/")+1);
    }

    public Zone toEntity() {
        return Zone.builder()
                .city(this.getCityName())
                .localNameOfCity(this.getLocalNameOfCity())
                .province(this.getProvinceName())
                .build();
    }

}
