package com.homedepot.mm.pc.merchantalerting.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonType.class)
@Table(name = "alert", schema = "merch_alerts")
public class Alert {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Type(type = "json")
    @Column(name = "key_identifiers", columnDefinition = "jsonb")
    private JsonNode keyIdentifiers;
    @Column(name = "system_source", nullable = false)
    private String systemSource;
    @Column(name = "alert_type", nullable = false)
    private String alertType;
    @Column(name = "template_name", nullable = false)
    private String templateName;
    @Type(type = "json")
    @Column(name = "template_body", columnDefinition = "jsonb", nullable = false)
    private JsonNode templateBody;
    @Column(name = "create_by", nullable = false)
    private String createBy;
    @Column(name = "create_date", nullable = false)
    private Date createDate;
    @Column(name = "last_update_by")
    private String lastUpdateBy;
    @Column(name = "last_update_date")
    private Date lastUpdateDate;
    @Column(name = "expiration_date")
    private Date expirationDate;

}
