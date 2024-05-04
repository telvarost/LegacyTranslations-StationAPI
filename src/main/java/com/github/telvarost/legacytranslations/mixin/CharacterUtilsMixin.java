package com.github.telvarost.legacytranslations.mixin;

import net.minecraft.util.CharacterUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(CharacterUtils.class)
public class CharacterUtilsMixin {

    @ModifyConstant(
            method = "loadValidCharacters",
            constant = @Constant(stringValue = "/font.txt")
    )
    private static String getValidCharacters(String def) {
        return "/assets/legacytranslations/font/font.txt";
    }
}
