package com.homedepot.mm.pc.merchantalerting.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "alert", schema = "merch_alerts")
public class Alert {

    @Id
    @Column(name = "id")
    private UUID id;
    @Column(name = "key_identifiers")
    private String keyIdentifiers;
    @Column(name = "system_source", nullable = false)
    private String systemSource;
    @Column(name = "alert_type", nullable = false)
    private String alertType;
    @Column(name = "template_name", nullable = false)
    private String templateName;
    @Column(name = "template_body", nullable = false)
    private String templateBody;
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
        this.id = UUID.randomUUID();
    }

    public UUID getId(){
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getKeyIdentifiers() {
        return keyIdentifiers;
    }
    public void setKeyIdentifiers(String keyIdentifiers) {
        this.keyIdentifiers = keyIdentifiers;
    }

    public String getSystemSource() {
        return systemSource;
    }
    public void setSystemSource(String systemSource) {
        this.systemSource = systemSource;
    }

    public String getAlertType() {
        return alertType;
    }
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateBody() {
        return templateBody;
    }
    public void setTemplateBody(String templateBody) {
        this.templateBody = templateBody;
    }

    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
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
