package com.homedepot.mm.pc.merchantalerting.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import java.sql.Date;
import java.util.Map;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {

    private String systemSource;
    private String type;
    private String templateName;
    private Map<String,String> templateBody;
    private String expirationDate;
    private Map<String,String> keyIdentifiers;

    /**
     * Maps the alert request to the internal alert model.
     * Generates a new UUID for the new alert object.
     * @return New alert
     */
    public Alert toAlert() {
        Alert alert = new Alert();
        toAlert(alert);
        return alert;
    }

    /**
     * Maps the alert request to an existing alert model.
     * Overwrites existing properties on the alert with those from the request.
     * @param alert Alert model object
     */
    public void toAlert(Alert alert) {
        ObjectMapper mapper = new ObjectMapper();
        alert.setKeyIdentifiers(this.getKeyIdentifiers() == null ? null : mapper.convertValue(this.getKeyIdentifiers(), JSONObject.class));
        alert.setSystemSource(this.getSystemSource());
        alert.setAlertType(this.getType());
        alert.setTemplateName(this.getTemplateName());
        alert.setTemplateBody(mapper.convertValue(this.getTemplateBody(), JSONObject.class));
        alert.setExpirationDate(this.getExpirationDate() == null ? null : Date.valueOf(LocalDate.parse(this.getExpirationDate())));
    }
}

