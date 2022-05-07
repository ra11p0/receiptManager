package com.ra11p0;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleBundle {
    public static ResourceBundle languageBundle;
    public static void setLanguage(String language){
        Locale locale = new Locale(language);
        languageBundle = ResourceBundle.getBundle("lang", locale);
    }
    public static String get(String key){ return languageBundle.getString(key); }
}
