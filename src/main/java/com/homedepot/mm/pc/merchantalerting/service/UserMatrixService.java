package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.model.DCS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
public class UserMatrixService {

    final RespMatrixClient responsibilityMatrixClient;

    public UserMatrixService(RespMatrixClient responsibilityMatrixClient) {
        this.responsibilityMatrixClient = responsibilityMatrixClient;
    }

    public List<String> getUserLDAPForGivenDCS(String dcs) {
        String[] dcsArray = dcs.split("-");

        if (dcsArray.length == 3) {
            String cleanDepartment = cleanDCS(dcsArray[0]);
            String cleanClass = cleanDCS(dcsArray[1]);
            String cleanSubClass = cleanDCS(dcsArray[2]);

            return responsibilityMatrixClient.getUsersByDcs(cleanDepartment, cleanClass, cleanSubClass);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "DCS malformed. Must be in the format: 001A-001-001 or 001-001-001");
        }

    }

    private String cleanDCS(String oldDCSValue) {
        return oldDCSValue.replaceFirst("^0+(?!$)", "")
                .replaceAll("([a-zA-Z])", "");
    }

}
