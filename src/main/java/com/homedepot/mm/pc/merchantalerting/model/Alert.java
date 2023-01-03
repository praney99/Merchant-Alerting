package com.homedepot.mm.pc.merchantalerting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

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
    @Column(name = "created", nullable = false)
    private Timestamp created;
    @Column(name = "last_update_by")
    private String lastUpdateBy;
    @Column(name = "last_updated")
    private Timestamp lastUpdated;
    @Column(name = "expiration_date")
    private Date expirationDate;

    @JsonIgnore
    @OneToMany(mappedBy="alert", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<UserAlert> userAlerts;
}
