package com.homedepot.mm.pc.merchantalerting.util;

import com.homedepot.mm.pc.merchantalerting.TestUtils;
import org.junit.jupiter.api.Test;

import static com.homedepot.mm.pc.merchantalerting.TestUtils.getServiceJwtToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JwtUtilsTest {
    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Passed JWT token is incorrect.";

    @Test
    void getSubjectClaim_SubjectIsLdap_ReturnLdap() {
        // This token has `sub` field with `mc62ye` value
        String actualLdap = JwtUtils.getSubjectClaim(TestUtils.getUserJwtToken());

        assertNotNull(actualLdap);
        assertEquals("mc62ye", actualLdap);
    }

    @Test
    void getSubjectClaim_SubjectIsUuid_ReturnUuid() {
        // This token has `sub` field with `a9bcaeee-aacb-11ed-afa1-0242ac120002` value
        String actualUuid = JwtUtils.getSubjectClaim(getServiceJwtToken());

        assertNotNull(actualUuid);
        assertEquals("a9bcaeee-aacb-11ed-afa1-0242ac120002", actualUuid);
    }

    @Test
    void getSubjectClaim_SubjectIsNotLdapAndUuid_ThrowException() {
        // This token has `sub` field with `234234dsf2334` value
        String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImtMMkROWnZKT0hQNjNyS0UtSjR0S3JqRjIwQSIsInBpLmF0bSI6Im" +
                "twY2EifQ.eyJzY29wZSI6Im9wZW5pZCIsImNsaWVudF9pZCI6InNwaWZmZTovL2hvbWVkZXBvdC5kZXYvbXB1bHNlLXVpIiwiaXNzIj" +
                "oiaHR0cHM6Ly9pZGVudGl0eS1xYS5ob21lZGVwb3QuY29tIiwianRpIjoiREVkZml1OWxRYmtGYlViOUljV2FmSHluWHpHWjRKN0EiL" +
                "CJhdWQiOiJzcGlmZmU6Ly9ob21lZGVwb3QuZGV2L21wdWxzZS11aSIsInN1YiI6IjIzNDIzNGRzZjIzMzQiLCJuYmYiOjE2NzU3ODQ3" +
                "NTcsInBpLnNyaSI6IldvZ2JPc2NMT0hLam5UTnlnUzBSUERYSnJvSS5jM05qLjRuSnYubXk2R3FHbUlSVG5JM3VMdkZRejk0dTU0SyI" +
                "sInRva2VuX3R5cGUiOiJhY2Nlc3NfdG9rZW4iLCJpYXQiOjE2NzU3ODQ4NzIsImV4cCI6MTY3NTc4NjY3N30.3N5pcUsDgdtqi3QRJ-" +
                "zlHZkfLxTIZaSeEThJ6OSktOC44IqrYTQELx2iMVPndsmxEqk-sX9-tm6_3XEdk26D7bQuoA7ToigkW772HeJ4EwlD5UTEigHABUtsB" +
                "rLvEv0kPk_LM14lafKgXDvl7aaheCRUJ7qKyJOtzUTmCCHdko9MtJZS_rWbfjPHtibrag_3frz2-Oh2yIT2fN8Bk1dhtqHv1HQ3w64V" +
                "7HbHKD9xEXPLViiB17lr5ByYzag5zcrS1KaIMyXs1wZSmPLXSWiAov58TlC-wLSs15USlhmiwZx3_p63Jkln1vRET8LVJRVMTvgfS6C" +
                "PzfkMABnoDHLgSA";

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> JwtUtils.getSubjectClaim(jwtToken));

        String actualMessage = illegalArgumentException.getMessage();

        assertNotNull(actualMessage);
        assertEquals(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, actualMessage);
    }

    @Test
    void getSubjectClaim_SubjectIsNotPresent_ThrowException() {
        // This token has no field like `sub` at all.
        String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImtMMkROWnZKT0hQNjNyS0UtSjR0S3JqRjIwQSIsInBpLmF0bSI6Im" +
                "twY2EifQ.eyJzY29wZSI6Im9wZW5pZCIsImNsaWVudF9pZCI6InNwaWZmZTovL2hvbWVkZXBvdC5kZXYvbXB1bHNlLXVpIiwiaXNzIj" +
                "oiaHR0cHM6Ly9pZGVudGl0eS1xYS5ob21lZGVwb3QuY29tIiwianRpIjoiREVkZml1OWxRYmtGYlViOUljV2FmSHluWHpHWjRKN0EiL" +
                "CJhdWQiOiJzcGlmZmU6Ly9ob21lZGVwb3QuZGV2L21wdWxzZS11aSIsIm5iZiI6MTY3NTc4NDc1NywicGkuc3JpIjoiV29nYk9zY0xP" +
                "SEtqblROeWdTMFJQRFhKcm9JLmMzTmouNG5Kdi5teTZHcUdtSVJUbkkzdUx2RlF6OTR1NTRLIiwidG9rZW5fdHlwZSI6ImFjY2Vzc19" +
                "0b2tlbiIsImlhdCI6MTY3NTc4NDg3MiwiZXhwIjoxNjc1Nzg2Njc3fQ.M7mpGxFxaKMoZdfvuXvY_Cubo4KZ-PUUsRP8JUDtCL-H9UA" +
                "lcMgDwFM1KsD5NTALYGDGCyW1F9WomZ4YZD50gp3dS6DFkcJeszSs7_2sOM3S7tO3nywa2-RTE6obM642ZHu73XTMw4pYJQalSUIfQi" +
                "Cxn47WwzyMokiG8gRHk6sslZ-p4LPTC7yTZmtXemIsD4r-uan4oUlDzjN03kK3g_Hm6-BC-ih550z6d2OnuTzguKSaxsO9TlhvB11FM" +
                "knY3UxbcSfsY3wTPMoYCj9blB-kcSN754E1WbwI92qhhy_8NIa9XKI6VscJtDrW25GSxzTqVQtCHVnetLrd8S1UWQ";

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> JwtUtils.getSubjectClaim(jwtToken));

        String actualMessage = illegalArgumentException.getMessage();

        assertNotNull(actualMessage);
        assertEquals(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, actualMessage);
    }

    @Test
    void getSubjectClaim_SubjectIsEmpty_ThrowException() {
        // This token has `sub` field with "" value.
        String jwtToken = "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImtMMkROWnZKT0hQNjNyS0UtSjR0S3JqRjIwQSIsInBpLmF0bSI6I" +
                "mtwY2EifQ.eyJzY29wZSI6Im9wZW5pZCIsImNsaWVudF9pZCI6InNwaWZmZTovL2hvbWVkZXBvdC5kZXYvbXB1bHNlLXVpIiwiaXNzI" +
                "joiaHR0cHM6Ly9pZGVudGl0eS1xYS5ob21lZGVwb3QuY29tIiwianRpIjoiREVkZml1OWxRYmtGYlViOUljV2FmSHluWHpHWjRKN0Ei" +
                "LCJhdWQiOiJzcGlmZmU6Ly9ob21lZGVwb3QuZGV2L21wdWxzZS11aSIsInN1YiI6IiIsIm5iZiI6MTY3NTc4NDc1NywicGkuc3JpIjo" +
                "iV29nYk9zY0xPSEtqblROeWdTMFJQRFhKcm9JLmMzTmouNG5Kdi5teTZHcUdtSVJUbkkzdUx2RlF6OTR1NTRLIiwidG9rZW5fdHlwZS" +
                "I6ImFjY2Vzc190b2tlbiIsImlhdCI6MTY3NTc4NDg3MiwiZXhwIjoxNjc1Nzg2Njc3fQ.lZ-RQa2lqIpYMC9-5ur_-FuP25Uw6bQ38F" +
                "yk-DMv282eZ7Wdit7ytDnb_5VGn4w2Wi_Jnc6bDjZ4n3j0W-Q8m8GqIKshqDNQLmUgJQjH1eWdC6dt1Z9G3Un0eqWbYOA52kUeFr3DB" +
                "9KIeeAeE81KA--yqKOxtqJcDGzCIk4w3XT6Yobj0kPfshSV5mPjmUJ4vM-FBQJWzQa_Odlrabi207ZNSIlsNLAf5OtEB_qtM8IVEyzG" +
                "oWKsG61_LAnKlzNiUQRBbJKSVJsFbemhLHspPmMTv06E8M9yBsNisUvSU85A0SG7kIVwuMElt7jN-nLmXBMpaRS_UOFTCRapX5u-tA";

        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> JwtUtils.getSubjectClaim(jwtToken));

        String actualMessage = illegalArgumentException.getMessage();

        assertNotNull(actualMessage);
        assertEquals(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, actualMessage);
    }

    @Test
    void getSubjectClaim_JwtTokenIsNull_ThrowException() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> JwtUtils.getSubjectClaim(null));

        String actualMessage = illegalArgumentException.getMessage();

        assertNotNull(actualMessage);
        assertEquals(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, actualMessage);
    }

    @Test
    void getSubjectClaim_JwtTokenIsEmpty_ThrowException() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> JwtUtils.getSubjectClaim(" "));

        String actualMessage = illegalArgumentException.getMessage();

        assertNotNull(actualMessage);
        assertEquals(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, actualMessage);
    }

    @Test
    void getSubjectClaim_JwtTokenHasSyntaxErrors_ThrowException() {
        IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                () -> JwtUtils.getSubjectClaim("Bearer eyJhbGc.ddssere3.sdd"));

        String actualMessage = illegalArgumentException.getMessage();

        assertNotNull(actualMessage);
        assertEquals(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE, actualMessage);
    }
}
