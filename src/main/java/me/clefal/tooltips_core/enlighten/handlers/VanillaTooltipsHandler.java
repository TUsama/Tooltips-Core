package me.clefal.tooltips_core.enlighten.handlers;

import me.clefal.tooltips_core.enlighten.base.MemorizedTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import net.minecraft.client.Minecraft;
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

public class VanillaTooltipsHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onPre(RenderTooltipEvent.Pre event){
        if (Minecraft.getInstance().screen != null && ((ScreenDuck) Minecraft.getInstance().screen).tc$getCurrentFocusTooltips() != null && !(event.getTooltipPositioner() instanceof MemorizedTooltipsPositioner)){
            event.setCanceled(true);
        }
    }

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
        boolean flag = false;
        for (TooltipsWidget tooltipsWidget : tooltipsWidgets) {
            if (tooltipsWidget.mouseDragged(event.getMouseX(), event.getMouseY(), event.getMouseButton(), event.getDragX(), event.getDragY())) {
                flag = true;
                break;
            }
        }
        if (flag) event.setCanceled(true);

    }

    @SubscribeEvent
    public void onRelease(ScreenEvent.MouseButtonReleased.Post event){
        Screen screen = event.getScreen();
        ArrayList<TooltipsWidget> tooltipsWidgets = new ArrayList<>(((ScreenDuck) screen).getAllFixed());
        boolean b = ((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null;
        if (b) tooltipsWidgets.add(((ScreenDuck) screen).tc$getCurrentFocusTooltips());
        for (TooltipsWidget tooltipsWidget : tooltipsWidgets) {
            tooltipsWidget.resetDragging();
        }
    }

    @SubscribeEvent
    public void OnScreenPre(ScreenEvent.Render.Pre event){
        if (TooltipsRecorder.isWaitingToProcess()){
            Screen screen = event.getScreen();
            System.out.println("OnScreenPre");
            TooltipsWidget tooltipsWidget = new TooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, TooltipsRecorder.pendingTooltips.provide(), TooltipsRecorder.pendingTooltips.itemStack(), event.getScreen());
            ((ScreenInvoker) screen).tc$addRenderableWidget(tooltipsWidget);
            ((ScreenDuck) screen).tc$setCurrentFocusTooltips(tooltipsWidget);
            TooltipsRecorder.pendingTooltips = null;
        }

    }
}
