package me.clefal.tooltips_core.mixin;

import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(value = GuiGraphics.class, remap = false)
public class GuiGraphicsMixin {

    @Shadow
    private ItemStack tooltipStack;

    @Inject(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V", at = @At("HEAD"))
    private void tc$renderTooltip$record1(Font font, List<Component> tooltipLines, int mouseX, int mouseY, CallbackInfo ci) {
        SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack);
    }

    @Inject(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/world/item/ItemStack;)V", at = @At("HEAD"))
    private void tc$renderTooltip$record2(Font p_font, List<? extends FormattedText> tooltipLines, int p_mouseX, int p_mouseY, ItemStack stack, CallbackInfo ci) {
        SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack);
    }

    @Inject(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"))
    private void tc$renderTooltip$record3(Font font, List<Component> tooltipLines, Optional<TooltipComponent> visualTooltipComponent, int mouseX, int mouseY, CallbackInfo ci) {
        SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack);
    }


/*
    @Inject(method = "renderComponentTooltipFromElements", at = @At("HEAD"))
    private void tc$renderTooltip$record4(Font font, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int mouseY, ItemStack stack, CallbackInfo ci) {
        //SaveCurrentComponentsEvent.post(tooltipLines);
    }*/
}
