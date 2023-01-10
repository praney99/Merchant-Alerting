package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.client.RespMatrixClient;
import com.homedepot.mm.pc.merchantalerting.exception.NotFoundException;
import com.homedepot.mm.pc.merchantalerting.client.ResponsibilityMatrixClient;
import com.homedepot.mm.pc.merchantalerting.domain.UserDCSRequest;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class UserMatrixServiceTest {

    @Autowired
    UserMatrixService userMatrixService;

    @MockBean
    RespMatrixClient responsibilityMatrixClient;


    @Test
    void testGetUserLDAPForGivenDCS() throws Exception {

        List<String> ldapListResponse = new ArrayList<String>();
        ldapListResponse.add("VPE80Z2");
        ldapListResponse.add("FOOOOOO");
        ldapListResponse.add("FO42BRO");
        String testDCS = "026p-001-003";

        Mockito.doReturn(ldapListResponse).when(responsibilityMatrixClient)
                .getUsersByDcs(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        Executable executableUserMatrixService = () -> {
            List<String> ldapList = userMatrixService.getUserLDAPForGivenDCS(testDCS);

            assertEquals(3, ldapList.size());
            assertNotNull(ldapList);
            assertFalse(ldapList.isEmpty());

        };

        assertDoesNotThrow(executableUserMatrixService);

    }

    @Test
    void testGetUserLDAPForGivenDCSThrowValidationException() throws JSONException {
        String testDCS = "001-003";

        Mockito.doThrow(new ValidationException("Missing param")).when(responsibilityMatrixClient)
                .getUsersByDcs(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        assertThrows(ValidationException.class, () -> userMatrixService.getUserLDAPForGivenDCS(testDCS));
    }

}
