package com.github.telvarost.legacytranslations;

import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.render.TextRenderer;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;

public class GuiButtonCustom extends Button {

    public int disabledColour, enabledColour, hoverColour, customIcon;
    public boolean enableBg;

    public GuiButtonCustom(int i, int j, int k, int l, int i1, String s, int colour1, int colour2, int colour3, boolean bg) {
        super(i, j, k, l, i1, s);
        disabledColour = colour1;
        enabledColour = colour2;
        hoverColour = colour3;
        enableBg = bg;
        customIcon = -1;
    }

    public GuiButtonCustom(int i, int j, int k, int l, int i1, String s, int colour1, int colour2, int colour3, boolean bg, int icon) {
        super(i, j, k, l, i1, s);
        disabledColour = colour1;
        enabledColour = colour2;
        hoverColour = colour3;
        enableBg = bg;
        customIcon = icon;
    }

    public GuiButtonCustom(int i, int j, int k, int l, int i1, String s, boolean bg, int icon) {
        super(i, j, k, l, i1, s);
        disabledColour = 0xffa0a0a0;
        enabledColour = 0xe0e0e0;
        hoverColour = 0xffffa0;
        enableBg = bg;
        customIcon = icon;
    }

    public void render(Minecraft minecraft, int i, int j) {
        if (!visible) {
            return;
        }
        TextRenderer fontrenderer = minecraft.textRenderer;
        boolean flag = i >= x && j >= y
                && i < x + width && j < y + height;
        int k = getYImage(flag);
        if(enableBg) {
            GL11.glBindTexture(3553 /* GL_TEXTURE_2D */,
                    minecraft.textureManager.getTextureId("/gui/gui.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            blit(x, y, 0, 46 + k * 20, width / 2,
                    height);
            blit(x + width / 2, y,
                    200 - width / 2, 46 + k * 20, width / 2, height);
        }
        isMouseOver(minecraft, i, j);
        if (!active) {
            drawTextWithShadowCentred(fontrenderer, text, x + width
                    / 2, y + (height - 8) / 2, disabledColour);
        } else if (flag) {
            drawTextWithShadowCentred(fontrenderer, text, x + width
                    / 2, y + (height - 8) / 2, hoverColour);
        } else {
            drawTextWithShadowCentred(fontrenderer, text, x + width
                    / 2, y + (height - 8) / 2, enabledColour);
        }
        if(customIcon > -1) {
            GL11.glBindTexture(3553 /* GL_TEXTURE_2D */, minecraft.textureManager.getTextureId("/assets/legacytranslations/button_icons.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            blit(x, y, (customIcon % 12) * 20, (customIcon / 12) * 20, 20, 20);
        }
    }
}
