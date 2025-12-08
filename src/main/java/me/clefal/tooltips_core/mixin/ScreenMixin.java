package me.clefal.tooltips_core.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.clefal.tooltips_core.enlighten.base.AbstractTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.ComponentTooltipsWidget;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import me.clefal.tooltips_core.enlighten.event.SaveFormattedCharSequenceEvent;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.*;

@Mixin(value = Screen.class
//? >1.20.1
        ,remap = false
)
public abstract class ScreenMixin implements ScreenDuck {


    @Unique
    @Nullable
    private AbstractTooltipsWidget currentFocusTooltips;
    @Unique
    private LinkedHashSet<AbstractTooltipsWidget> fixedTooltips = new LinkedHashSet<>();
    @Shadow
    @Final
    public List<Renderable> renderables;
    @Shadow
    @Final
    private List<GuiEventListener> children;
    @Shadow
    @Final
    private List<NarratableEntry> narratables;

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> T addFirstRenderableWidget(T widget) {
        this.renderables.add(0, widget);
        this.children.add(0, widget);
        this.narratables.add(0, widget);
        return widget;
    }

    @Override
    public <T extends GuiEventListener & NarratableEntry> T addWidgetToFirst(T listener) {
        this.children.add(0, listener);
        this.narratables.add(0, listener);
        return listener;
    }

    @Shadow
    protected abstract void removeWidget(GuiEventListener listener);

    @Unique
    public AbstractTooltipsWidget tc$getCurrentFocusTooltips() {
        return currentFocusTooltips;
    }

    @Unique
    public void tc$setCurrentFocusTooltips(AbstractTooltipsWidget currentFocusTooltips) {
        this.currentFocusTooltips = currentFocusTooltips;
    }

    @Override
    public boolean tc$isTakenOver() {
        return currentFocusTooltips != null;
    }

    @Override
    public AbstractTooltipsWidget addToFixed(AbstractTooltipsWidget widget) {
        //only 21 has addFirst().
        ArrayList<AbstractTooltipsWidget> tooltipsWidgets = new ArrayList<>(this.fixedTooltips);
        tooltipsWidgets.add(0, widget);
        this.fixedTooltips.clear();
        this.fixedTooltips.addAll(tooltipsWidgets);
        return widget;
    }

    @Override
    public Set<AbstractTooltipsWidget> getAllFixed() {
        return this.fixedTooltips;
    }

    @Override
    public List<AbstractTooltipsWidget> getAll() {
        ImmutableList.Builder<AbstractTooltipsWidget> builder = ImmutableList.builder();
        if (this.currentFocusTooltips != null) builder.add(currentFocusTooltips);
        ArrayList<AbstractTooltipsWidget> tooltipsWidgets = new ArrayList<>(getAllFixed());
        for (int i = tooltipsWidgets.size() - 1; i >= 0; i--) {
            builder.add(tooltipsWidgets.get(i));
        }
        return builder.build();
    }

    @Override
    public void removeFromFixed(AbstractTooltipsWidget widget) {
        this.fixedTooltips.remove(widget);
        Minecraft.getInstance().tell(() -> {
            this.removeWidget(widget);
        });

    }


    @Override
    public <T extends GuiEventListener & NarratableEntry> T raiseToFirstWidget(T listener) {
        this.removeWidget(listener);
        this.addWidgetToFirst(listener);
        return listener;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tc$tickRemoveCurrent(CallbackInfo ci) {
        if (!Screen.hasAltDown() && currentFocusTooltips != null) {
            this.removeWidget(currentFocusTooltips);
            currentFocusTooltips = null;
        }
    }

    @WrapOperation(method = "renderWithTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;II)V"))
    private void tc$renderWithTooltip$record1(GuiGraphics instance, Font font, List<FormattedCharSequence> tooltipLines, ClientTooltipPositioner tooltipPositioner, int mouseX, int mouseY, Operation<Void> original) {
        if (!SaveFormattedCharSequenceEvent.tryPost(tooltipLines, ItemStack.EMPTY, tooltipPositioner)) {
            original.call(instance, font, tooltipLines, tooltipPositioner, mouseX, mouseY);
        }
    }



}
