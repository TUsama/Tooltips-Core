package me.clefal.tooltips_core.enlighten.handlers;

import me.clefal.tooltips_core.enlighten.base.AbstractTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.BypassTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.base.MemorizedTooltipsPositioner;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
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
import java.util.List;

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
            event.getScreen().setFocused(TooltipsRecorder.getPendingTooltips().setUpWidget(event));;
            TooltipsRecorder.setPendingTooltips(null);
        }


    }

    @SubscribeEvent
    public void postRenderTooltipsWidget(ScreenEvent.Render.Post event) {
        //since I render the widget on Post, The widget hover event will be a little bit earlier triggered, resulting a visual bug that when the first line is enlightened, the enlightened content will be rendered prob for 1 frame.
        //IDK if this is really the cause tho.
        Screen screen = event.getScreen();
        List<AbstractTooltipsWidget> all = ((ScreenDuck) screen).getAll();
        GuiGraphics guiGraphics = event.getGuiGraphics();

        for (int i = 0; i < all.size(); i++) {
            AbstractTooltipsWidget tooltipsWidget = all.get(i);
            guiGraphics.pose().pushPose();
            if (tooltipsWidget.isFocused()) {
                guiGraphics.pose().translate(0, 0, all.size() * 400);
                ((ScreenDuck) screen).raiseToFirstWidget(tooltipsWidget);
            } else {
                guiGraphics.pose().translate(0, 0, i * 400);
            }
            tooltipsWidget.render(event.getGuiGraphics(), event.getMouseX(), event.getMouseY(), event.getPartialTick());
            guiGraphics.pose().popPose();
        }


        if (!Screen.hasAltDown() && ((ScreenDuck) screen).tc$getCurrentFocusTooltips() != null) {
            ((ScreenDuck) screen).resetCurrentFocusTooltips();
        }

    }

}
