package com.github.telvarost.legacytranslations.mixin;

import net.minecraft.class_214;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.client.texture.TextureManager;
import org.spongepowered.asm.mixin.Mixin;

import java.nio.IntBuffer;

@Mixin(TextRenderer.class)
public class TextRenderMixin {
    public TextRenderMixin(GameOptions arg, String string, TextureManager arg2) { }

}
