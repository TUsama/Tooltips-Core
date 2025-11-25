package me.clefal.tooltips_core.mixin;

import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mixin(value = Screen.class, remap = false)
public abstract class ScreenMixin implements ScreenDuck {


    @Unique
    @Nullable
    public TooltipsWidget currentFocusTooltips;
    @Unique
    public Set<TooltipsWidget> fixedTooltips = new HashSet<>();
    @Unique
    public List<TooltipsWidget> pendingToRemoved = new ArrayList<>();

    @Shadow
    protected abstract <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget);

    @Shadow
    protected abstract void removeWidget(GuiEventListener listener);


    @Unique
    public TooltipsWidget tc$getCurrentFocusTooltips() {
        return currentFocusTooltips;
    }

    @Unique
    public void tc$setCurrentFocusTooltips(TooltipsWidget currentFocusTooltips) {
        this.currentFocusTooltips = currentFocusTooltips;
    }

    @Override
    public boolean tc$isTakenOver() {
        return currentFocusTooltips != null;
    }

    @Override
    public TooltipsWidget addToFixed(TooltipsWidget widget) {
        this.fixedTooltips.add(widget);
        return widget;
    }

    @Override
    public Set<TooltipsWidget> getAllFixed() {
        return this.fixedTooltips;
    }

    @Override
    public void removeFromFixed(TooltipsWidget widget) {
        this.fixedTooltips.remove(widget);
        Minecraft.getInstance().tell(() -> {
            this.removeWidget(widget);
        });

    }

    @Override
    public void addToRemoved(TooltipsWidget widget) {

    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tc$tickRemoveCurrent(CallbackInfo ci) {
        if (!Screen.hasAltDown() && currentFocusTooltips != null){
            this.removeWidget(currentFocusTooltips);
            currentFocusTooltips = null;
        }
    }
/*
    @Inject(method = "setTooltipForNextRenderPass(Lnet/minecraft/network/chat/Component;)V", at = @At("HEAD"))
    private void tc$handleComponent(Component tooltip, CallbackInfo ci) {
        MixinMethod.handleComponent(tooltip);
    }

    @Inject(method = "setTooltipForNextRenderPass(Lnet/minecraft/client/gui/components/Tooltip;Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;Z)V", at = @At("HEAD"))
    private void tc$handleTooltip(Tooltip tooltip, ClientTooltipPositioner positioner, boolean override, CallbackInfo ci) {
        MixinMethod.handleTooltips((TooltipsDuck) tooltip);
    }

    @Redirect(method = "renderWithTooltip",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;II)V"))
    private void tc$renderTooltipWrap(GuiGraphics instance, Font font, List<FormattedCharSequence> tooltipLines, ClientTooltipPositioner tooltipPositioner, int mouseX, int mouseY) {
        if (MixinMethod.wrapRenderWithTooltips(((Screen) ((Object) this)), currentFocusTooltips, mouseX, mouseY)) {
            instance.renderTooltip(font, tooltipLines, tooltipPositioner, mouseX, mouseY);
        }
    }
*/

}
