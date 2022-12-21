package com.homedepot.mm.pc.merchantalerting.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserAlertId implements Serializable {
    String ldap;
    UUID alertId;
}
