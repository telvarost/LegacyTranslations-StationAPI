package com.github.telvarost.legacytranslations;

import java.util.HashMap;
import java.util.Properties;

public class TranslationFixHook {
    public static final HashMap<String, String> hashMap_modLocalisation = new HashMap<>();

    public static void add(String s, String s1)
    {
        hashMap_modLocalisation.put(s, s1);
    }

    public static void work(Properties properties)
    {
        for(String s : hashMap_modLocalisation.keySet())
        {
            properties.put(s, hashMap_modLocalisation.get(s));
        }
    }
}
