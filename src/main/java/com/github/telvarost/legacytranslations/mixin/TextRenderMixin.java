package com.github.telvarost.legacytranslations.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_214;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.CharacterUtils;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Environment(value= EnvType.CLIENT)
@Mixin(TextRenderer.class)
public abstract class TextRenderMixin {
    @Shadow public abstract void drawText(String string, int i, int j, int k);

    @Shadow public abstract void method_1904(String string, int i, int j, int k, int l);

    @Shadow public abstract int getTextWidth(String string);

    @Shadow public abstract int method_1902(String string, int i);

    @Shadow public abstract void drawText(String string, int i, int j, int k, boolean bl);

    @Shadow private int[] field_2462 = new int[256]; // charWidth
    @Shadow public int field_2461 = 0; // fontTextureName
    @Shadow private int field_2463; // fontDisplayLists
    @Shadow private IntBuffer field_2464 = class_214.method_745(1024); // buffer
    
    @Unique private int res;
    @Unique public int fontTextureNames[];
    @Unique public String fontPngName;
    @Unique public int textureWidth;
    @Unique public int textureHeight;
    @Unique public int imageRGB[];
    @Unique public BufferedImage fontPng;
    @Unique private static final String colorChars = "0123456789abcdefgh";

    public TextRenderMixin(GameOptions arg, String string, TextureManager arg2) { }

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void TextRenderMixin(GameOptions options, String texString, TextureManager renderengine, CallbackInfo ci) {
        field_2462 = new int[CharacterUtils.validCharacters.length()];
        field_2461 = 0;
        field_2464 = class_214.method_745(1024 /*GL_FRONT_LEFT*/);
        fontTextureNames = new int[(CharacterUtils.validCharacters.length() / 256) + 1];
        fontPngName = "";
        updateFontImage(0, renderengine, texString);
        for(int k = 0; k < CharacterUtils.validCharacters.length(); k++)
        {
            updateFontImage(k, renderengine, texString);
            int wrappedk = k % 256;
            int l = wrappedk % 16;
            int k1 = wrappedk / 16;
            int j2 = (textureWidth/16)-1;
            do
            {
                if(j2 < 0)
                {
                    break;
                }
                int i3 = l * (textureWidth/16) + j2;
                boolean flag = true;
                for(int l3 = 0; l3 < (textureWidth/16) && flag; l3++)
                {
                    int i4 = (k1 * (textureWidth/16) + l3) * textureWidth;
                    int k4 = imageRGB[i3 + i4] & 0xff;
                    if(k4 > 0)
                    {
                        flag = false;
                    }
                }

                if(!flag)
                {
                    break;
                }
                j2--;
            } while(true);
            if(k == 32)
            {
                j2 = (textureWidth/64) + (textureWidth/128) - 1;
            }
            field_2462[k] = (j2 + 2 + ((res/8) - 1)) / (res/8);
        }

        field_2463 = class_214.method_741(288);
        Tessellator tessellator = Tessellator.INSTANCE;
        for(int i1 = 0; i1 < CharacterUtils.validCharacters.length(); i1++)
        {
            updateFontImage(i1, renderengine, texString);
            field_2461 = getTextureId(i1, renderengine);
            GL11.glNewList(field_2463 + i1 + (i1 >= 256 ? 512 : 0), 4864 /*GL_COMPILE*/);
            GL11.glBindTexture(3553, field_2461);
            int renderWidth = 128;
            tessellator.start();
            int l1 = (i1 % 16) * (renderWidth/16);
            int k2 = (i1 / 16) * (renderWidth/16);
            float f = (float)(renderWidth/16);
            float f1 = 0.0F;
            float f2 = 0.0F;
            tessellator.vertex(0.0D, 0.0F + f, 0.0D, (float)l1 / (float)(renderWidth) + f1, ((float)k2 + f) / (float)(renderWidth) + f2);
            tessellator.vertex(0.0F + f, 0.0F + f, 0.0D, ((float)l1 + f) / (float)(renderWidth) + f1, ((float)k2 + f) / (float)(renderWidth) + f2);
            tessellator.vertex(0.0F + f, 0.0D, 0.0D, ((float)l1 + f) / (float)(renderWidth) + f1, (float)k2 / (float)(renderWidth) + f2);
            tessellator.vertex(0.0D, 0.0D, 0.0D, (float)l1 / (float)(renderWidth) + f1, (float)k2 / (float)(renderWidth) + f2);
            tessellator.draw();
            GL11.glTranslatef(field_2462[i1], 0.0F, 0.0F);
            GL11.glEndList();
//        	System.out.println(ChatAllowedCharacters.allowedCharacters.charAt(i1) + " ? " + ChatAllowedCharacters.isAllowedCharacter(ChatAllowedCharacters.allowedCharacters.charAt(i1)));
//            System.out.println("Width of ID " + i1 + " (" + ChatAllowedCharacters.allowedCharacters.charAt(i1) + "): " + field_2462[i1] + " texID " + getTextureId(i1, renderengine) + " PNGID " + fontPng);
        }

        for(int j1 = 0; j1 < colorChars.length() * 2; j1++)
        {
            int k1 = -1;
            if(j1 >= 32)
                k1 = j1 - ((j1 / 16) * 16);
            int customPosition = k1 == -1 ? -1 : k1 % (((colorChars.length() - 16) / 2) + 1);
            if(customPosition == 0) {
                //Rainbow is g, or the first (0) custom character.
                //We skip rendering here because rainbow isn't compatible with this code block, we render it elsewhere.
                continue;
            }
            int i2 = (j1 >> 3 & 1) * 85;
            int l2 = (j1 >> 2 & 1) * 170 + i2;
            int j3 = (j1 >> 1 & 1) * 170 + i2;
            int k3 = (j1 >> 0 & 1) * 170 + i2;
            if(j1 == 6)
            {
                l2 += 85;
            }
            if(customPosition == 1) {
                l2 = 0xFF;
                j3 = 0xFF;
                k3 = 0xA0;
            }
            boolean flag1 = j1 < 32 ? j1 >= 16 : j1 - 32 > (colorChars.length() - 16) / 2;
            if(options.anaglyph3d)
            {
                int j4 = (l2 * 30 + j3 * 59 + k3 * 11) / 100;
                int l4 = (l2 * 30 + j3 * 70) / 100;
                int i5 = (l2 * 30 + k3 * 70) / 100;
                l2 = j4;
                j3 = l4;
                k3 = i5;
            }
            if(flag1)
            {
                l2 /= 4;
                j3 /= 4;
                k3 /= 4;
            }
            GL11.glNewList(field_2463 + 256 + j1, 4864 /*GL_COMPILE*/);
            GL11.glColor3f((float)(l2 / 255F), (float)j3 / 255F, (float)k3 / 255F);
//            Below commented line used to ensure the colour was outputted correctly
//            System.out.println("Color values for value " + j1 + (flag1 ? "'s shadow" : "") + " are... Red: " + l2 + " Green: " + j3 + " Blue: " + k3);
            GL11.glEndList();
        }
    }

