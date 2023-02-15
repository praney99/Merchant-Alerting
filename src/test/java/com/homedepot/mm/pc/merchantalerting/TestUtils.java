package com.homedepot.mm.pc.merchantalerting;

import java.util.Map;

public final class TestUtils {

    public static final Map<String, String> DEFAULT_TEMPLATE = Map.of(
            "title", "title",
            "titleDescription", "title description",
            "primaryText1", "primary text 1",
            "primaryText2", "primary text 2",
            "tertiaryText", "tertiary text",
            "primaryLinkText", "link",
            "primaryLinkUri", "http://localhost:8080"
    );

    public static final Map<String, String> DEFAULT_KEY_IDENTIFIERS = Map.of(
            "sku", "123456",
            "cpi", "0.98"
    );

    /**
     * Returns JWT (PingFed) token where `sub` is equal to `mc62ye` LDAP id.
     */
    public static String getUserJwtToken() {
        return "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImtMMkROWnZKT0hQNjNyS0UtSjR0S3JqRjIwQSIsInBpLmF0bSI6ImtwY2EifQ.eyJzY2" +
                "9wZSI6Im9wZW5pZCIsImNsaWVudF9pZCI6InNwaWZmZTovL2hvbWVkZXBvdC5kZXYvbXB1bHNlLXVpIiwiaXNzIjoiaHR0cHM6Ly9pZ" +
                "GVudGl0eS1xYS5ob21lZGVwb3QuY29tIiwianRpIjoiREVkZml1OWxRYmtGYlViOUljV2FmSHluWHpHWjRKN0EiLCJhdWQiOiJzcGlm" +
                "ZmU6Ly9ob21lZGVwb3QuZGV2L21wdWxzZS11aSIsInN1YiI6Im1jNjJ5ZSIsIm5iZiI6MTY3NTc4NDc1NywicGkuc3JpIjoiV29nYk9" +
                "zY0xPSEtqblROeWdTMFJQRFhKcm9JLmMzTmouNG5Kdi5teTZHcUdtSVJUbkkzdUx2RlF6OTR1NTRLIiwidG9rZW5fdHlwZSI6ImFjY2" +
                "Vzc190b2tlbiIsImlhdCI6MTY3NTc4NDg3MiwiZXhwIjoxNjc1Nzg2Njc3fQ.GulVIfCBezBxRdlvBLF_YpWLlX84k1iFpNI_EeB3Nu" +
                "BCWaMDqqPVC2Yxy1kntwISWX9v_sjpB533Yc_r5ibdZl6n_68bSiF-jp_d37Tb_BgToGbHXZQrlnEhQEgcCgds-nMM0MKDsrYRexMZN" +
                "PQGjuZqE6I0T697vArnWO0xnSTXAoA5UDgn7mWbDN9Lb0CKk-WuIvEPelpmGDmNJr566Euh3Kqf2BPxN-hhLnpdY4wLhSfiGvXeAxcM" +
                "aHiYUXqnrVf9j4cY6HgAeA3DgWIHXn7SgS0brjBL2c4CTxYxBnYyBDNae6Z_nMxgb03nCeTuXAJGfFLXJvfkZLxbsOmGSw";
    }

    /**
     * Returns JWT (PingFed) token where `sub` is equal to `a9bcaeee-aacb-11ed-afa1-0242ac120002` uuid.
     */
    public static String getServiceJwtToken() {
        return "Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6ImtMMkROWnZKT0hQNjNyS0UtSjR0S3JqRjIwQSIsInBpLmF0bSI6ImtwY2EifQ.eyJzY2" +
                "9wZSI6Im9wZW5pZCIsImNsaWVudF9pZCI6InNwaWZmZTovL2hvbWVkZXBvdC5kZXYvbXB1bHNlLXVpIiwiaXNzIjoiaHR0cHM6Ly9pZ" +
                "GVudGl0eS1xYS5ob21lZGVwb3QuY29tIiwianRpIjoiREVkZml1OWxRYmtGYlViOUljV2FmSHluWHpHWjRKN0EiLCJhdWQiOiJzcGlm" +
                "ZmU6Ly9ob21lZGVwb3QuZGV2L21wdWxzZS11aSIsInN1YiI6ImE5YmNhZWVlLWFhY2ItMTFlZC1hZmExLTAyNDJhYzEyMDAwMiIsIm5" +
                "iZiI6MTY3NTc4NDc1NywicGkuc3JpIjoiV29nYk9zY0xPSEtqblROeWdTMFJQRFhKcm9JLmMzTmouNG5Kdi5teTZHcUdtSVJUbkkzdU" +
                "x2RlF6OTR1NTRLIiwidG9rZW5fdHlwZSI6ImFjY2Vzc190b2tlbiIsImlhdCI6MTY3NTc4NDg3MiwiZXhwIjoxNjc1Nzg2Njc3fQ.Ff" +
                "aksVipUwKQgG4f1rdNKuNZwaYceah0s3awQiO2GUuC39QYwj4_7MWOgzki_Wv5Qjs49WlDK8UlGBNexfELeIF-veKq89U2qnDGaapdh" +
                "MUOL7zkSFKgTV5hgJr38N90vOnIbMhHWe-BO3IBC7ni9M11q87c_LK_WgrQ1nW39V2-9g_tJ4SRcyrWmsu48fOowfFqXanHxmKO2nJF" +
                "l0MFS1AgcGuNhF9dJSvXVUEApwLGOBH-hy_Wj_HQ6B__oGIJn8XEKlipZ8N1CS2cJh71mijkIoXneDI1jjnJROX-Up6p3FdnZ1IcD9c" +
                "MQtmqgjJoKekgf_PgeICXDCCRq4OVWA";
    }

    private TestUtils() {
        throw new UnsupportedOperationException("Could not initiate util class.");
    }
}
