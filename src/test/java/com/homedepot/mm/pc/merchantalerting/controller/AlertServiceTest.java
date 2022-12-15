package com.homedepot.mm.pc.merchantalerting.controller;

import com.homedepot.mm.pc.merchantalerting.dao.AlertInfoDAO;
import com.homedepot.mm.pc.merchantalerting.model.Alert;
import com.homedepot.mm.pc.merchantalerting.processor.AlertService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AlertServiceTest {

    @MockBean
    AlertInfoDAO alertInfoDAO;
    @Autowired
    AlertService alertService;

    private Alert alert;

    @BeforeEach
    void setup() throws JSONException {
        MockitoAnnotations.initMocks(this);

         alert= new Alert();
        alert.setId(UUID.fromString("c0533e1f-f452-4747-8293-a43cf168ad3f"));
        alert.setKeyIdentifiers(new JSONObject()
                .put("sku", "123456").put("cpi", "0.98").toString());
        alert.setSystemSource("My Assortment");
        alert.setType("Regional Assortment");
        alert.setTemplateName("default");
        alert.setTemplateBody(new JSONObject().put("title", "test1")
                .put("titleDescription", "test2")
                .put("primaryText1", "test3")
                .put("primaryLink", "test4").toString());
        alert.setCreatedBy("UMQ8QTG");
        alert.setCreateDate(new Date());
        alert.setLastUpdatedBy("UMQ8QTG");
        alert.setLastUpdateDate(new Date());
        alert.setExpirationDate(new Date());
    }
    @Test
    void DeleteAlert() throws JSONException {
        Mockito.doNothing().when(alertInfoDAO).deleteById(Mockito.any());
        assertNotNull(alert);//Cheking the aler not null
        //Cheking the output of the method.
        assertEquals("The AlertId: "+alert.getId()+" has been deleted",alertService.deleteAlertByAlertId(alert.getId()));
        // Cheking With Another Alert
        System.out.println(alert.getId());
        alert.setId(UUID.randomUUID());
        assertEquals("The AlertId: "+alert.getId()+" has been deleted",alertService.deleteAlertByAlertId(alert.getId()));
        System.out.println(alert.getId());
    }
}
