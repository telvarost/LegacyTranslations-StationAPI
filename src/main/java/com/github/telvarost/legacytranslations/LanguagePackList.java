package com.github.telvarost.legacytranslations;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.util.ScreenScaler;

public class LanguagePackList
{
    private final String defaultLanguagePack = "en_US";
    /** The list of the available texture packs. */
    private List<String> availableLanguagePacks;
    private Map<String, String[]> titleAndAuthorMap = new HashMap<String, String[]>();
    private String defaultLanguagePacks[] = new String[] {"en_US"};

    /** The TexturePack that will be used. */
    public String selectedLanguagePack;

    /** The Minecraft instance used by this TexturePackList */
    private Minecraft mc;

    /** The directory the texture packs will be loaded from. */
    private File languagePackDir;

    public LanguagePackList(Minecraft par1Minecraft, File par2File)
    {
        availableLanguagePacks = new ArrayList<String>();
        mc = par1Minecraft;
        languagePackDir = new File(par2File, "languagepacks");

        if (languagePackDir.exists())
        {
            if (!languagePackDir.isDirectory())
            {
                languagePackDir.delete();
                languagePackDir.mkdirs();
            }
        }
        else
        {
            languagePackDir.mkdirs();
        }
        updateAvaliableLanguagePacks();
    }

    /**
     * Sets the new TexturePack to be used, returning true if it has actually changed, false if nothing changed.
     * @throws FileNotFoundException
     */
    public boolean setLanguagePack(ScreenBase screen, int k) throws FileNotFoundException
    {
        if(StringTranslate.langFile == availableLanguagePacks.get(k))
        {
            return false;
        } else
        {
            selectedLanguagePack = StringTranslate.langFile;
            StringTranslate.langFile = availableLanguagePacks.get(k);
            StringTranslate.reloadKeys();
            mc.options.saveOptions();
            ScreenScaler scaledresolution = new ScreenScaler(mc.options, mc.actualWidth, mc.actualHeight);
            int i = scaledresolution.getScaledWidth();
            int j = scaledresolution.getScaledHeight();
            screen.init(mc, i, j);
            return true;
        }
    }

    /**
     * check the language packs the client has installed
     */
    public void updateAvaliableLanguagePacks()
    {
        ArrayList<String> arraylist = new ArrayList<String>();
        selectedLanguagePack = StringTranslate.langFile;

        if (languagePackDir.exists() && languagePackDir.isDirectory())
        {
            File afile[] = languagePackDir.listFiles();
            File afile1[] = new File[afile.length + defaultLanguagePacks.length];
            for(int i = 0; i < afile.length + defaultLanguagePacks.length; i++) {
                afile1[i] = i < afile.length ? afile[i] : new File((LanguagePackList.class).getResource("/lang/" + defaultLanguagePacks[i - afile.length] + ".lang").getFile());
            }
            int i = afile1.length;

            titleAndAuthorMap.clear();
            for (int j = 0; j < i; j++)
            {
                File file = afile1[j];
                if(file == null) continue;

                if (file.getName().toLowerCase().endsWith(".lang"))
                {
                    try
                    {
                        InputStream in;
                        if(file.isFile()) {
                            in = new FileInputStream(file);
                        } else {
                            in = (LanguagePackList.class).getResourceAsStream("/lang/" + file.getName());
                        }
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                        String title = reader.readLine();
                        String author = reader.readLine();
                        reader.close();
                        boolean hasHeading = title.contains("title|") && author.contains("author|");
                        if(hasHeading) {
                            title = title.replace("title|", "");
                            author = author.replace("author|", "");
                        } else {
                            System.out.println("File " + file.getName() + " is missing a title and/or author, or isn't a valid lang file.");
                            continue;
                        }
                        arraylist.add(file.getName().replace(".lang", ""));
                        titleAndAuthorMap.put(file.getName().replace(".lang", ""), new String[] {title, author});
                    }
                    catch (IOException ioexception)
                    {
                        ioexception.printStackTrace();
                    }
                }
            }
        }

        if (!arraylist.contains(selectedLanguagePack))
        {
            selectedLanguagePack = defaultLanguagePack;
        }

        availableLanguagePacks.clear();
        Collections.sort(arraylist, String.CASE_INSENSITIVE_ORDER);
        availableLanguagePacks = arraylist;
    }

    /**
     * Returns a list of the available texture packs.
     */
    public List<String> availableLanguagePacks()
    {
        return availableLanguagePacks;
    }

    /**
     * Returns a map of the pack descriptions and authors.
     */
    public Map<String, String[]> packMetas()
    {
        return titleAndAuthorMap;
    }
}
