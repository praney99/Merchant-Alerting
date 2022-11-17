package com.homedepot.mm.pc.merchantalerting.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CreateAlertRequest {

        @Builder
        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class AlertRequest {

                private String systemSource;
                private String type;
                private String keyIdentifiers;
                private String templateName;
                private String templateBody;
                private String expirationDate;

                public String getSystemSource() {
                        return systemSource;
                }

                public void setSystemSource(String systemSource) {
                        this.systemSource = systemSource;
                }

                public String getType() {
                        return type;
                }

                public void setType(String type) {
                        this.type = type;
                }

                public String getKeyIdentifiers() {
                        return keyIdentifiers;
                }

                public void setKeyIdentifiers(String keyIdentifiers) {
                        this.keyIdentifiers = keyIdentifiers;
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

                public String getExpirationDate() {
                        return expirationDate;
                }

                public void setExpirationDate(String expirationDate) {
                        this.expirationDate = expirationDate;
                }


        }
}