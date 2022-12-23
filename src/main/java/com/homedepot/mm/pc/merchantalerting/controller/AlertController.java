package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.Exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;

import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/alert")
public class AlertController {

    private final AlertService alertService;

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

    @Operation(summary = "Delete alerts by alertId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deleted successfully.", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No records found for given param", content = @Content),
            @ApiResponse(responseCode = "500", description = "There is a problem internally to process the request.", content = @Content),
            @ApiResponse(responseCode = "503", description = "There is an error with the server.", content = @Content)})
    @DeleteMapping(value = "/{alertId}", produces = APPLICATION_JSON_VALUE)
    public void deleteAlertsById(@PathVariable("alertId") UUID alertId) {
        alertService.deleteAlertByAlertId(alertId);

    }
}

