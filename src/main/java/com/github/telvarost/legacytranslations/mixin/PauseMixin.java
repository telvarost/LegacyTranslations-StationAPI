package com.github.telvarost.legacytranslations.mixin;

import com.github.telvarost.legacytranslations.GuiButtonCustom;
import com.github.telvarost.legacytranslations.GuiLanguagePacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.ingame.Pause;
import net.minecraft.client.gui.screen.menu.TexturePacks;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Pause.class)
public class PauseMixin extends ScreenBase {
    public PauseMixin() { }

    @Inject(method = "init", at = @At("RETURN"), cancellable = true)
    public void init(CallbackInfo ci) {
        byte byte0 = -16;
        buttons.add(new Button(7, width / 2 - 100, height / 4 + 72 + byte0, 178, 20, I18n.translate(("menu.mods"))));
        buttons.add(new GuiButtonCustom(8, width / 2 + 80, height / 4 + 72 + byte0, 20, 20, "", true, 0));
    }

    @Inject(method = "buttonClicked", at = @At("RETURN"), cancellable = true)
    protected void buttonClicked(Button arg, CallbackInfo ci) {

        if (arg.id == 7) {
            this.minecraft.openScreen(new TexturePacks(this));
        }
        if (arg.id == 8) {
            this.minecraft.openScreen(new GuiLanguagePacks(this));
        }
    }
}
