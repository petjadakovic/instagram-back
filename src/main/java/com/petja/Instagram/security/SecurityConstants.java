package com.petja.Instagram.security;

/**
 * Konstante koje nam trebaju. Poslednja moze da bude bilo koji url zelite da bude za singup formu
 */
public final class SecurityConstants {
    public static final String SECRET = "MojSecretKey";
    public static final long EXPIRATION_TIME = 864000000;
    public static final String TOKEN_PREFIX = "Basic ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/users/register";
    public static final String CONFIRM_TOKEN = "/users/confirm";
    public static final String CHECK_EMAIL = "/users/check-email";
    public static final String CHECK_USERNAME = "/users/check-username";
    public static final String PHOTO_URL = "/users/photo";
    public static final String PHOTO_SMALL_URL = "/users/photo-small";
    public static final String PHOTO_CIRCLE_URL = "/users/photo-circle";
}