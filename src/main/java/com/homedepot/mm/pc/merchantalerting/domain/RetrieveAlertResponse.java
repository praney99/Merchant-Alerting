package com.homedepot.mm.pc.merchantalerting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveAlertResponse {

        private String id;
        private String keyIdentifiers;
        private String systemSource;
        private String type;
        private String templateName;
        private String templateBody;
        private String createdBy;
        private String createDate;
        private String lastUpdatedBy;
        private String lastUpdateDate;
        private String expirationDate;

}
