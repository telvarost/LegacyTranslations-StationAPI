package com.github.telvarost.legacytranslations.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ButtonWidget.class)
public abstract class ButtonWidgetMixin extends DrawContext {

    @Shadow public int x;

    @Shadow public int y;

    @Shadow protected int width;

    @Shadow protected int height;

    @Unique private int _renderTick = 0;
    @Unique private int _charIndex = 0;
    @Unique private int _pauseDuration = 0;
    @Unique private boolean scrollDirection = true;
    @Unique private int _mouseX = 0;
    @Unique private int _mouseY = 0;
    @Unique Minecraft _minecraft;

    @Inject(
            method = "render",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render(Minecraft minecraft, int mouseX, int mouseY, CallbackInfo ci) {
        _minecraft = minecraft;
        _mouseX = mouseX;
        _mouseY = mouseY;
    }

    @Redirect(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/widget/ButtonWidget;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;III)V"
            )
    )
    public void render(ButtonWidget instance, TextRenderer textRenderer, String s, int i, int j, int k) {

        if (textRenderer.getWidth(s) > this.width) {
            String drawText;
            int charWidth = textRenderer.getWidth("W");
            int numChars = this.width / charWidth;

            if (legacyTranslations_isMouseOver(_mouseX, _mouseY)) {
                drawText = s.substring(_charIndex, numChars + _charIndex);
                int currentTicksPlayed = ((MinecraftAccessor)_minecraft).getTicksPlayed();

                if (currentTicksPlayed > (4 + _renderTick)) {
                    _renderTick = currentTicksPlayed;

                    if (_charIndex <= 0) {
                        if (1 < _pauseDuration) {
                            _pauseDuration = 0;
                            scrollDirection = true;
                        } else {
                            _pauseDuration++;
                        }
                    }

                    if (_charIndex >= (s.length() - numChars)) {
                        if (1 < _pauseDuration) {
                            _pauseDuration = 0;
                            scrollDirection = false;
                        } else {
                            _pauseDuration++;
                        }
                    }

                    if (0 == _pauseDuration) {
                        if (scrollDirection) {
                            _charIndex++;
                        } else {
                            _charIndex--;
                        }
                    }
                }
            } else {
                drawText = s.substring(0, numChars - 2) + "...";
                scrollDirection = true;
                _pauseDuration = 0;
                _charIndex = 0;
            }

            super.drawCenteredTextWithShadow(textRenderer, drawText, i, j, k);
        } else {
            super.drawCenteredTextWithShadow(textRenderer, s, i, j, k);
        }
    }

    @Unique
    private boolean legacyTranslations_isMouseOver(int mouseX, int mouseY) {
        return mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }
}
