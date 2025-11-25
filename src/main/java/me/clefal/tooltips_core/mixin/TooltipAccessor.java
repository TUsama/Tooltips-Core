package me.clefal.tooltips_core.mixin;

import me.clefal.tooltips_core.enlighten.utils.TooltipsDuck;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = Tooltip.class)
public class TooltipAccessor implements TooltipsDuck {

    @Mutable
    @Shadow
    @Final
    private Component message;

    public void setMessage(Component component){
        this.message = component;
    }

    public Component getMessage() {
        return message;
    }
}
