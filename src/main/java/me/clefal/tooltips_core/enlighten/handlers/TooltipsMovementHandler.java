package me.clefal.tooltips_core.enlighten.handlers;

import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.ArrayList;

public class TooltipsMovementHandler {

    @SubscribeEvent
    public void onPost(ScreenEvent.MouseDragged.Post event){
        System.out.println("111");
    }
    @SubscribeEvent
    public void onPre(ScreenEvent.MouseDragged.Pre event){
        Screen screen = event.getScreen();
        //fixed first, then to the current focus;
        ArrayList<TooltipsWidget> tooltipsWidgets = new ArrayList<>(((ScreenDuck) screen).getAllFixed());
        boolean b = ((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null;
        if (b) tooltipsWidgets.add(((ScreenDuck) screen).tc$getCurrentFocusTooltips());
        for (TooltipsWidget tooltipsWidget : tooltipsWidgets) {
            if (tooltipsWidget.mouseDragged(event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY())) {
                break;
            }
        }

    }
}
