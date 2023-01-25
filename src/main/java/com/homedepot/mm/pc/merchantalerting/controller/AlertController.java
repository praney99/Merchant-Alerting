package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@SecurityRequirement(name = "PingFed")
@RequestMapping(value = "/alert")
@Validated
public class AlertController {

    final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content)})
    @Operation(summary = "Create alert by LDAP.")
    @PostMapping(value = "/user/{ldap}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Alert> createAlertByLdap(@PathVariable("ldap") String ldap, @RequestBody @Valid CreateAlertRequest createAlertRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alertService.createAlertByLdap(createAlertRequest, ldap));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content)})
    @Operation(summary = "Creates an alert, and assigns the alert to all users who manage the input DCS.")
    @PostMapping(value = "/dcs/{dcs}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Alert> createAlertByDCS(
            @PathVariable("dcs") @Pattern(regexp = "^((\\d{1,3}[a-zA-Z])|(\\d{1,4}))-\\d{1,3}-\\d{1,3}$") String dcs,
            @RequestBody @Valid CreateAlertRequest createAlertRequest
    ) {
        Alert alert = alertService.createAlertByDCS(createAlertRequest, dcs);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alert);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content)})
    @Operation(summary = "Retrieve alerts by LDAP.")
    @GetMapping(value = "/user/{ldap}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Alert>> retrieveAlertsByLdap(@PathVariable("ldap") String ldap) {
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alertService.getAlertsByLdap(ldap));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content)})
    @Operation(summary = "Retrieve alerts by alert id.")
    @GetMapping(value = "/{alertId}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Alert> retrieveAlertById(@PathVariable("alertId") UUID alertId) {
        Optional<Alert> alert = alertService.getAlert(alertId);
        return alert.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully.", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing.", content = @Content)})
    @Operation(summary = "Delete alert by alert id.")
    @DeleteMapping(value = "/{alertId}", produces = APPLICATION_JSON_VALUE)
    public void deleteAlertById(@PathVariable("alertId") UUID alertId) {
        alertService.deleteAlert(alertId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dismissed successfully.", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing.", content = @Content)})
    @Operation(summary = "Dismiss user alert.")
    @PostMapping(value = "/user/{ldap}/dismiss", produces = APPLICATION_JSON_VALUE)
    public void dismissUserAlert(@PathVariable("ldap") String ldap, @RequestBody Map<UUID, Boolean> alertDismissalStates) {
        alertService.dismissAlert(ldap, alertDismissalStates);
    }

}
