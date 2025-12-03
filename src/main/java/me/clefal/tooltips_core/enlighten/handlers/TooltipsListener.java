package me.clefal.tooltips_core.enlighten.handlers;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.SubscribeEvent;
import me.clefal.tooltips_core.enlighten.event.DirectlyAddEnlightenToFixedEvent;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import java.util.List;


public class TooltipsListener {

    @SubscribeEvent
    private void grabRawTooltips(SaveCurrentComponentsEvent event) {
        if (TooltipsRecorder.pendingTooltips == null) {
            TooltipsRecorder.setPendingTooltips(new TooltipsRecorder.TooltipsRecord(event.components, event.itemStack));
        }

    }

    @SubscribeEvent
    private void addToFixedForEnlighten(DirectlyAddEnlightenToFixedEvent event) {
        List<FormattedText> list = event.components.stream().map(x -> {
                    if (x instanceof Component component) {
                        Tuple2<Boolean, Component> tuple2 = EnlightenUtil.trimEnlighten(component);
                        if (tuple2._1) {
                            return tuple2._2;
                        }
                    }
                    return x;
                })
                .toList();
        //will be revealed when it becomes a widget.
        TooltipsRecorder.setAsFixed(new TooltipsRecorder.EnlightenTooltipsRecord(list, event.itemStack, event.styleHashcode));
    }


}
