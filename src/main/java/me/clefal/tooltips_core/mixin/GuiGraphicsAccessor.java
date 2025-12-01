package me.clefal.tooltips_core.mixin;

import me.clefal.tooltips_core.enlighten.utils.TooltipsDuck;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = GuiGraphics.class
//? >1.20.1
        ,remap = false
)
public interface GuiGraphicsAccessor {
    @Accessor
    ItemStack getTooltipStack();

    @Accessor
    void setTooltipStack(ItemStack stack);
}
