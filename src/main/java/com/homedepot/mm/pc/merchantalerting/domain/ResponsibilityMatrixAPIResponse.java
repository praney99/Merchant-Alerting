package com.homedepot.mm.pc.merchantalerting.domain;

import lombok.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "user")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponsibilityMatrixAPIResponse {

    @XmlAttribute(name = "sd")
    String subDepartment;
    @XmlAttribute(name = "d")
    String department;
    @XmlAttribute(name = "c")
    String classNumber;
    @XmlAttribute(name = "sc")
    String subClassNumber;
    @XmlAttribute(name = "id")
    String ldap;

    String email;
    String first;
    String last;
    String role;
}
