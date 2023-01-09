package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.model.DCS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserMatrixService {

    final RespMatrixClient responsibilityMatrixClient;

    DCS dcs = new DCS();

    @Autowired
    public UserMatrixService(RespMatrixClient responsibilityMatrixClient) {
        this.responsibilityMatrixClient = responsibilityMatrixClient;
    }


    public List<String> getUserLDAPForGivenDCS(String department, String clazz, String subClass) throws Exception {

        String cleanDepartment = cleanSubDepartment(department);

        if (!StringUtils.isNotEmpty(dcs.getDepartment())) {

            return responsibilityMatrixClient.getUserIDs(cleanDepartment, clazz, subClass);

        } else {

            throw new ValidationException("Missing param");
        }

    }

    private String cleanSubDepartment(String oldDepartment){
        String newDepartment, removedZeros;

        if(oldDepartment.length() > 2){
            removedZeros = oldDepartment.replaceFirst("^0+(?!$)", "");

            newDepartment = removedZeros.replaceAll("([a-zA-Z])", "");

        } else {
            newDepartment = oldDepartment;
        }

        return newDepartment;
    }

}
