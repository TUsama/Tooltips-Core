package me.clefal.tooltips_core.enlighten.base;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class MemorizedTooltipsPositioner implements ClientTooltipPositioner {
    private final ClientTooltipPositioner positioner;
    protected Vector2ic lastPosition = null;
    protected Vector2ic lastRectangle = null;

    public MemorizedTooltipsPositioner(ClientTooltipPositioner positioner) {
        this.positioner = positioner;
    }

    public MemorizedTooltipsPositioner() {
        this.positioner = DefaultTooltipPositioner.INSTANCE;
    }

    @Override
    public Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        if (this.lastPosition == null){
            lastPosition = positioner.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight);
            lastRectangle = new Vector2i(tooltipWidth, tooltipHeight);
        }
        return lastPosition;
    }

}
