package com.github.telvarost.legacytranslations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Properties;

import net.minecraft.client.Minecraft;

public class StringTranslate
{

    private StringTranslate()
    {
        reloadKeys();
    }

    public static StringTranslate getInstance()
    {
        return instance;
    }

    public String translateKey(String s)
    {
        return translateTable.getProperty(s, s);
    }

    public String translateKeyFormat(String s, Object aobj[])
    {
        String s1 = translateTable.getProperty(s, s);
        return String.format(s1, aobj);
    }

    public String translateNamedKey(String s)
    {
        return translateTable.getProperty((new StringBuilder()).append(s).append(".name").toString(), "");
    }

    public static String langFile;
    private static boolean starting = true;
    public static String splashesFile = "";

    public static void reloadKeys() {
        translateTable = new Properties();
        try
        {
            translateTable.load(new InputStreamReader((StringTranslate.class).getResourceAsStream("/lang/en_US.lang"), "UTF-8"));
            System.out.println("Loading \"en_US\" lang table...");
            File settings = new File("options.txt");
            if(starting) {
                if(!settings.exists())
                {
                    if(langFile == null || !langFile.equals("en_US")) {
                        langFile = "en_US";
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
                            langFile = as[1];
                            bufferedreader.close();
                            closed = true;
                            break;
                        }
                    }
                    if(!closed) {
                        langFile = "en_US";
                        bufferedreader.close();
                        System.out.println("\"langFile:\" option is missing from options.txt. It will be created on the next save.");
                    }
                }
            }
            if(instance != null) {
                TranslationFixHook.work(translateTable);
            }
            starting = false;
            InputStreamReader reader;
            try {
                reader = new InputStreamReader(new FileInputStream("./languagepacks/" + langFile + ".lang"), "UTF-8");
            } catch (FileNotFoundException e) {
                reader = new InputStreamReader((StringTranslate.class).getResourceAsStream("/lang/" + langFile + ".lang"), "UTF-8");
            }
            InputStreamReader reader2;
            try {
                reader2 = new InputStreamReader(new FileInputStream("./languagepacks/" + langFile + ".lang"), "UTF-8");
            } catch (FileNotFoundException e) {
                reader2 = new InputStreamReader((StringTranslate.class).getResourceAsStream("/lang/" + langFile + ".lang"), "UTF-8");
            }
            if(!langFile.equals("en_US")) {
                System.out.println("Loading \"" + langFile + "\" lang table...");
                translateTable.load(reader);
            }
            boolean useDefaultSplashes = langFile.equals("en_US");
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
                if(!langFile.equals("en_US"))
                    System.out.println("Current lang file has not defined a splashes file. Using the default one.");
            }
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    private static StringTranslate instance = new StringTranslate();
    private static Properties translateTable;
}
