package com.ra11p0.utils;

import com.ra11p0.frames.Init;

import java.nio.charset.StandardCharsets;

public class LangResource {
    public static String get(String key){
        return new String(Init.localeBundle.getString(key).getBytes(StandardCharsets.ISO_8859_1));
    }
}
