package com.github.telvarost.legacytranslations.mixin;

import java.io.*;
import java.util.Properties;

import com.github.telvarost.legacytranslations.ModHelper;
import com.github.telvarost.legacytranslations.TranslationFixHook;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {
    @Shadow private static TranslationStorage instance;

    @Inject(
            method = "<init>",
            at = @At("RETURN")
    )
    private void TranslationStorageMixin(CallbackInfo ci) {
        ModHelper.reloadKeys();
    }
}