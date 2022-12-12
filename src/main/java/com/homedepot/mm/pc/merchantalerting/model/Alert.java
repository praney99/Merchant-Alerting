package com.homedepot.mm.pc.merchantalerting.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Alert {

        @Id
        @Column(name ="id")
        private UUID id;
        @Column(name ="keyIdentifiers")
        private String keyIdentifiers;
        @Column(name ="systemSource")
        private String systemSource;
        @Column(name ="type")
        private String type;
        @Column(name ="templateName")
        private String templateName;
        @Column(name ="templateBody")
        private String templateBody;
        @Column(name ="createdBy")
        private String createdBy;
        @Column(name ="createDate")
        private Date createDate;
        @Column(name ="lastUpdatedBy")
        private String lastUpdatedBy;
        @Column(name ="lastUpdateDate")
        private Date lastUpdateDate;
        @Column(name ="expirationDate")
        private Date expirationDate;
}
