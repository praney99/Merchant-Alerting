package com.homedepot.mm.pc.merchantalerting.domain;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "json", typeClass = JsonType.class)
public class Alert {

        @Id
        @Column(name ="id")
        private UUID id;

        @Type(type = "json")
        @Column(name ="keyIdentifiers", columnDefinition = "jsonb")
        private JSONObject keyIdentifiers = new JSONObject();
        @Column(name ="systemSource")
        private String systemSource;
        @Column(name ="type")
        private String type;
        @Column(name ="templateName")
        private String templateName;
        @Type(type = "json")
        @Column(name ="templateBody", columnDefinition = "jsonb")
        private JSONObject templateBody = new JSONObject();
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
