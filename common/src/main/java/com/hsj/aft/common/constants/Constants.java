package com.hsj.aft.common.constants;

import java.time.Duration;

public class Constants {

    public static final String VIEW_COUNT_KEY = "post:view:%d";
    public static final String POST_CACHE_KEY = "post:data:%d";
    public static final String LIST_CACHE_KEY = "post:list:%s:%s:%d:%d:%s";
    public static final Duration POST_CACHE_TTL = Duration.ofMinutes(10);
    public static final Duration LIST_CACHE_TTL = Duration.ofMinutes(10);
    public static final String REFRESH_TOKEN_PREFIX = "RT:";
    public static final String BLACKLIST_PREFIX = "BL:";

    public static final String SUCCESS_CODE = "SUCCESS";

    public static final String PASSWORD_NOT_MATCH_CODE = "LOG01";
    public static final String LOGIN_FAIL_CODE = "LOG02";
    public static final String LOGIN_REQUIRED_CODE = "LOG03";
    public static final String NO_AUTHORIZATION_CODE = "LOG04";
    public static final String DUPLICATE_ID_CODE = "LOG05";

    public static final String METHOD_ARGUMENT_NOT_VALID_CODE = "COM01";
    public static final String MESSAGE_NOT_READABLE_CODE = "COM02";
    public static final String ENTITY_NOT_FOUND_CODE = "COM03";
    public static final String METHOD_NOT_ALLOWED_CODE = "COM04";
    public static final String NOT_FOUND_CODE = "COM05";

    public static final String TOKEN_ERROR_CODE = "TOK01";
    public static final String INVALID_TOKEN_CODE = "TOK02";
    public static final String TOKEN_EXPIRED_CODE = "TOK03";
    public static final String INVALID_REFRESH_TOKEN_CODE = "TOK04";
    public static final String TOKEN_BLACK_LIST_CODE = "TOK05";


}
