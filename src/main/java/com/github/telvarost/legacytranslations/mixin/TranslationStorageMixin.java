package com.github.telvarost.legacytranslations.mixin;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

import com.github.telvarost.legacytranslations.ModHelper;
import com.github.telvarost.legacytranslations.TranslationFixHook;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {
    @Shadow private static TranslationStorage INSTANCE;

    @Shadow private Properties translations;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void TranslationStorageMixin(CallbackInfo ci) {
        ModHelper.reloadKeys();
    }

    @Inject(
            method = "get(Ljava/lang/String;)Ljava/lang/String;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void get(String key, CallbackInfoReturnable<String> cir) {
        if (ModHelper.ModHelperFields.outputMissingKeysConsole || ModHelper.ModHelperFields.outputMissingKeysFile) {
            if (null != key && !key.isBlank() && !ModHelper.ModHelperFields.keysWithoutTranslations.contains(key)) {
                String translatedString = this.translations.getProperty(key, key);
                if (key.equals(translatedString)) {
                    ModHelper.ModHelperFields.keysWithoutTranslations.add(key);

                    if (ModHelper.ModHelperFields.outputMissingKeysConsole) {
                        System.out.println(ModHelper.ModHelperFields.langFile + " Missing: " + key);
                    }

                    if (ModHelper.ModHelperFields.outputMissingKeysFile) {
                        writeMissingTranslationToFile(key);
                    }
                }
            }
        }
    }

    @Redirect(
            method = "get(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Properties;getProperty(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;"
            )
    )
    public String get(Properties instance, String key, String defaultValue) {
        String translatedString = instance.getProperty(key, defaultValue);
        if (ModHelper.ModHelperFields.outputMissingKeysConsole || ModHelper.ModHelperFields.outputMissingKeysFile) {
            if (null != key && !key.isBlank() && !ModHelper.ModHelperFields.keysWithoutTranslations.contains(key)) {
                if (key.equals(translatedString)) {
                    ModHelper.ModHelperFields.keysWithoutTranslations.add(key);

                    if (ModHelper.ModHelperFields.outputMissingKeysConsole) {
                        System.out.println(ModHelper.ModHelperFields.langFile + " Missing: " + key);
                    }

                    if (ModHelper.ModHelperFields.outputMissingKeysFile) {
                        writeMissingTranslationToFile(key);
                    }
                }
            }
        }
        return translatedString;
    }

    @Environment(EnvType.CLIENT)
    @Inject(
            method = "get(Ljava/lang/String;)Ljava/lang/String;",
            at = @At("HEAD"),
            cancellable = true
    )
    public void getClientTranslation(String key, CallbackInfoReturnable<String> cir) {
        if (ModHelper.ModHelperFields.outputMissingKeysConsole || ModHelper.ModHelperFields.outputMissingKeysFile) {
            if (null != key && !key.isBlank() && !ModHelper.ModHelperFields.keysWithoutTranslations.contains(key)) {
                String translatedString = this.translations.getProperty(key + ".name", key);
                if (key.equals(translatedString)) {
                    ModHelper.ModHelperFields.keysWithoutTranslations.add(key);

                    if (ModHelper.ModHelperFields.outputMissingKeysConsole) {
                        System.out.println(ModHelper.ModHelperFields.langFile + " Missing: " + key);
                    }

                    if (ModHelper.ModHelperFields.outputMissingKeysFile) {
                        writeMissingTranslationToFile(key);
                    }
                }
            }
        }
    }

    @Unique
    private void writeMissingTranslationToFile(String missingKey) {
        try {
            String fileName = "missing_" + ModHelper.ModHelperFields.langFile + ".lang";
            String fileDirectory = "legacytranslations";
            File directory = new File(fileDirectory);
            File missingTranslationFile = new File(fileDirectory + '/' + fileName);
            if (!directory.exists()){
                directory.mkdir();
            }
            if (!missingTranslationFile.exists()) {
                missingTranslationFile.createNewFile();
            }
            Scanner scanner = new Scanner(missingTranslationFile);
            PrintWriter writer = new PrintWriter(new FileWriter(missingTranslationFile,true));

            boolean found = false;
            while (scanner.hasNextLine())
            {
                String lineFromFile = scanner.nextLine();
                if (lineFromFile.equals(missingKey + "="))
                {
                    found = true;
                    break;
                }
            }

            if (!found) {
                writer.write(missingKey + "=\n");
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Failed to write missing translation key to missing_" + ModHelper.ModHelperFields.langFile + ".lang");
            e.printStackTrace();
        }
    }
}