package com.github.telvarost.legacytranslations;

import blue.endless.jankson.Comment;
import net.glasslauncher.mods.api.gcapi.api.ConfigName;
import net.glasslauncher.mods.api.gcapi.api.GConfig;

public class Config {

    @GConfig(value = "config", visibleName = "LegacyTranslations")
    public static ConfigFields config = new ConfigFields();

    public static class ConfigFields {

        @ConfigName("Test Config")
        @Comment("Config for testing purposes")
        public Boolean testConfig = false;
    }
}