    @Unique
    public void updateFontImage(int i, TextureManager renderengine, String texString) {
        String prevPngName = fontPngName;
        if(i >= 256) {
            fontPngName = "font_" + String.format("%02X", (byte)(i/256));
        } else {
            fontPngName = "default";
        }
        if(prevPngName.equals(fontPngName)) {
            return;
        }
        try
        {
            BufferedImage image = ImageIO.read((net.minecraft.client.Minecraft.class).getResource("/assets/legacytranslations/font/" + fontPngName + ".png"));
            fontPng = image;
        }
        catch(IOException ioexception)
        {
            System.err.println("Image \"" + fontPngName + ".png\" was null!");
            throw new RuntimeException(ioexception);
        }
        textureWidth = fontPng.getWidth();
        textureHeight = fontPng.getHeight();
        res = textureWidth/16;
        for(int j = 0; j < textureWidth * textureHeight; j++) {
            int l = j % textureWidth;
            int k1 = j / textureHeight;
            Color c = new Color(fontPng.getRGB(l, k1), true);
            if(c.getAlpha() <= 2) {
                fontPng.setRGB(l, k1, 0);
            }
        }
        imageRGB = new int[textureWidth * textureHeight];
        imageRGB = fontPng.getRGB(0, 0, textureWidth, textureHeight, imageRGB, 0, textureWidth);
    }

    @Unique
    public int getTextureId(int i, TextureManager renderengine) {
        int j = i/256;
        if(fontTextureNames[j] == 0) {
            fontTextureNames[j] = renderengine.method_1088(fontPng);
        }
        return fontTextureNames[j];
    }



