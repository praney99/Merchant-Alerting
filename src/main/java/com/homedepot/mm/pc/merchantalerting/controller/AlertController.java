package com.homedepot.mm.pc.merchantalerting.controller;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.processor.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

    @RestController
    @RequestMapping(value = "/alert")
    public class AlertController {
        AlertService alertService;

        public AlertController(AlertService alertService) {
            this.alertService = alertService;
        }


        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
                @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content),
                @ApiResponse(responseCode = "404", description = "No Data Found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)})
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
                @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content),
                @ApiResponse(responseCode = "404", description = "No Data Found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)})
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
                @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content),
                @ApiResponse(responseCode = "404", description = "No Data Found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)})
        @Operation(summary = "Retrieve alerts by alert id.")
        @GetMapping(value = "/{alertId}", produces = APPLICATION_JSON_VALUE)
        @ResponseBody
        public ResponseEntity<Alert> retrieveAlertById(@PathVariable("alertId") String alertId) {
            Optional<Alert> alert = alertService.getAlert(UUID.fromString(alertId));
            return new ResponseEntity<>(alert.orElse(null), HttpStatus.OK);
        }

        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
                @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing", content = @Content),
                @ApiResponse(responseCode = "404", description = "No Data Found", content = @Content),
                @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content),
                @ApiResponse(responseCode = "503", description = "Service Unavailable", content = @Content)})
        @Operation(summary = "Create alerts by DCS")
        @PostMapping(value = "/dcs/{dcs}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
        @ResponseBody
        public ResponseEntity<UUID> generateAlertByDCS(@PathVariable("dcs") @NotNull String dcs,@RequestBody @NotNull CreateAlertRequest createAlertRequest) {

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(alertService.createAlertByDCS(createAlertRequest, dcs));
        }

        @Operation(summary = "Delete alerts by alertId")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Deleted successfully.", content = {@Content(mediaType = APPLICATION_JSON_VALUE)}),
                @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing.", content = @Content),
                @ApiResponse(responseCode = "404", description = "No records found for given param", content = @Content),
                @ApiResponse(responseCode = "500", description = "There is a problem internally to process the request.", content = @Content),
                @ApiResponse(responseCode = "503", description = "There is an error with the server.", content = @Content)})
        @DeleteMapping(value = "/{alertId}", produces = APPLICATION_JSON_VALUE)
        public void deleteAlertsById(@PathVariable("alertId") @NotNull UUID alertId)
        {
            alertService.deleteAlertByAlertId(alertId);

        }


        private boolean isUserIdsInfoInputValid(String userId) {

            return !StringUtils.isEmpty(userId);
        }

    }

