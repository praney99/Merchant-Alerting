package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.domain.UserDCSRequest;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.model.DCS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class UserMatrixService {

    final RespMatrixClient responsibilityMatrixClient;
    final static String[] PARAMS = {"d", "c", "sc"};

    DCS dcs = new DCS();

    @Autowired
    public UserMatrixService(RespMatrixClient responsibilityMatrixClient) {
        this.responsibilityMatrixClient = responsibilityMatrixClient;
    }


    public List<String> getUserLDAPForGivenDCS(String department, String clazz, String subClass) throws Exception {

        if (!StringUtils.isNotEmpty(dcs.getDepartment())) {

            return responsibilityMatrixClient.getUserIDs(department, clazz, subClass);

        } else {

            throw new ValidationException("Missing param");
        }

    }
//    private static String buildRequestParams(String paramValue, UserDCSRequest userDCSRequest) {
//        //If param string matches sub-deparment format ex. "026p" then truncate values to only get dept "26"
//        //This only works if sub-department always comes as the format above
//
//
//        return StringUtils.join(PARAMS[0] + "=" + paramValue,
//                "&" + PARAMS[1] + "=" + userDCSRequest.getClassNumber(),
//                "&" + PARAMS[2] + "=" + userDCSRequest.getSubClassNumber());
//    }
}
