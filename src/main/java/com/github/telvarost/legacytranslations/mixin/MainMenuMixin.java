package com.github.telvarost.legacytranslations.mixin;

import com.github.telvarost.legacytranslations.GuiButtonCustom;
import com.github.telvarost.legacytranslations.GuiLanguagePacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ScreenBase;
import net.minecraft.client.gui.screen.menu.*;
import net.minecraft.client.gui.widgets.Button;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(MainMenu.class)
public class MainMenuMixin extends ScreenBase {

    public MainMenuMixin() { }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Happy birthday, ez!")
    )
    public String init_translateHappyBirthdayEz(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        String birthdayTemplate = stringTranslate.translate("game.splash.bday");
        return birthdayTemplate.replace("%s", "ez");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Happy birthday, Notch!")
    )
    public String init_translateHappyBirthdayNotch(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        String birthdayTemplate = stringTranslate.translate("game.splash.bday");
        return birthdayTemplate.replace("%s", "Notch");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Merry X-mas!")
    )
    public String init_translateMerryChristmas(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        return stringTranslate.translate("game.splash.xmas");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Happy new year!")
    )
    public String init_translateHappyNewYear(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        return stringTranslate.translate("game.splash.newyear");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "menu.mods")
    )
    public String init_translateTexturePacks(String def) {
        return "menu.texturepacks";
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(stringValue = "Copyright Mojang AB. Do not distribute.")
    )
    public String init_translateBackToGame(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        return stringTranslate.translate("menu.copyright");
    }

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
