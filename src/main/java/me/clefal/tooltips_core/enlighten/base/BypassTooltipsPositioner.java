package me.clefal.tooltips_core.enlighten.base;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.joml.Vector2ic;

public class BypassTooltipsPositioner implements ClientTooltipPositioner {
    private final ClientTooltipPositioner positioner;

    public BypassTooltipsPositioner(ClientTooltipPositioner positioner) {
        this.positioner = positioner;
    }

    public BypassTooltipsPositioner() {
        this.positioner = DefaultTooltipPositioner.INSTANCE;
    }

    @Override
    public Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        return positioner.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
    }
}
