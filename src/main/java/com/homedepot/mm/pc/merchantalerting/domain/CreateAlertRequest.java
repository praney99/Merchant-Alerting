package com.homedepot.mm.pc.merchantalerting.domain;
import com.homedepot.mm.pc.merchantalerting.model.Alert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONObject;


import java.util.Date;
import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {

        private String systemSource;
        private String AlertType;
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
                alert.setKeyIdentifiers(this.getKeyIdentifiers() == null ? null :(this.getKeyIdentifiers().toString()));
                alert.setSystemSource(this.getSystemSource());
                alert.setAlertType(this.getAlertType());
                alert.setTemplateName(this.getTemplateName());
                alert.setTemplateBody(this.getTemplateBody().toString());
                alert.setExpirationDate(this.getExpirationDate() == null ? null : new Date(Long.parseLong(this.getExpirationDate())));
        }
}