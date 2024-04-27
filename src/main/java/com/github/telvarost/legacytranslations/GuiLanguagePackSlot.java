package com.github.telvarost.legacytranslations;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import net.minecraft.client.gui.widgets.ScrollableBase;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;

public class GuiLanguagePackSlot extends ScrollableBase {

    public GuiLanguagePackSlot(GuiLanguagePacks guilangpacks)
    {
        super(GuiLanguagePacks.func_22124_a(guilangpacks), guilangpacks.width, guilangpacks.height, 32, (guilangpacks.height - 55) + 4, 26);
        parentLanguagePackGui = guilangpacks;
    }

    @Override
    protected int getSize()
    {
        List list = parentLanguagePackGui.packlist.availableLanguagePacks();
        return list.size();
    }

    @Override
    protected void entryClicked(int i, boolean flag)
    {
        try {
            parentLanguagePackGui.packlist.setLanguagePack(parentLanguagePackGui, i);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isEntrySelected(int i) {
        return parentLanguagePackGui.packlist.availableLanguagePacks().get(i).equals(StringTranslate.langFile);
    }

    @Override
    protected void renderBackground() {
        parentLanguagePackGui.renderBackground();
    }

    @Override
    protected void renderEntry(int i, int j, int k, int l, Tessellator tessellator) {
        TextRenderer fontrenderer = GuiLanguagePacks.func_22127_j(parentLanguagePackGui);
        List<String> list = parentLanguagePackGui.packlist.availableLanguagePacks();
        Map<String, String[]> map = parentLanguagePackGui.packlist.packMetas();
        String title = map.get(list.get(i))[0];
        String author = map.get(list.get(i))[1];
        parentLanguagePackGui.drawTextWithShadowCentred(fontrenderer, title, parentLanguagePackGui.width / 2, k + 1, 0xffffff);
        parentLanguagePackGui.drawTextWithShadowCentred(fontrenderer, author, parentLanguagePackGui.width / 2, k + 12, 0x808080);
    }

    protected int getContentHeight()
    {
        return getSize() * 26;
    }

    final GuiLanguagePacks parentLanguagePackGui; /* synthetic field */

}
