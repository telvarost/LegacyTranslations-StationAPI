package com.github.telvarost.legacytranslations.mixin;

import com.github.telvarost.legacytranslations.GuiButtonCustom;
import com.github.telvarost.legacytranslations.GuiLanguagePacks;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.*;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(MainMenu.class)
public class MainMenuMixin extends ScreenBase {

    public MainMenuMixin() { }

    @Inject(method = "init", at = @At("RETURN"), cancellable = true)
    public void init(CallbackInfo ci) {
        int i = this.height / 4 + 48;
        buttons.add(new GuiButtonCustom(5, width / 2 + 104, i + 48, 20, 20, I18n.translate(""), true, 0));
    }

    @Inject(method = "buttonClicked", at = @At("RETURN"), cancellable = true)
    protected void buttonClicked(Button arg, CallbackInfo ci) {
        if (arg.id == 5)
        {
            this.minecraft.openScreen(new GuiLanguagePacks(this));
        }
    }


}
