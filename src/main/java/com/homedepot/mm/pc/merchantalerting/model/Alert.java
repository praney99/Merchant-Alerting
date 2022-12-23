package com.homedepot.mm.pc.merchantalerting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "alert", schema = "merch_alerts")
@TypeDef(name = "json", typeClass = JsonType.class)
public class Alert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Type(type = "json")
    @Column(name = "key_identifiers", columnDefinition = "jsonb")
    @JsonIgnore
    private JsonNode keyIdentifiers;

    @Column(name = "system_source", nullable = false)
    private String systemSource;
    @Column(name = "alert_type", nullable = false)
    private String alertType;
    @Column(name = "template_name", nullable = false)
    private String templateName;

    @Type(type = "json")
    @Column(name = "template_body", columnDefinition = "jsonb", nullable = false)
    @JsonIgnore
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


    public Alert() {
    }

    @OneToMany(mappedBy="alert", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<UserAlert> users;

    public List<UserAlert> getUsers() {
        return users;
    }

    public void setUsers(List<UserAlert> users) {
        this.users = users;
    }

    public void addUsers(UserAlert user){
        if(this.users == null){
            this.users = new ArrayList<UserAlert>();
        }
        this.users.add(user);
        user.setAlert(this);
    }

    public void removeUsers(UserAlert user){
        if(this.users != null){
            users.remove(user);
            user.setAlert(null);
        }
    }

}
