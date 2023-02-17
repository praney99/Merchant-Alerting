package com.homedepot.mm.pc.merchantalerting.util;

import com.homedepot.mm.pc.merchantalerting.entity.Alert;
import com.homedepot.mm.pc.merchantalerting.model.CombinedAlertDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        componentModel = "spring"
)
public interface AlertDTOMapper {

    CombinedAlertDTO alertToCombinedAlertDTO(Alert alerts);

    List<CombinedAlertDTO> alertToCombinedAlertDTOs (List<Alert> alerts);

    Alert toAlert(CombinedAlertDTO combinedAlertDTO);

}
