package com.github.telvarost.legacytranslations.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_303;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.texture.TextureManager;
import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.image.BufferedImage;

@Environment(value= EnvType.CLIENT)
@Mixin(TextureManager.class)
public class TextureManagerMixin {

    @Shadow public static boolean field_1245;

    @Inject(
            method = "<init>",
            at = @At("RETURN"),
            cancellable = false
    )
    public void TextureManagerMixin(class_303 gameOptions, GameOptions par2, CallbackInfo ci) {
        field_1245 = true;
    }

    @Inject(
            method = "method_1089",
            at = @At("RETURN"),
            cancellable = true
    )
    public void bindImageToId(BufferedImage i, int par2, CallbackInfo ci) {
        GL30.glGenerateMipmap(3553);
    }

    @Inject(
            method = "method_1095",
            at = @At("RETURN"),
            cancellable = true
    )
    public void bindImageToId2(int[] i, int j, int k, int par4, CallbackInfo ci) {
        GL30.glGenerateMipmap(3553);
    }

    @Inject(
            method = "method_1084",
            at = @At("RETURN"),
            cancellable = true
    )
    public void method_1084(CallbackInfo ci) {
        GL30.glGenerateMipmap(3553);
    }
}
