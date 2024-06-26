package com.github.telvarost.legacytranslations.mixin;

import com.github.telvarost.legacytranslations.GuiButtonCustom;
import com.github.telvarost.legacytranslations.GuiLanguagePacks;
import com.github.telvarost.legacytranslations.ModHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameMenuScreen.class)
public class PauseMixin extends Screen {
    public PauseMixin() { }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Save and quit to title")
    )
    public String init_translateSaveAndQuit(String def) {
        return I18n.getTranslation("inGameMenu.saveAndQuitToTitle");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Disconnect")
    )
    public String init_translateDisconnect(String def) {
        return I18n.getTranslation("inGameMenu.disconnect");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Back to game")
    )
    public String init_translateBackToGame(String def) {
        return I18n.getTranslation("inGameMenu.backToGame");
    }

    @ModifyConstant(
            method = "init",
            constant = @Constant(stringValue = "Options...")
    )
    public String init_translateOptions(String def) {
        return I18n.getTranslation("menu.options");
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    public void init_head(CallbackInfo ci) {
        ModHelper.reloadKeys();
    }

    @Inject(method = "init", at = @At("RETURN"), cancellable = true)
    public void init_return(CallbackInfo ci) {
        byte byte0 = -16;
        if (!FabricLoader.getInstance().isModLoaded("modmenu")) {
            buttons.add(new ButtonWidget(7, width / 2 - 100, height / 4 + 72 + byte0, 200, 20, I18n.getTranslation(("menu.texturepacks"))));
        }
        buttons.add(new GuiButtonCustom(8, width / 2 + 104, height / 4 + 72 + byte0, 20, 20, "", true, 0));
    }

    @Inject(method = "buttonClicked", at = @At("RETURN"), cancellable = true)
    protected void buttonClicked(ButtonWidget arg, CallbackInfo ci) {
        if (arg.id == 7) {
            if (!FabricLoader.getInstance().isModLoaded("modmenu")) {
                this.minecraft.setScreen(new PackScreen(this));
            }
        }

        if (arg.id == 8) {
            this.minecraft.setScreen(new GuiLanguagePacks(this));
        }
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(stringValue = "Saving level..")
    )
    public String render_translateSavingLevel(String def) {
        return I18n.getTranslation("inGameMenu.savingLevel");
    }

    @ModifyConstant(
            method = "render",
            constant = @Constant(stringValue = "Game menu")
    )
    public String render_translateGameMenu(String def) {
        return I18n.getTranslation("inGameMenu.gameMenu");
    }
}
