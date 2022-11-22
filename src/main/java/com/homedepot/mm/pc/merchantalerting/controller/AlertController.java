package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.processor.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

    @RestController
    @RequestMapping(value = "/")
    public class AlertController {
        @Autowired
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
        @Operation(summary = "Create alerts by LDAP")
        @PostMapping(value = "/create", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
        @ResponseBody
        public ResponseEntity<String> generateAlertByLdap(@RequestBody CreateAlertRequest createAlertRequest) {

            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
                    .body(alertService.createAlertByUser(createAlertRequest));
        }
    }
