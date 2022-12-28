package com.homedepot.mm.pc.merchantalerting.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.homedepot.mm.pc.merchantalerting.exception.NotFoundException;
import com.homedepot.mm.pc.merchantalerting.exception.ValidationException;
import com.homedepot.mm.pc.merchantalerting.domain.UserDCSRequest;
import com.homedepot.mm.pc.merchantalerting.service.UserMatrixService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
public class UserMatrixClientControllerTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private UserMatrixService userMatrixService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    private MockMvc mvc;

    @Test
    void testGetUserLDAPForGivenDCS() throws Exception {
        UserDCSRequest request = UserDCSRequest.builder()
                .department("26")
                .classNumber("1")
                .subClassNumber("3")
                .build();

        Mockito.doReturn(new ArrayList<>()).when(userMatrixService).getUserLDAPForGivenDCS(Mockito.any(UserDCSRequest.class));

        Gson gson = new GsonBuilder().create();

        this.mvc.perform(
                get("/matrix-client/rs/findUser")
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(200));

    }

    @Test
    void testGetUserLDAPForGivenDCSThrowValidationException() throws Exception {

        UserDCSRequest request = UserDCSRequest.builder()
                .department("30")
                .classNumber("1")
                .subClassNumber("3")
                .build();

        Mockito.doThrow(new ValidationException()).when(userMatrixService).getUserLDAPForGivenDCS(Mockito.any(UserDCSRequest.class));

        Gson gson = new GsonBuilder().create();

        this.mvc.perform(
                get("/matrix-client/rs/findUser")
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(400));

    }

    @Test
    void testGetUserLDAPForGivenDCSThrowNotFoundException() throws Exception {

        UserDCSRequest request = UserDCSRequest.builder()
                .department("30")
                .classNumber("1")
                .subClassNumber("3")
                .build();

        Mockito.doThrow(new NotFoundException()).when(userMatrixService).getUserLDAPForGivenDCS(Mockito.any(UserDCSRequest.class));

        Gson gson = new GsonBuilder().create();

        this.mvc.perform(
                get("/matrix-client/rs/findUser")
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(404));

    }

    @Test
    void testGetUserLDAPForGivenDCSThrowResponseStatusException() throws Exception {
        UserDCSRequest request = UserDCSRequest.builder()
                .department("30")
                .classNumber("1")
                .subClassNumber("3")
                .build();

        Mockito.doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)).when(userMatrixService).getUserLDAPForGivenDCS(Mockito.any(UserDCSRequest.class));

        Gson gson = new GsonBuilder().create();

        this.mvc.perform(
                get("/matrix-client/rs/findUser")
                        .content(gson.toJson(request))
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().is(500));
    }
}
