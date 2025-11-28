package me.clefal.tooltips_core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(value = GuiGraphics.class, remap = false)
public class GuiGraphicsMixin {

    @Shadow
    private ItemStack tooltipStack;

    @WrapOperation(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V"))
    private void tc$renderTooltip$record1(GuiGraphics instance, Font font, List<ClientTooltipComponent> clienttooltipcomponent1, int mouseX, int mouseY, ClientTooltipPositioner k2, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) List<? extends net.minecraft.network.chat.FormattedText> tooltipLines) {
        if (!SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack)) {
            original.call(instance, font, tooltipLines, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }

    @WrapOperation(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V"))
    private void tc$renderTooltip$record2(GuiGraphics instance, Font font, List<ClientTooltipComponent> clienttooltipcomponent1, int mouseX, int mouseY, ClientTooltipPositioner positioner, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) List<? extends net.minecraft.network.chat.FormattedText> tooltipLines) {
        if (!SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack)) {
            original.call(instance, font, clienttooltipcomponent1, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }

    @WrapOperation(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V"))
    private void tc$renderTooltip$record3(GuiGraphics instance, Font font, List<ClientTooltipComponent> clienttooltipcomponent1, int mouseX, int mouseY, ClientTooltipPositioner positioner, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) List<? extends net.minecraft.network.chat.FormattedText> tooltipLines) {
        if (!SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack)) {
            original.call(instance, font, clienttooltipcomponent1, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }


/*
    @Inject(method = "renderComponentTooltipFromElements", at = @At("HEAD"))
    private void tc$renderTooltip$record4(Font font, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int mouseY, ItemStack stack, CallbackInfo ci) {
        //SaveCurrentComponentsEvent.post(tooltipLines);
    }*/
}
