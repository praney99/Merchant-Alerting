package com.homedepot.mm.pc.merchantalerting.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetrieveAlertResponse {

        @javax.persistence.Id
        @Column(name ="alertId")
        private UUID alertId;

        private String keyIdentifiers;
        private String systemSource;
        private String type;
        private String templateName;
        private String templateBody;
        private String createdBy;
        private Date createDate;
        private String lastUpdatedBy;
        private Date lastUpdateDate;
        private Date expirationDate;
}
