package com.homedepot.mm.pc.merchantalerting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;


import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;


@Data
public class CombinedAlertDTO {
    @JsonProperty("id")
    private UUID id;
    private JsonNode keyIdentifiers;
    private String systemSource;
    private String alertType;
    private String templateName;

    private JsonNode templateBody;

    private String createBy;

    private Timestamp created;

    private String lastUpdateBy;

    private Timestamp lastUpdated;

    private Date expirationDate;

    private Boolean isRead;

    private Boolean isDismissed;

}