    @Inject(
            method = "drawText(Ljava/lang/String;IIIZ)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void legacyTranslations_drawText(String s, int i, int j, int k, boolean flag, CallbackInfo ci) {
        if(s == null)
        {
            return;
        }
        if(flag)
        {
            int l = k & 0xff000000;
            k = (k & 0xfcfcfc) >> 2;
            k += l;
        }
        float f = (float)(k >> 16 & 0xff) / 255F;
        float f1 = (float)(k >> 8 & 0xff) / 255F;
        float f2 = (float)(k & 0xff) / 255F;
        float f3 = (float)(k >> 24 & 0xff) / 255F;
        if(f3 == 0.0F)
        {
            f3 = 1.0F;
        }
        GL11.glColor4f(f, f1, f2, f3);
        field_2464.clear();
        GL11.glPushMatrix();
        GL11.glTranslatef(i, j, 0.0F);
        for(int i1 = 0; i1 < s.length(); i1++)
        {
            for(; s.length() > i1 + 1 && s.charAt(i1) == '\247'; i1 += 2)
            {
                int j1 = colorChars.indexOf(s.toLowerCase().charAt(i1 + 1));
                int k1 = j1 / 16;
                char ch = s.toLowerCase().charAt(i1 + 1);
                if(ch == 'g') {
                    int k4 = Color.HSBtoRGB(((System.currentTimeMillis() % 10000L) / 10000F), 1.0F, 1.0F) & 0xffffff;
                    if(flag)
                    {
                        int l = k4 & 0xff000000;
                        k4 = (k4 & 0xfcfcfc) >> 2;
                        k4 += l;
                    }
                    f = (float)(k4 >> 16 & 0xff) / 255F;
                    f1 = (float)(k4 >> 8 & 0xff) / 255F;
                    f2 = (float)(k4 & 0xff) / 255F;
                }
                if(j1 < 0 || j1 > colorChars.length() - 1)
                {
                    j1 = 15;
                }
                field_2464.put(field_2463 + 256 + j1 + (k1 * 16) + (flag ? j1 > 15 ? ((colorChars.length() - 16) / 2) + 1 : 16 : 0));
                if(field_2464.remaining() == 0)
                {
                    field_2464.flip();
                    GL11.glCallLists(field_2464);
                    field_2464.clear();
                }
                if(ch == 'g')
                    GL11.glColor4f(f, f1, f2, f3);
            }

            if(i1 < s.length())
            {
                int k1 = CharacterUtils.validCharacters.indexOf(s.charAt(i1));
                if(k1 >= 0)
                {
                    //k1 += 32;
                    field_2464.put(field_2463 + k1 + (k1 >= 256 ? 512 : 0));
                }
            }
            if(field_2464.remaining() == 0)
            {
                field_2464.flip();
                GL11.glCallLists(field_2464);
                field_2464.clear();
            }
        }

        field_2464.flip();
        GL11.glCallLists(field_2464);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glPopMatrix();
        ci.cancel();
    }


    @Inject(
            method = "getTextWidth",
            at = @At("HEAD"),
            cancellable = true
    )
    public void legacyTranslations_getTextWidth(String s, CallbackInfoReturnable<Integer> cir) {
        if(s == null)
        {
            cir.setReturnValue(0);
            return;
        }
        int i = 0;
        for(int j = 0; j < s.length(); j++)
        {
            if(s.charAt(j) == '\247')
            {
                j++;
                continue;
            }
            int k = CharacterUtils.validCharacters.indexOf(s.charAt(j));
            if(k >= 0)
            {
                i += field_2462[k];
            }
        }

        cir.setReturnValue(i);
        return;
    }

    @Inject(
            method = "method_1904",
            at = @At("HEAD"),
            cancellable = true
    )
    public void legacyTranslations_method_1904(String s, int i, int j, int k, int l, CallbackInfo ci) {
        String as[] = s.split("\n");
        if(as.length > 1)
        {
            for(int i1 = 0; i1 < as.length; i1++)
            {
                method_1904(as[i1], i, j, k, l);
                j += method_1902(as[i1], k);
            }

            return;
        }
        String as1[] = s.split(" ");
        int j1 = 0;
        do
        {
            if(j1 >= as1.length)
            {
                break;
            }
            String s1;
            for(s1 = (new StringBuilder()).append(as1[j1++]).append(" ").toString(); j1 < as1.length && getTextWidth((new StringBuilder()).append(s1).append(as1[j1]).toString()) < k; s1 = (new StringBuilder()).append(s1).append(as1[j1++]).append(" ").toString()) { }
            int k1;
            for(; getTextWidth(s1) > k; s1 = s1.substring(k1))
            {
                for(k1 = 0; getTextWidth(s1.substring(0, k1 + 1)) <= k; k1++) { }
                if(s1.substring(0, k1).trim().length() > 0)
                {
                    drawText(s1.substring(0, k1), i, j, l);
                    j += 8;
                }
            }

            if(s1.trim().length() > 0)
            {
                drawText(s1, i, j, l);
                j += 8;
            }
        } while(true);
        ci.cancel();
    }


