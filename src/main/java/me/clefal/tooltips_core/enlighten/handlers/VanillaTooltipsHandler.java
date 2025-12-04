package me.clefal.tooltips_core.enlighten.handlers;

import me.clefal.tooltips_core.enlighten.base.AbstractTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.BypassTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.base.ComponentTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.MemorizedTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
//? 1.20.1 {
/*import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
*///?} else {
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
//?}


import java.util.ArrayList;
import java.util.Iterator;

public class VanillaTooltipsHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void filterTT(RenderTooltipEvent.Pre event) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen != null && (((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null || ((ScreenDuck) screen).getAllFixed().stream().anyMatch(AbstractWidget::isHovered)) && !(event.getTooltipPositioner() instanceof MemorizedTooltipsPositioner || event.getTooltipPositioner() instanceof BypassTooltipsPositioner)) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void preDragHandle(ScreenEvent.MouseDragged.Pre event) {
        Screen screen = event.getScreen();
        //fixed first, then to the current focus;
        ArrayList<AbstractTooltipsWidget> tooltipsWidgets = new ArrayList<>(((ScreenDuck) screen).getAllFixed());
        boolean b = ((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null;
        if (b) tooltipsWidgets.add(((ScreenDuck) screen).tc$getCurrentFocusTooltips());

        tooltipsWidgets.stream()
                .filter(x -> x.isDragging())
                .findFirst()
                .ifPresentOrElse(x -> {
                    //means you were dragging a widget, so keep dragging it.
                    x.mouseDragged(event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY());
                    event.setCanceled(true);
                }, () ->{
                    boolean flag = false;
                    for (AbstractTooltipsWidget componentTooltipsWidget : tooltipsWidgets) {
                        if (componentTooltipsWidget.mouseDragged(event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY())) {
                            flag = true;
                            break;
                        }
                    }
                    if (flag) event.setCanceled(true);
                });


    }

    @SubscribeEvent
    public void onRelease(ScreenEvent.MouseButtonReleased.Post event) {
        Screen screen = event.getScreen();
        ArrayList<AbstractTooltipsWidget> tooltipsWidgets = new ArrayList<>(((ScreenDuck) screen).getAllFixed());
        boolean b = ((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null;
        if (b) tooltipsWidgets.add(((ScreenDuck) screen).tc$getCurrentFocusTooltips());
        for (AbstractTooltipsWidget componentTooltipsWidget : tooltipsWidgets) {
            componentTooltipsWidget.resetDragging();
        }
    }

    @SubscribeEvent
    public void OnScreenPre(ScreenEvent.Render.Pre event) {
        if (TooltipsRecorder.ifTooltipsRecordPending()) {
            TooltipsRecorder.getPendingTooltips().setUpWidget(event);
            TooltipsRecorder.setPendingTooltips(null);
        }


    }
}
