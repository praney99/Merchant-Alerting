/*
package com.homedepot.mm.pc.merchantalerting.Exception;

import com.homedepot.mm.pc.merchantalerting.model.DCS;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

public class ValidationDCSException {
    public String validateDCS(String dcs) {
        DCS validatedDCS = new DCS();
        if (dcs.length()>12 || dcs.length()<11) {
            throw new ValidationException("402", HttpStatus.CONFLICT,"The Size of your dcs its not appropriate example:001A-001-001 or 001-001-001");
        }
        String A= StringUtils.getDigits(dcs);
        if ((Character.isAlphabetic(dcs.charAt(3)))) {
            validatedDCS.setDepartment(A.substring(0, 3));
        } else {
            validatedDCS.setSubDepartment(A.substring(0, 3));
        }
        validatedDCS.setClassNumber(A.substring(3,6));
        validatedDCS.setSubClassNumber(A.substring(6,9));

        if (validatedDCS.getClassNumber().isEmpty() || validatedDCS.getClassNumber().equals("000") || validatedDCS.getClassNumber().length()==2) {
            throw new ValidationException("402",HttpStatus.NOT_ACCEPTABLE,"The classNumber should not be empty and Need to be numeric example: 001");
        }
        if (validatedDCS.getSubClassNumber().isEmpty() || validatedDCS.getSubClassNumber().equals("000")|| validatedDCS.getSubClassNumber().length()==2) {
            throw new ValidationException("402",HttpStatus.NOT_ACCEPTABLE,"The SubclassNumber should not be empty and Need to be numeric example: 001");
        } else {
            return StringUtils.getDigits(validatedDCS.getDCS());
        }
    }
}
*/
