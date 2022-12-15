package com.homedepot.mm.pc.merchantalerting.domain;

import com.homedepot.mm.pc.merchantalerting.domain.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertResponse
{
    private List<Alert> alerts;
}
