package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.model.UserAlert;
import com.homedepot.mm.pc.merchantalerting.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content),
            @ApiResponse(responseCode = "404", description = "No Data Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)})
    @Operation(summary = "Create alerts by LDAP")
    @PostMapping(value = "/user/{ldap}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Alert> createAlertByLdap(@PathVariable("ldap") String ldap, @RequestBody CreateAlertRequest createAlertRequest) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alertService.createAlertByLdap(createAlertRequest, ldap));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content),
            @ApiResponse(responseCode = "404", description = "No Data Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)})
    @Operation(summary = "Create alerts by LDAP")
    @GetMapping(value = "/user/{ldap}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<Alert>> retrieveAlertsByLdap(@PathVariable("ldap") String ldap) {

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(alertService.getAlertsForUser(ldap));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content),
            @ApiResponse(responseCode = "404", description = "No Data Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
            @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)})
    @Operation(summary = "Create alerts by LDAP")
    @GetMapping(value = "/{alertId}", produces = APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Alert> retrieveAlertById(@PathVariable("alertId") String alertId) {
        Optional<Alert> alert = alertService.getAlert(UUID.fromString(alertId));
        return new ResponseEntity<>(alert.get(), HttpStatus.OK);
    }

}

