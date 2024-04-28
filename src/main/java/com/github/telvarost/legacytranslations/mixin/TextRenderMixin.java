//package com.github.telvarost.legacytranslations.mixin;
//
//import net.minecraft.class_214;
//import net.minecraft.client.options.GameOptions;
//import net.minecraft.client.render.Tessellator;
//import net.minecraft.client.render.TextRenderer;
//import net.minecraft.client.texture.TextureManager;
//import net.minecraft.util.CharacterUtils;
//import org.lwjgl.opengl.GL11;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.Unique;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//import javax.imageio.ImageIO;
//import java.awt.*;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.nio.IntBuffer;
//
//@Mixin(TextRenderer.class)
//public class TextRenderMixin {
//    @Unique private int charWidth[];
//    @Unique private int res;
//    @Unique public int fontTextureName;
//    @Unique public int fontTextureNames[];
//    @Unique public String fontPngName;
//    @Unique public int textureWidth;
//    @Unique public int textureHeight;
//    @Unique public int imageRGB[];
//    @Unique public BufferedImage fontPng;
//    @Unique private int fontDisplayLists;
//    @Unique private IntBuffer buffer;
//    @Unique private static final String colorChars = "0123456789abcdefgh";
//
//    public TextRenderMixin(GameOptions arg, String string, TextureManager arg2) { }
//
//    @Inject(method = "<init>", at = @At("RETURN"), cancellable = true)
//    private void constructor(GameOptions options, String texString, TextureManager renderengine, CallbackInfo ci) {
//        charWidth = new int[CharacterUtils.validCharacters.length()];
//        fontTextureName = 0;
//        buffer = class_214.method_745(1024 /*GL_FRONT_LEFT*/);
//        fontTextureNames = new int[(CharacterUtils.validCharacters.length() / 256) + 1];
//        fontPngName = "";
//        updateFontImage(0, renderengine, texString);
//        for(int k = 0; k < CharacterUtils.validCharacters.length(); k++)
//        {
//            updateFontImage(k, renderengine, texString);
//            int wrappedk = k % 256;
//            int l = wrappedk % 16;
//            int k1 = wrappedk / 16;
//            int j2 = (textureWidth/16)-1;
//            do
//            {
//                if(j2 < 0)
//                {
//                    break;
//                }
//                int i3 = l * (textureWidth/16) + j2;
//                boolean flag = true;
//                for(int l3 = 0; l3 < (textureWidth/16) && flag; l3++)
//                {
//                    int i4 = (k1 * (textureWidth/16) + l3) * textureWidth;
//                    int k4 = imageRGB[i3 + i4] & 0xff;
//                    if(k4 > 0)
//                    {
//                        flag = false;
//                    }
//                }
//
//                if(!flag)
//                {
//                    break;
//                }
//                j2--;
//            } while(true);
//            if(k == 32)
//            {
//                j2 = (textureWidth/64) + (textureWidth/128) - 1;
//            }
//            charWidth[k] = (j2 + 2 + ((res/8) - 1)) / (res/8);
//        }
//
//        fontDisplayLists = class_214.method_741(288);
//        Tessellator tessellator = Tessellator.INSTANCE;
//        for(int i1 = 0; i1 < CharacterUtils.validCharacters.length(); i1++)
//        {
//            updateFontImage(i1, renderengine, texString);
//            fontTextureName = getTextureId(i1, renderengine);
//            GL11.glNewList(fontDisplayLists + i1 + (i1 >= 256 ? 512 : 0), 4864 /*GL_COMPILE*/);
//            GL11.glBindTexture(3553, fontTextureName);
//            int renderWidth = 128;
//            tessellator.start();
//            int l1 = (i1 % 16) * (renderWidth/16);
//            int k2 = (i1 / 16) * (renderWidth/16);
//            float f = (float)(renderWidth/16);
//            float f1 = 0.0F;
//            float f2 = 0.0F;
//            tessellator.vertex(0.0D, 0.0F + f, 0.0D, (float)l1 / (float)(renderWidth) + f1, ((float)k2 + f) / (float)(renderWidth) + f2);
//            tessellator.vertex(0.0F + f, 0.0F + f, 0.0D, ((float)l1 + f) / (float)(renderWidth) + f1, ((float)k2 + f) / (float)(renderWidth) + f2);
//            tessellator.vertex(0.0F + f, 0.0D, 0.0D, ((float)l1 + f) / (float)(renderWidth) + f1, (float)k2 / (float)(renderWidth) + f2);
//            tessellator.vertex(0.0D, 0.0D, 0.0D, (float)l1 / (float)(renderWidth) + f1, (float)k2 / (float)(renderWidth) + f2);
//            tessellator.draw();
//            GL11.glTranslatef(charWidth[i1], 0.0F, 0.0F);
//            GL11.glEndList();
////        	System.out.println(ChatAllowedCharacters.allowedCharacters.charAt(i1) + " ? " + ChatAllowedCharacters.isAllowedCharacter(ChatAllowedCharacters.allowedCharacters.charAt(i1)));
////            System.out.println("Width of ID " + i1 + " (" + ChatAllowedCharacters.allowedCharacters.charAt(i1) + "): " + charWidth[i1] + " texID " + getTextureId(i1, renderengine) + " PNGID " + fontPng);
//        }
//
//        for(int j1 = 0; j1 < colorChars.length() * 2; j1++)
//        {
//            int k1 = -1;
//            if(j1 >= 32)
//                k1 = j1 - ((j1 / 16) * 16);
//            int customPosition = k1 == -1 ? -1 : k1 % (((colorChars.length() - 16) / 2) + 1);
//            if(customPosition == 0) {
//                //Rainbow is g, or the first (0) custom character.
//                //We skip rendering here because rainbow isn't compatible with this code block, we render it elsewhere.
//                continue;
//            }
//            int i2 = (j1 >> 3 & 1) * 85;
//            int l2 = (j1 >> 2 & 1) * 170 + i2;
//            int j3 = (j1 >> 1 & 1) * 170 + i2;
//            int k3 = (j1 >> 0 & 1) * 170 + i2;
//            if(j1 == 6)
//            {
//                l2 += 85;
//            }
//            if(customPosition == 1) {
//                l2 = 0xFF;
//                j3 = 0xFF;
//                k3 = 0xA0;
//            }
//            boolean flag1 = j1 < 32 ? j1 >= 16 : j1 - 32 > (colorChars.length() - 16) / 2;
//            if(options.anaglyph3d)
//            {
//                int j4 = (l2 * 30 + j3 * 59 + k3 * 11) / 100;
//                int l4 = (l2 * 30 + j3 * 70) / 100;
//                int i5 = (l2 * 30 + k3 * 70) / 100;
//                l2 = j4;
//                j3 = l4;
//                k3 = i5;
//            }
//            if(flag1)
//            {
//                l2 /= 4;
//                j3 /= 4;
//                k3 /= 4;
//            }
//            GL11.glNewList(fontDisplayLists + 256 + j1, 4864 /*GL_COMPILE*/);
//            GL11.glColor3f((float)(l2 / 255F), (float)j3 / 255F, (float)k3 / 255F);
////            Below commented line used to ensure the colour was outputted correctly
////            System.out.println("Color values for value " + j1 + (flag1 ? "'s shadow" : "") + " are... Red: " + l2 + " Green: " + j3 + " Blue: " + k3);
//            GL11.glEndList();
//        }
//    }
//
//    @Unique
//    public void updateFontImage(int i, TextureManager renderengine, String texString) {
//        String prevPngName = fontPngName;
//        if(i >= 256) {
//            fontPngName = "font_" + String.format("%02X", (byte)(i/256));
//        } else {
//            fontPngName = "default";
//        }
//        if(prevPngName.equals(fontPngName)) {
//            return;
//        }
//        try
//        {
//            BufferedImage image = ImageIO.read((net.minecraft.client.Minecraft.class).getResource("/font/" + fontPngName + ".png"));
//            fontPng = image;
//        }
//        catch(IOException ioexception)
//        {
//            System.err.println("Image \"" + fontPngName + ".png\" was null!");
//            throw new RuntimeException(ioexception);
//        }
//        textureWidth = fontPng.getWidth();
//        textureHeight = fontPng.getHeight();
//        res = textureWidth/16;
//        for(int j = 0; j < textureWidth * textureHeight; j++) {
//            int l = j % textureWidth;
//            int k1 = j / textureHeight;
//            Color c = new Color(fontPng.getRGB(l, k1), true);
//            if(c.getAlpha() <= 2) {
//                fontPng.setRGB(l, k1, 0);
//            }
//        }
//        imageRGB = new int[textureWidth * textureHeight];
//        imageRGB = fontPng.getRGB(0, 0, textureWidth, textureHeight, imageRGB, 0, textureWidth);
//    }
//
//    @Unique
//    public int getTextureId(int i, TextureManager renderengine) {
//        int j = i/256;
//        if(fontTextureNames[j] == 0) {
//            fontTextureNames[j] = renderengine.method_1088(fontPng);
//        }
//        return fontTextureNames[j];
//    }
//}
