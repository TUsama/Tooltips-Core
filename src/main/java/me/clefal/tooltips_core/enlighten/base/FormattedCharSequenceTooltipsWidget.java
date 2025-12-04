package me.clefal.tooltips_core.enlighten.base;

import com.mojang.blaze3d.systems.RenderSystem;
import me.clefal.tooltips_core.mixin.GuiGraphicsInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.stream.Collectors;

public class FormattedCharSequenceTooltipsWidget extends AbstractTooltipsWidget {

    private List<FormattedCharSequence> original;
    private List<ClientTooltipComponent> components;

    public FormattedCharSequenceTooltipsWidget(int x, int y, int width, int height, Screen screen, List<FormattedCharSequence> original, ClientTooltipPositioner positioner) {
        super(x, y, width, height, Component.empty(), screen);
        this.original = original;
        super.positioner = new MemorizedTooltipsPositioner(positioner);
        this.components = original.stream().map(ClientTooltipComponent::create).collect(Collectors.toList());
        updateSize(Minecraft.getInstance().font);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        super.renderWidget(guiGraphics, i, i1, v);
        ((GuiGraphicsInvoker) guiGraphics).tc$renderTooltipInternal(Minecraft.getInstance().font, this.components, getX(), getY(), positioner);
    }

    @Override
    public List<ClientTooltipComponent> getClientTooltipComponent() {
        return components;
    }

}
