package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.model.DCS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserMatrixService {

    final RespMatrixClient responsibilityMatrixClient;


    @Autowired
    public UserMatrixService(RespMatrixClient responsibilityMatrixClient) {
        this.responsibilityMatrixClient = responsibilityMatrixClient;
    }


    public List<String> getUserLDAPForGivenDCS(String dcs) throws Exception {
        String[] dcsArry = dcs.split("-");


        if (dcsArry.length == 3) {
            String cleanDepartment = cleanDCS(dcsArry[0]);
            String cleanClass = cleanDCS(dcsArry[1]);
            String cleanSubClass = cleanDCS(dcsArry[2]);

            return responsibilityMatrixClient.getUsersByDcs(cleanDepartment, cleanClass, cleanSubClass);

        } else {

            throw new ValidationException("Missing param");
        }

    }

    private String cleanDCS(String oldDCSValue){
        String newDCSValue, removedZeros;

        if(oldDCSValue.length() > 2){
            removedZeros = oldDCSValue.replaceFirst("^0+(?!$)", "");

            newDCSValue = removedZeros.replaceAll("([a-zA-Z])", "");

        } else {
            newDCSValue = oldDCSValue;
        }

        return newDCSValue;
    }

}
