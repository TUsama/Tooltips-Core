package me.clefal.tooltips_core.enlighten.handlers;

import me.clefal.tooltips_core.enlighten.base.BypassTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.base.MemorizedTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
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
    public void onPre(RenderTooltipEvent.Pre event) {
        Screen screen = Minecraft.getInstance().screen;
        if (screen != null && (((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null || ((ScreenDuck) screen).getAllFixed().stream().anyMatch(AbstractWidget::isHovered)) && !(event.getTooltipPositioner() instanceof MemorizedTooltipsPositioner || event.getTooltipPositioner() instanceof BypassTooltipsPositioner)) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void onPre(ScreenEvent.MouseDragged.Pre event) {
        Screen screen = event.getScreen();
        //fixed first, then to the current focus;
        ArrayList<TooltipsWidget> tooltipsWidgets = new ArrayList<>(((ScreenDuck) screen).getAllFixed());
        boolean b = ((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null;
        if (b) tooltipsWidgets.add(((ScreenDuck) screen).tc$getCurrentFocusTooltips());

        tooltipsWidgets.stream()
                .filter(TooltipsWidget::isDragging)
                .findFirst()
                .ifPresentOrElse(x -> {
                    //means you were dragging a widget, so keep dragging it.
                    x.mouseDragged(event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY());
                    event.setCanceled(true);
                }, () ->{
                    boolean flag = false;
                    for (TooltipsWidget tooltipsWidget : tooltipsWidgets) {
                        if (tooltipsWidget.mouseDragged(event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY())) {
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
        ArrayList<TooltipsWidget> tooltipsWidgets = new ArrayList<>(((ScreenDuck) screen).getAllFixed());
        boolean b = ((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null;
        if (b) tooltipsWidgets.add(((ScreenDuck) screen).tc$getCurrentFocusTooltips());
        for (TooltipsWidget tooltipsWidget : tooltipsWidgets) {
            tooltipsWidget.resetDragging();
        }
    }

    @SubscribeEvent
    public void OnScreenPre(ScreenEvent.Render.Pre event) {
        Screen screen = event.getScreen();
        if (TooltipsRecorder.isWaitingToProcess()) {
            TooltipsWidget tooltipsWidget = new TooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, TooltipsRecorder.pendingTooltips.provide(), TooltipsRecorder.pendingTooltips.itemStack, event.getScreen());
            ((ScreenInvoker) screen).tc$addRenderableWidget(tooltipsWidget);
            ((ScreenDuck) screen).tc$setCurrentFocusTooltips(tooltipsWidget);
            TooltipsRecorder.pendingTooltips = null;

        }

        TooltipsRecorder.consumeFixed(x -> {
            if (!x.isEmpty()) {
                Iterator<TooltipsRecorder.TooltipsRecord> iterator = x.iterator();
                while (iterator.hasNext()) {
                    TooltipsRecorder.TooltipsRecord next = iterator.next();
                    TooltipsWidget fixed;
                    if (next instanceof TooltipsRecorder.EnlightenTooltipsRecord enlightenTooltipsRecord) {

                        fixed = new EnlightenTooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, enlightenTooltipsRecord.provide(), enlightenTooltipsRecord.itemStack, event.getScreen(), enlightenTooltipsRecord.hashcode);

                    } else {

                        fixed = new TooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, next.provide(), next.itemStack, event.getScreen());

                    }


                    ((ScreenDuck) screen).addFirstRenderableWidget(fixed);
                    ((ScreenDuck) screen).addToFixed(fixed);
                    iterator.remove();
                }
            }
        });


    }
}
