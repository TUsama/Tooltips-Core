package me.clefal.tooltips_core.mixin;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ClientTextTooltip.class
        //? >1.20.1
        ,remap = false
)
public interface ClientTextTooltipAccess {
    @Accessor
    FormattedCharSequence getText();
}
