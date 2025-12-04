package me.clefal.tooltips_core.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.clefal.tooltips_core.enlighten.base.BypassTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//? =1.20.1 {
/*@Mixin(GuiGraphics.class)
 *///?} else {
@Mixin(value = GuiGraphics.class, remap = false)
//?}
public abstract class GuiGraphicsMixin {

    @Shadow(remap = false)
    private ItemStack tooltipStack;

    @Shadow
    public abstract int guiWidth();

    @Shadow
    protected abstract void renderTooltipInternal(Font font, List<ClientTooltipComponent> components, int mouseX, int mouseY, ClientTooltipPositioner tooltipPositioner);

    @WrapOperation(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V")
    )
    private void tc$renderTooltip$record1(GuiGraphics instance, Font font, List<ClientTooltipComponent> clienttooltipcomponent1, int mouseX, int mouseY, ClientTooltipPositioner k2, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) List<Component> tooltipLines) {
        if (!SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack)) {
            original.call(instance, font, tooltipLines, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }

    @WrapOperation(method = "renderComponentTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V"))
    private void tc$renderTooltip$record2(GuiGraphics instance, Font font, List<ClientTooltipComponent> clienttooltipcomponent1, int mouseX, int mouseY, ClientTooltipPositioner positioner, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) List<? extends FormattedText> tooltipLines) {
        if (!SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack)) {
            original.call(instance, font, clienttooltipcomponent1, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }

    @WrapOperation(method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;Ljava/util/Optional;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V"))
    private void tc$renderTooltip$record3(GuiGraphics instance, Font font, List<ClientTooltipComponent> clienttooltipcomponent1, int mouseX, int mouseY, ClientTooltipPositioner positioner, Operation<Void> original, @Local(ordinal = 0, argsOnly = true) List<? extends FormattedText> tooltipLines) {
        if (!SaveCurrentComponentsEvent.tryPost(tooltipLines, this.tooltipStack)) {
            original.call(instance, font, clienttooltipcomponent1, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE);
        }
    }


    @WrapOperation(
            method = "renderComponentHoverEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V")
    )
    private void tc$revealComponent(GuiGraphics instance, Font font, List<? extends FormattedCharSequence> tooltipLines, int mouseX, int mouseY, Operation<Void> original, @Local Component component){
        var tuple2 = EnlightenUtil.trimEnlighten(component);
        Component component1 = ((Component) EnlightenUtil.reveal(tuple2._2).get(0));
        if (!SaveCurrentComponentsEvent.tryPost(List.of(component1), this.tooltipStack)) {
            List<FormattedCharSequence> split = new ArrayList<>(font.split(component1, Math.max(this.guiWidth() / 2, 200)));
            split.add(FormattedCharSequence.EMPTY);
            original.call(instance, font, split, mouseX, mouseY);
        }
    }

    @WrapOperation(
            method = "renderTooltip(Lnet/minecraft/client/gui/Font;Ljava/util/List;II)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderTooltipInternal(Lnet/minecraft/client/gui/Font;Ljava/util/List;IILnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)V")
    )
    private void tc$checkIfBypass(GuiGraphics instance, Font font, List<ClientTooltipComponent> clienttooltipcomponent, int mouseX, int mouseY, ClientTooltipPositioner positioner, Operation<Void> original, @Local(argsOnly = true) List<? extends FormattedCharSequence> tooltipLines){
        if (!tooltipLines.isEmpty() && tooltipLines.get(tooltipLines.size() - 1).equals(FormattedCharSequence.EMPTY)){
            ArrayList<? extends FormattedCharSequence> formattedCharSequences = new ArrayList<>(tooltipLines);
            formattedCharSequences.remove(formattedCharSequences.size() - 1);
            this.renderTooltipInternal(
                    font,
                    formattedCharSequences.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()),
                    mouseX,
                    mouseY,
                    new BypassTooltipsPositioner()
            );
        } else {
            original.call(instance, font, clienttooltipcomponent, mouseX, mouseY, positioner);
        }
    }



/*
    @Inject(method = "renderComponentTooltipFromElements", at = @At("HEAD"))
    private void tc$renderTooltip$record4(Font font, List<Either<FormattedText, TooltipComponent>> elements, int mouseX, int mouseY, ItemStack stack, CallbackInfo ci) {
        //SaveCurrentComponentsEvent.post(tooltipLines);
    }*/
}
