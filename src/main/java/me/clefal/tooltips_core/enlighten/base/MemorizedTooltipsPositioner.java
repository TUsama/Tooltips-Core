package me.clefal.tooltips_core.enlighten.base;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import org.joml.*;

public class MemorizedTooltipsPositioner implements ClientTooltipPositioner {
    private final ClientTooltipPositioner positioner;
    public Vector2d accurateLastPosition;
    public Vector2i lastPosition = null;
    protected Vector4i lastRectangle = null;

    public MemorizedTooltipsPositioner(ClientTooltipPositioner positioner) {
        this.positioner = positioner;
    }

    public MemorizedTooltipsPositioner() {
        this.positioner = DefaultTooltipPositioner.INSTANCE;
    }

    @Override
    public Vector2ic positionTooltip(int screenWidth, int screenHeight, int mouseX, int mouseY, int tooltipWidth, int tooltipHeight) {
        if (this.lastPosition == null){
            lastPosition = ((Vector2i) positioner.positionTooltip(screenWidth, screenHeight, mouseX, mouseY, tooltipWidth, tooltipHeight));
            accurateLastPosition = new Vector2d(lastPosition.x(), lastPosition.y());
            lastRectangle = new Vector4i(lastPosition.x(), lastPosition.y(), tooltipWidth, tooltipHeight);
        }
        return lastPosition;
    }


}