    @Inject(
            method = "method_1902",
            at = @At("HEAD"),
            cancellable = true
    )
    public void legacyTranslations_method_1902(String s, int i, CallbackInfoReturnable<Integer> cir) {
        String as[] = s.split("\n");
        if(as.length > 1)
        {
            int j = 0;
            for(int k = 0; k < as.length; k++)
            {
                j += method_1902(as[k], i);
            }

            cir.setReturnValue(j);
            return;
        }
        String as1[] = s.split(" ");
        int l = 0;
        int i1 = 0;
        do
        {
            if(l >= as1.length)
            {
                break;
            }
            String s1;
            for(s1 = (new StringBuilder()).append(as1[l++]).append(" ").toString(); l < as1.length && getTextWidth((new StringBuilder()).append(s1).append(as1[l]).toString()) < i; s1 = (new StringBuilder()).append(s1).append(as1[l++]).append(" ").toString()) { }
            int j1;
            for(; getTextWidth(s1) > i; s1 = s1.substring(j1))
            {
                for(j1 = 0; getTextWidth(s1.substring(0, j1 + 1)) <= i; j1++) { }
                if(s1.substring(0, j1).trim().length() > 0)
                {
                    i1 += 8;
                }
            }

            if(s1.trim().length() > 0)
            {
                i1 += 8;
            }
        } while(true);
        if(i1 < 8)
        {
            i1 += 8;
        }
        cir.setReturnValue(i1);
        return;
    }

    @Unique
    public int splitStringWidth(String par1Str, int par2) {
        return this.listFormattedStringToWidth(par1Str, par2).size();
    }

    @Unique
    public void drawSplitString(String par1Str, int par2, int par3, int par4,
                                int par5) {
        par1Str = this.trimStringNewline(par1Str);
        this.renderSplitString(par1Str, par2, par3, par4, false);
    }

    @Unique
    private String trimStringNewline(String par1Str) {
        while (par1Str != null && par1Str.endsWith("\n")) {
            par1Str = par1Str.substring(0, par1Str.length() - 1);
        }

        return par1Str;
    }

    @Unique
    private void renderSplitString(String par1Str, int par2, int par3,
                                   int par4, boolean par5) {
        List var6 = this.listFormattedStringToWidth(par1Str, par4);

        for (Iterator var7 = var6.iterator(); var7.hasNext(); par3 += 8) {
            String var8 = (String) var7.next();
            this.drawText(var8, par2, par3, 0, par5);
        }
    }

    @Unique
    public List listFormattedStringToWidth(String par1Str, int par2) {
        return Arrays.asList(this.wrapFormattedStringToWidth(par1Str, par2)
                .split("\n"));
    }

    @Unique
    String wrapFormattedStringToWidth(String par1Str, int par2) {
        int var3 = this.sizeStringToWidth(par1Str, par2);

        if (par1Str.length() <= var3) {
            return par1Str;
        } else {
            String var4 = par1Str.substring(0, var3);
            char var5 = par1Str.charAt(var3);
            boolean var6 = var5 == 32 || var5 == 10;
            String var7 = getFormatFromString(var4)
                    + par1Str.substring(var3 + (var6 ? 1 : 0));
            return var4 + "\n" + this.wrapFormattedStringToWidth(var7, par2);

        }
    }

    @Unique
    private static String getFormatFromString(String par0Str) {
        String var1 = "";
        int var2 = -1;
        int var3 = par0Str.length();

        while ((var2 = par0Str.indexOf(167, var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                char var4 = par0Str.charAt(var2 + 1);

            }
        }

        return var1;
    }

    @Unique
    private int sizeStringToWidth(String par1Str, int par2) {
        int var3 = par1Str.length();
        int var4 = 0;
        int var5 = 0;
        int var6 = -1;

        for (boolean var7 = false; var5 < var3; ++var5) {
            char var8 = par1Str.charAt(var5);

            switch (var8) {
                case 10:
                    --var5;
                    break;

                case 167:
                    if (var5 < var3 - 1) {
                        ++var5;
                        char var9 = par1Str.charAt(var5);

                        if (var9 != 108 && var9 != 76) {
                            if (var9 == 114 || var9 == 82) {
                                var7 = false;
                            }
                        } else {
                            var7 = true;
                        }
                    }

                    break;

                case 32:
                    var6 = var5;

                default:
                    var4 += this.getCharWidth(var8);

                    if (var7) {
                        ++var4;
                    }
            }

            if (var8 == 10) {
                ++var5;
                var6 = var5;
                break;
            }

            if (var4 > par2) {
                break;
            }
        }

        return var5 != var3 && var6 != -1 && var6 < var5 ? var6 : var5;
    }

    @Unique
    public int getCharWidth(char par1) {
        if (par1 == 167) {
            return -1;
        } else if (par1 == 32) {
            return 4;
        } else {
            int var2 = CharacterUtils.validCharacters.indexOf(par1);

            if (par1 > 0 && var2 != -1) {
                return this.field_2462[var2];
            } else {
                return 0;
            }
        }
    }
}
