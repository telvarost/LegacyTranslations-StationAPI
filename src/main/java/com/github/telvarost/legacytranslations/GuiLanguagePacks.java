package com.github.telvarost.legacytranslations;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import net.minecraft.client.resource.language.TranslationStorage;
import org.lwjgl.Sys;

import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionButtonWidget;

public class GuiLanguagePacks extends Screen {

    protected Screen guiScreen;
    private int field_6454_o;
    public LanguagePackList packlist;
    private GuiLanguagePackSlot guiLanguagePackSlot;

    public GuiLanguagePacks(Screen guiscreen) {
        field_6454_o = -1;
        guiScreen = guiscreen;
    }

    public void init() {
        TranslationStorage stringtranslate = TranslationStorage.getInstance();
        buttons.add(new OptionButtonWidget(5, width / 2 - 154, height - 48,
                stringtranslate.get("languagePack.openFolder")));
        buttons.add(new OptionButtonWidget(6, width / 2 + 4, height - 48,
                stringtranslate.get("gui.done")));
        packlist = new LanguagePackList(minecraft, Minecraft.getRunDirectory());
        packlist.updateAvailableLanguagePacks();
        guiLanguagePackSlot = new GuiLanguagePackSlot(this);
        guiLanguagePackSlot.registerButtons(buttons, 7, 8);
    }

    protected void buttonClicked(ButtonWidget guibutton) {
        if (!guibutton.active) {
            return;
        }
        if (guibutton.id == 5) {
            //Sys.openURL((new StringBuilder()).append("file://").append(mc.getMinecraftDir()).append("/languagepacks").toString());
            try {
                Desktop.getDesktop().open(new File("languagepacks"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (guibutton.id == 6) {
            //minecraft.textureManager.reloadTexturesFromTexturePack();
            minecraft.setScreen(guiScreen);
        } else {
            guiLanguagePackSlot.buttonClicked(guibutton);
        }
    }

    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
    }

    protected void mouseReleased(int i, int j, int k) {
        super.mouseReleased(i, j, k);
    }

    public void render(int i, int j, float f) {
        guiLanguagePackSlot.render(i, j, f);
        TranslationStorage stringtranslate = TranslationStorage.getInstance();
        drawCenteredTextWithShadow(textRenderer,
                stringtranslate.get("languagePack.title"), width / 2,
                16, 0xffffff);
        drawCenteredTextWithShadow(textRenderer,
                stringtranslate.get("languagePack.folderInfo"),
                width / 2 - 77, height - 26, 0x808080);
        super.render(i, j, f);
//		guiTexturePackSlot.registerScrollButtons(controlList, 7, 8);
    }

    protected void keyPressed(char c, int i)
    {
        if(i == 1 && guiScreen instanceof TitleScreen)
        {
            minecraft.setScreen(guiScreen);
        } else {
            super.keyPressed(c, i);
        }
    }

    public void tick() {
        super.tick();
        field_6454_o--;
    }

    static Minecraft func_22124_a(GuiLanguagePacks guilanguagepacks) {
        return guilanguagepacks.minecraft;
    }

    static TextRenderer func_22127_j(GuiLanguagePacks guilanguagepacks) {
        return guilanguagepacks.textRenderer;
    }
}
