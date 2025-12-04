package me.clefal.tooltips_core.mixin;

import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = Tooltip.class
//? >1.20.1
        ,remap = false
)
public class TooltipMixin {
    @ModifyVariable(
            method = "create(Lnet/minecraft/network/chat/Component;)Lnet/minecraft/client/gui/components/Tooltip;",
            at = @At(value = "HEAD"),
            argsOnly = true)
    private static Component reveal1(Component value){
        return ((Component) EnlightenUtil.reveal(value).get(0));
    }

    @ModifyVariable(
            method = "create(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/Component;)Lnet/minecraft/client/gui/components/Tooltip;",
            at = @At(value = "HEAD"),
            ordinal = 0,
            argsOnly = true)
    private static Component reveal2(Component message){
        return ((Component) EnlightenUtil.reveal(message).get(0));
    }
}
