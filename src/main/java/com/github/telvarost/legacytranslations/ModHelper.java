package com.github.telvarost.legacytranslations;

import com.github.telvarost.legacytranslations.mixin.TranslationStorageAccessor;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Unique;

import java.io.*;
import java.util.Properties;

public class ModHelper {

    private static boolean starting = true;
    private static String splashesFile = "";

    public static void reloadKeys() {
        if (null == TranslationStorage.getInstance()) {
            return;
        }
        try
        {
            ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations().load(new InputStreamReader((TranslationStorage.class).getResourceAsStream("/assets/legacytranslations/lang/en_US.lang"), "UTF-8"));
            System.out.println("Loading \"en_US\" lang table...");
            File settings = new File("options.txt");
            if(starting) {
                if(!settings.exists())
                {
                    if(ModHelper.ModHelperFields.langFile == null || !ModHelper.ModHelperFields.langFile.equals("en_US")) {
                        ModHelper.ModHelperFields.langFile = "en_US";
                        System.err.println("Had trouble finding options.txt, using en_US instead.");
                    }
                } else {
                    BufferedReader bufferedreader = new BufferedReader(new FileReader(settings));
                    boolean closed = false;
                    for(String s = ""; (s = bufferedreader.readLine()) != null;)
                    {
                        String as[] = s.split(":");
                        if(as[0].equals("langFile"))
                        {
                            ModHelper.ModHelperFields.langFile = as[1];
                            bufferedreader.close();
                            closed = true;
                            break;
                        }
                    }
                    if(!closed) {
                        ModHelper.ModHelperFields.langFile = "en_US";
                        bufferedreader.close();
                        System.out.println("\"langFile:\" option is missing from options.txt. It will be created on the next save.");
                    }
                }
            }
            if(TranslationStorage.getInstance() != null) {
                TranslationFixHook.work(((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations());
            }
            starting = false;
            InputStreamReader reader;
            try {
                reader = new InputStreamReader(new FileInputStream("./languagepacks/" + ModHelper.ModHelperFields.langFile + ".lang"), "UTF-8");
            } catch (FileNotFoundException e) {
                reader = new InputStreamReader((TranslationStorage.class).getResourceAsStream("/lang/" + ModHelper.ModHelperFields.langFile + ".lang"), "UTF-8");
            }
            InputStreamReader reader2;
            try {
                reader2 = new InputStreamReader(new FileInputStream("./languagepacks/" + ModHelper.ModHelperFields.langFile + ".lang"), "UTF-8");
            } catch (FileNotFoundException e) {
                reader2 = new InputStreamReader((TranslationStorage.class).getResourceAsStream("/lang/" + ModHelper.ModHelperFields.langFile + ".lang"), "UTF-8");
            }
            if(!ModHelper.ModHelperFields.langFile.equals("en_US")) {
                System.out.println("Loading \"" + ModHelper.ModHelperFields.langFile + "\" lang table...");
                ((TranslationStorageAccessor) TranslationStorage.getInstance()).getTranslations().load(reader);
            }
            boolean useDefaultSplashes = ModHelper.ModHelperFields.langFile.equals("en_US");
            BufferedReader lineReader = new BufferedReader(reader2);
            splashesFile = "";
            for(int i = 0; i < 3; i++) {
                String line = lineReader.readLine();
                if(line.startsWith("#")) {
                    while(lineReader.readLine().startsWith("#"));
                }
                if(line.startsWith("splashes|")) {
                    splashesFile = line.replace("splashes|", "");
                    useDefaultSplashes = false;
                }
                if(splashesFile.equals("")) {
                    useDefaultSplashes = true;
                }
            }
            if(useDefaultSplashes) {
                splashesFile = "jar:/title/splashes.txt";
                if(!ModHelper.ModHelperFields.langFile.equals("en_US"))
                    System.out.println("Current lang file has not defined a splashes file. Using the default one.");
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public static class ModHelperFields {
        public static String langFile;
    }
}
