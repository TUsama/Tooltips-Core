package me.clefal.tooltips_core.mixin;

import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @Inject(
            method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;IIII)V",
            at = @At(value = "HEAD"),
            cancellable = true
    )
    private static void tc$blockHighlight(GuiGraphics guiGraphics, int x, int y, int blitOffset, int color, CallbackInfo ci) {
        ScreenDuck screen = (ScreenDuck) Minecraft.getInstance().screen;
        if (screen!= null){
            if (screen.tc$getCurrentFocusTooltips() != null || screen.getAllFixed().stream().anyMatch(AbstractWidget::isHovered)){
                ci.cancel();
            }
        }

    }
}
