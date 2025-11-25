package me.clefal.tooltips_core.enlighten.utils;

import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@UtilityClass
public class MixinMethod {

    public static boolean wrapRenderWithTooltips(Screen screen, TooltipsWidget currentFocusTooltips, int mouseX, int mouseY) {
        System.out.println("start wrap!");
        //handle the in-queue tt
        if (TooltipsRecorder.pendingTooltips != null && currentFocusTooltips == null){
            System.out.println("wrap success!");
            TooltipsRecorder.TooltipsRecord pendingTooltips = TooltipsRecorder.getPendingTooltips();
            currentFocusTooltips = new TooltipsWidget(mouseX, mouseY, 5, 5, pendingTooltips.components(), pendingTooltips.itemStack(), screen);
            ((ScreenInvoker) screen).tc$addRenderableWidget(currentFocusTooltips);
            TooltipsRecorder.pendingTooltips = null;
        }
        return !TooltipsRecorder.isTakenOverByTC();
    }

    public static void handleComponent(Component tooltip) {
        System.out.println("from handleComponent");
        tooltip = EnlightenUtil.reveal(tooltip);
        SaveCurrentComponentsEvent.tryPost(List.of(tooltip), ItemStack.EMPTY);
    }

    public static void handleTooltips(TooltipsDuck duck) {
        System.out.println("from handleTooltips");
        duck.setMessage(EnlightenUtil.reveal(duck.getMessage()));
        SaveCurrentComponentsEvent.tryPost(List.of(duck.getMessage()), ItemStack.EMPTY);
    }
}
