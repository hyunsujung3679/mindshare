package com.hsj.aft.common.constants;

import java.time.Duration;

public class Constants {

    public static final String VIEW_COUNT_KEY = "post:view:%d";
    public static final String POST_CACHE_KEY = "post:data:%d";
    public static final String LIST_CACHE_KEY = "post:list:%s:%s:%d:%d:%s:%s";
    public static final Duration POST_CACHE_TTL = Duration.ofMinutes(10);
    public static final Duration LIST_CACHE_TTL = Duration.ofMinutes(10);

    public static final String PASSWORD_NOT_MATCH_CODE = "400";
    public static final String LOGIN_FAIL_CODE = "401";
    public static final String LOGIN_REQUIRED_CODE = "402";
    public static final String NO_AUTHORIZATION_CODE = "403";
    public static final String ENTITY_NOT_FOUND_CODE = "404";
    public static final String METHOD_NOT_ALLOWED_CODE = "405";
    public static final String MESSAGE_NOT_READABLE_CODE = "406";
    public static final String METHOD_ARGUMENT_NOT_VALID_CODE = "407";
    public static final String DUPLICATE_ID_CODE = "409";


}
