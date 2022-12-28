package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.domain.UserDCSRequest;
import com.homedepot.mm.pc.merchantalerting.service.UserMatrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserMatrixController {

    final UserMatrixService userMatrixService;

    @Autowired
    public UserMatrixController(UserMatrixService userMatrixService) {
        this.userMatrixService = userMatrixService;
    }


    @GetMapping(value = "/matrix-client/rs/findUser", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @Operation(description = "Consumes an API from responsibility matrix for user details" +
            " and this endpoint retrieve a list of user's LDAP for given D-C-S or SD-C-S.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "LDAP list successfully returned."),
            @ApiResponse(responseCode = "400", description = "Invalid Input supplied or input parameters missing.", content = @Content),
            @ApiResponse(responseCode = "404", description = "No matches found with the parameters given."),
            @ApiResponse(responseCode = "500", description = "An error occurred while processing the request")
    })
    public ResponseEntity<List<String>> retrieveUserLDAPByDCS(@Valid @RequestBody UserDCSRequest
                                                                      userDCSRequest) {

        validateDepartmentOrSubDepartment(userDCSRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(userMatrixService.getUserLDAPForGivenDCS(userDCSRequest));

    }

    private void validateDepartmentOrSubDepartment(UserDCSRequest userDCSRequest) {
        if (StringUtils.isNotBlank(userDCSRequest.getDepartment()) && StringUtils.isNotBlank(userDCSRequest.getSubDepartment())) {
            throw new ValidationException("You must submit the request with department or sub-department param, not both");
        } else if (!StringUtils.isNotBlank(userDCSRequest.getDepartment()) && !StringUtils.isNotBlank(userDCSRequest.getSubDepartment())) {
            throw new ValidationException("You cannot make the request, mandatory department or sub-department param");
        }
    }
}
