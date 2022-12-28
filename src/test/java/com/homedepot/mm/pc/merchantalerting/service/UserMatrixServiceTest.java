package com.homedepot.mm.pc.merchantalerting.service;

import com.homedepot.mm.pc.merchantalerting.exception.NotFoundException;
import com.homedepot.mm.pc.merchantalerting.client.ResponsibilityMatrixClient;
import com.homedepot.mm.pc.merchantalerting.domain.UserDCSRequest;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
    ResponsibilityMatrixClient responsibilityMatrixClient;


    @Test
    void testGetUserLDAPForGivenDCS() {

        List<String> ldapListResponse = new ArrayList<>();
        ldapListResponse.add("VPE80Z2");
        ldapListResponse.add("FOOOOOO");
        ldapListResponse.add("FO42BRO");

        Mockito.doReturn(ldapListResponse).when(responsibilityMatrixClient).fetchLDAP(Mockito.anyString());

        Executable executableUserMatrixService = () -> {
            List<String> ldapList = userMatrixService.getUserLDAPForGivenDCS(UserDCSRequest.builder().build());

            assertEquals(3, ldapList.size());
            assertNotNull(ldapList);
            assertFalse(ldapList.isEmpty());

        };

        assertDoesNotThrow(executableUserMatrixService);

    }

    @Test
    void testGetUserLDAPForGivenDCSThrowValidationException() {

        Mockito.doThrow(new ValidationException()).when(responsibilityMatrixClient).fetchLDAP(Mockito.anyString());

        assertThrows(ValidationException.class, () -> userMatrixService.getUserLDAPForGivenDCS(UserDCSRequest.builder().build()));
    }

    @Test
    void testGetUserLDAPForGivenDCSThrowNotFoundException() {

        Mockito.doThrow(new NotFoundException()).when(responsibilityMatrixClient).fetchLDAP(Mockito.anyString());

        assertThrows(NotFoundException.class, () -> userMatrixService.getUserLDAPForGivenDCS(UserDCSRequest.builder().build()));

    }

    @Test
    void testGetUserLDAPForGivenDCSThrowResponseStatusException() {

        Mockito.doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)).when(responsibilityMatrixClient).fetchLDAP(Mockito.anyString());

        assertThrows(ResponseStatusException.class, () -> userMatrixService.getUserLDAPForGivenDCS(UserDCSRequest.builder().build()));

    }


}
