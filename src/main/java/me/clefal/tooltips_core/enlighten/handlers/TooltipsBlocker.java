package me.clefal.tooltips_core.enlighten.handlers;

import me.clefal.tooltips_core.enlighten.base.MemorizedTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

public class TooltipsBlocker {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPre(RenderTooltipEvent.Pre event){
        if (Minecraft.getInstance().screen != null && ((ScreenDuck) Minecraft.getInstance().screen).tc$getCurrentFocusTooltips() != null && !(event.getTooltipPositioner() instanceof MemorizedTooltipsPositioner)){
            event.setCanceled(true);
        }
    }
}
