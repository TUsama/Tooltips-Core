package me.clefal.tooltips_core.enlighten.base;

import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import me.clefal.tooltips_core.mixin.GuiGraphicsInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Vector2ic;

import java.util.List;

public class TooltipsWidget extends AbstractWidget {
    private ItemStack itemStack;
    private List<ClientTooltipComponent> components;
    private MemorizedTooltipsPositioner positioner = new MemorizedTooltipsPositioner();

    public TooltipsWidget(int x, int y, int width, int height, List<? extends FormattedText> components, ItemStack itemStack) {
        super(x, y, width, height, Component.empty());
        List<ClientTooltipComponent> clientTooltipComponents = ClientHooks.gatherTooltipComponents(itemStack, EnlightenUtil.reveal(components), x, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), Minecraft.getInstance().font);
        this.components = clientTooltipComponents;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Vector2ic lastRectangle = positioner.lastRectangle;
        if (lastRectangle != null){
            this.setRectangle(lastRectangle.x(), lastRectangle.y(), getX(), getY());
        }
        ((GuiGraphicsInvoker) guiGraphics).renderTooltipInternal(Minecraft.getInstance().font, this.components, getX(), getY(), positioner);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }
}
