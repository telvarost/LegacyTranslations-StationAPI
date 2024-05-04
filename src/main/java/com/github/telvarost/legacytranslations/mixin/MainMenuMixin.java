package com.github.telvarost.legacytranslations.mixin;

import com.github.telvarost.legacytranslations.GuiButtonCustom;
import com.github.telvarost.legacytranslations.GuiLanguagePacks;
import com.github.telvarost.legacytranslations.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.resource.language.TranslationStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(TitleScreen.class)
public class MainMenuMixin extends Screen {

    public MainMenuMixin() { }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Happy birthday, ez!")
    )
    public String init_translateHappyBirthdayEz(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        String birthdayTemplate = stringTranslate.get("game.splash.bday");
        return birthdayTemplate.replace("%s", "ez");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Happy birthday, Notch!")
    )
    public String init_translateHappyBirthdayNotch(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        String birthdayTemplate = stringTranslate.get("game.splash.bday");
        return birthdayTemplate.replace("%s", "Notch");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Merry X-mas!")
    )
    public String init_translateMerryChristmas(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        return stringTranslate.get("game.splash.xmas");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Happy new year!")
    )
    public String init_translateHappyNewYear(String def) {
        TranslationStorage stringTranslate = TranslationStorage.getInstance();
        return stringTranslate.get("game.splash.newyear");
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
        return stringTranslate.get("menu.copyright");
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    public void init_head(CallbackInfo ci) {
        ModHelper.reloadKeys();
    }

    @Inject(method = "init", at = @At("RETURN"), cancellable = true)
    public void init_return(CallbackInfo ci) {
        int i = this.height / 4 + 48;
        buttons.add(new GuiButtonCustom(5, width / 2 + 104, i + 48, 20, 20, I18n.getTranslation(""), true, 0));
    }

    @Inject(method = "buttonClicked", at = @At("RETURN"), cancellable = true)
    protected void buttonClicked(ButtonWidget arg, CallbackInfo ci) {
        if (arg.id == 5)
        {
            this.minecraft.setScreen(new GuiLanguagePacks(this));
        }
    }


}
