package com.homedepot.mm.pc.merchantalerting.util;

import com.homedepot.mm.pc.merchantalerting.Exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.domain.CreateAlertRequest;

public class AlertValidator {

    public static void validateAlertRequest(CreateAlertRequest request) throws ValidationException {
        TemplateValidator.validateAlertTemplate(
                request.getTemplateName(),
                request.getTemplateBody()
        );
    }
}
