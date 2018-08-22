package pl.netia.gateway.security;

import java.util.Arrays;
import java.util.List;

class SecurityConstants {
    static final List<String> ALLOWED_ORIGIN_METHODS = Arrays.asList("GET", "POST", "PUT","DELETE");
    static final String SECRET = SecretGenerator.getSecret();
    static final long EXPIRATION_TIME = 864_000_000; // 10 days
    static final String TOKEN_PREFIX = "Bearer ";
    static final String HEADER_STRING = "Authorization";
    static final String SIGN_UP_URL = "/users/sign-up";
}
