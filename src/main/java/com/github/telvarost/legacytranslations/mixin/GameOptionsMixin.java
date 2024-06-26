package com.github.telvarost.legacytranslations.mixin;

import com.github.telvarost.legacytranslations.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintWriter;

@Environment(value= EnvType.CLIENT)
@Mixin(GameOptions.class)
public class GameOptionsMixin {

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    protected void buttonClicked(CallbackInfo ci) {
        ModHelper.reloadKeys();
    }

    @Redirect(
            method = "save",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/io/PrintWriter;println(Ljava/lang/String;)V",
                    ordinal = 14
            )
    )
    public void saveOptions(PrintWriter instance, String x) {
        instance.println("lastServer:" + x);
        instance.println("langFile:" + ModHelper.ModHelperFields.langFile);
    }
}
