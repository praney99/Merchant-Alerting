package com.homedepot.mm.pc.merchantalerting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@IdClass(UserAlertId.class)
@Table(name = "user_alert", schema = "merch_alerts")
public class UserAlert {

    @Id
    @Column(name = "ldap")
    private String ldap;
    @Id
    @Column(name = "alert_id")
    private UUID alertId;
    @Column(name = "is_dismissed")
    private Boolean isDismissed;
    @Column(name = "last_updated")
    private Timestamp lastUpdated;
    @Column(name = "last_update_by")
    private String lastUpdateBy;

    @ManyToOne
    @JoinColumn(name="alert_id", nullable=false, insertable=false, updatable=false)
    private Alert alert;

    public UserAlert(String ldap, UUID alertId) {
        this.ldap = ldap;
        this.alertId = alertId;
        this.isDismissed = false;
        this.lastUpdated = null;
        this.lastUpdateBy = null;
    }

}
