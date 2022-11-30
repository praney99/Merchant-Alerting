package com.homedepot.mm.pc.merchantalerting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveAlertResponse {

        private UUID id;
        private String keyIdentifiers;
        private String systemSource;
        private String type;
        private String templateName;
        private String templateBody;
        private String createdBy;
        private Date createDate;
        private String lastUpdatedBy;
        private Date lastUpdateDate;
        private String expirationDate;

}
