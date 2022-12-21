package com.homedepot.mm.pc.merchantalerting.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.util.UUID;

@Entity
@IdClass(UserAlertId.class)
@Getter
@Setter
@NoArgsConstructor
@Table(name = "user_alert", schema = "merch_alerts")
public class UserAlert {

    @Id
    @Column(name = "ldap")
    private String ldap;
    @Id
    @Column(name = "alert_id")
    private UUID alertId;
    @Column(name = "dismiss_date")
    private Date dismissDate;

    @ManyToOne
    @JoinColumn(name="alert_id", nullable=false, insertable = false, updatable = false)
    private Alert alert;

    public Alert getAlert() {
        return alert;
    }
    public void setAlert(Alert alert) {
        this.alert = alert;
    }
}
