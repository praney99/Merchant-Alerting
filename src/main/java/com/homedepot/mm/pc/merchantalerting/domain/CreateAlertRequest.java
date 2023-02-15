package com.homedepot.mm.pc.merchantalerting.domain;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.homedepot.mm.pc.merchantalerting.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static com.homedepot.mm.pc.merchantalerting.constants.AlertConstants.DEFAULT_EXPIRATION_DAYS;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlertRequest {

    @NotEmpty
    private String systemSource;
    @NotEmpty
    private String type;
    private AlertTemplateType templateName;
    private Map<String, String> templateBody = new HashMap<>();
    @Future
    private LocalDate expirationDate;
    private Map<String, String> keyIdentifiers = new HashMap<>();

    /**
     * Maps the alert request to the internal alert model.
     * Generates a new UUID for the new alert object.
     * @return New alert
     */
    public Alert toAlert() {
        Alert alert = new Alert();

        try {
            ObjectMapper mapper = new ObjectMapper();
            alert.setKeyIdentifiers(this.getKeyIdentifiers() == null ? null : mapper.convertValue(this.getKeyIdentifiers(), JsonNode.class));
            alert.setSystemSource(this.getSystemSource());
            alert.setAlertType(this.getType());
            alert.setTemplateName(this.getTemplateName().toString().toLowerCase());
            alert.setTemplateBody(mapper.convertValue(this.getTemplateBody(), JsonNode.class));
            alert.setExpirationDate(this.getExpirationDate() == null ? Date.valueOf(LocalDate.now().plusDays(DEFAULT_EXPIRATION_DAYS)) : Date.valueOf(this.getExpirationDate()));
            alert.setCreateBy(this.getSystemSource());
            alert.setCreated(new Timestamp(System.currentTimeMillis()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Alert request is malformed.");
        }

        return alert;
    }

}
