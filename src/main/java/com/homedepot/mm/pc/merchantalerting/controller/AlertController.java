package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import com.homedepot.mm.pc.merchantalerting.service.UserMatrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

    @RestController
    @SecurityRequirement(name = "PingFed")
    @RequestMapping(value = "/alert")
    public class AlertController {
        @Autowired
        AlertService alertService;

        UserMatrixService userMatrixService;


    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content)})
    @Operation(summary = "Create alert by LDAP.")
    @PostMapping(value = "/user/{ldap}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Alert> createAlertByLdap(@PathVariable("ldap") String ldap, @RequestBody CreateAlertRequest createAlertRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alertService.createAlertByLdap(createAlertRequest, ldap));
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
    @Operation(summary = "Delete alert by alert id")
    @DeleteMapping(value = "/{alertId}", produces = APPLICATION_JSON_VALUE)
    public void deleteAlertById(@PathVariable("alertId") UUID alertId) {
        alertService.deleteAlert(alertId);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content)})
    @Operation(summary = "Create alerts by DCS")
    @PostMapping(value = "/dcs/{d}-{c}-{s}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Alert> generateAlertByDCS(@PathVariable @NotNull String d, String c, String s, @RequestBody CreateAlertRequest createAlertRequest) throws Exception {

        List<String> userIds = userMatrixService.getUserLDAPForGivenDCS(d,c,s);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alertService
                        .createAlertWithLdapAssociations(createAlertRequest, userIds));
    }
}
