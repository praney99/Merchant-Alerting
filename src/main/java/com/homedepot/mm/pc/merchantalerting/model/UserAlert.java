package com.homedepot.mm.pc.merchantalerting.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@Table(name = "user_alert", schema = "alerting_schema")
public class UserAlert {


    @Column(name = "id")
    private long id;
    @Column(name = "ldap")
    private String ldap;
    @Column(name = "alert_id")
    private UUID alertId;
    @Column(name = "dismiss_date")
    private Date dismissDate;


    public UserAlert() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getLdap() {
        return ldap;
    }
    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

    public UUID getAlertId() {
        return alertId;
    }
    public void setAlertId(UUID alertId) {
        this.alertId = alertId;
    }

    public Date getDismissDate() {
        return dismissDate;
    }
    public void setDismissDate(Date dismissDate){
        this.dismissDate = dismissDate;
    }

}
