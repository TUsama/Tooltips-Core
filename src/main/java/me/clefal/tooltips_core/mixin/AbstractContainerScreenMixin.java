package me.clefal.tooltips_core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AbstractContainerScreen.class)
public class AbstractContainerScreenMixin {

    @WrapOperation(
            method = "renderSlotHighlight(Lnet/minecraft/client/gui/GuiGraphics;IIII)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(Lnet/minecraft/client/renderer/RenderType;IIIIIII)V")
    )
    private static void tc$blockHighlight(GuiGraphics instance, RenderType renderType, int x1, int y1, int x2, int y2, int colorFrom, int colorTo, int z, Operation<Void> original) {
        ScreenDuck screen = (ScreenDuck) Minecraft.getInstance().screen;
        if (screen!= null){
            if (screen.tc$getCurrentFocusTooltips() != null || screen.getAllFixed().stream().anyMatch(AbstractWidget::isHovered)){

            } else {
                original.call(instance, renderType, x1, y1, x2, y2, colorFrom, colorTo, z);
            }
        }

    }
}
