package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.client.ResponsibilityMatrixClient;
import com.homedepot.mm.pc.merchantalerting.domain.UserDCSRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMatrixService {

    final ResponsibilityMatrixClient responsibilityMatrixClient;
    final static String[] PARAMS = {"d", "c", "sc"};

    @Autowired
    public UserMatrixService(ResponsibilityMatrixClient responsibilityMatrixClient) {
        this.responsibilityMatrixClient = responsibilityMatrixClient;
    }

    public List<String> getUserLDAPForGivenDCS(UserDCSRequest userDCSRequest) {

        if (!StringUtils.isNotEmpty(userDCSRequest.getSubDepartment())) {

            return responsibilityMatrixClient.fetchLDAP(buildRequestParams(userDCSRequest.getDepartment(), userDCSRequest));

        } else {

            return responsibilityMatrixClient.fetchLDAP(buildRequestParams(String.valueOf(
                    Math.abs(Integer.parseInt(StringUtils.getDigits(
                            userDCSRequest.getSubDepartment()
                    )))), userDCSRequest));
        }

    }
    private static String buildRequestParams(String paramValue, UserDCSRequest userDCSRequest) {
        return StringUtils.join(PARAMS[0] + "=" + paramValue,
                "&" + PARAMS[1] + "=" + userDCSRequest.getClassNumber(),
                "&" + PARAMS[2] + "=" + userDCSRequest.getSubClassNumber());
    }
}
