package me.clefal.tooltips_core.enlighten.handlers;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;


public class TooltipsListener {

    @SubscribeEvent
    private void grabRawTooltips(SaveCurrentComponentsEvent event) {
        System.out.println("grab!");
        if (TooltipsRecorder.pendingTooltips == null) {
            System.out.println("set pending tooltips!");
            TooltipsRecorder.setPendingTooltips(new TooltipsRecorder.TooltipsRecord(event.components, event.itemStack));
        }

    }


}
