package me.clefal.tooltips_core.enlighten.base;

import me.clefal.tooltips_core.mixin.GuiGraphicsInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;
import java.util.stream.Collectors;

public class FormattedCharSequenceTooltipsWidget extends AbstractTooltipsWidget {

    private List<FormattedCharSequence> original;
    private List<ClientTooltipComponent> components;

    public FormattedCharSequenceTooltipsWidget(int x, int y, int width, int height, Screen screen, List<FormattedCharSequence> original) {
        super(x, y, width, height, Component.empty(), screen);
        this.original = original;
        this.components = original.stream().map(ClientTooltipComponent::create).collect(Collectors.toList());
        updateSize(Minecraft.getInstance().font);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        ((GuiGraphicsInvoker) guiGraphics).tc$renderTooltipInternal(Minecraft.getInstance().font, this.components, getX(), getY(), positioner);
    }

    @Override
    public List<ClientTooltipComponent> getClientTooltipComponent() {
        return components;
    }

}
