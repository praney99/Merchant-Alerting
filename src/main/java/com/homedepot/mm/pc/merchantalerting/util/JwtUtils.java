package com.homedepot.mm.pc.merchantalerting.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

@Slf4j
public final class JwtUtils {
    private static final String HOME_DEPOT_LDAP_REGEXP = "^[a-zA-Z][a-zA-Z0-9]{3,6}$";
    private static final Pattern HOME_DEPOT_LDAP_PATTERN = Pattern.compile(HOME_DEPOT_LDAP_REGEXP);
    private static final String UUID_REGEXP = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";
    private static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEXP);
    private static final int INDEX_AFTER_BEARER = 7;

    /**
     * Extracts LDAP or UUID from `sub` claims of JWT token.
     *
     * @param jwtToken is token that has `sub` claims with LDAP or UUID value
     * @return extracted LDAP or UUID from token
     * @throws IllegalArgumentException in case if token hasn't LDAP or UUID in the `sub` claims or token has syntax
     *                                  errors and cannot be parsed.
     */
    public static String getSubjectClaim(String jwtToken) {
        if (StringUtils.isNotBlank(jwtToken)) {

            if (jwtToken.startsWith("Bearer")) {
                jwtToken = jwtToken.substring(INDEX_AFTER_BEARER);
            }

            String subjectValue = null;

            try {
                DecodedJWT decodedJWT = JWT.decode(jwtToken);

                Claim jwtSubject = decodedJWT.getClaim("sub");

                if (!jwtSubject.isMissing()) {
                    subjectValue = jwtSubject.asString().trim();

                    if (isLdap(subjectValue) || isUuid(subjectValue)) {
                        return subjectValue;
                    }
                }
            } catch (JWTDecodeException ex) {
                log.error("Could not extract subject from JWT token: {}", ex.getMessage());
            }
            log.error("LDAP or UUID is empty or incorrect: `{}`", subjectValue);
        }
        throw new IllegalArgumentException("Passed JWT token is incorrect.");
    }

    /**
     * Checks if passed `value` is match to the Home Depot pattern for the LDAP id.
     *
     * @param value is any string which needs to check
     * @return `true` in case if passed value matches, `false` if not.
     */
    private static boolean isLdap(String value) {
        return HOME_DEPOT_LDAP_PATTERN.matcher(value).matches();
    }

    /**
     * Checks if passed `value` is match to the UUID pattern.
     *
     * @param value is any string which needs to check
     * @return `true` in case if passed value matches, `false` if not.
     */
    private static boolean isUuid(String value) {
        return UUID_PATTERN.matcher(value).matches();
    }

    private JwtUtils() {
        throw new UnsupportedOperationException("Could not initiate util class.");
    }
}
