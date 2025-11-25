package me.clefal.tooltips_core.enlighten.handlers;


import me.clefal.tooltips_core.enlighten.base.IComponentProvider;
import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import me.clefal.tooltips_core.mixin.ScreenMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.ScreenEvent;

import javax.annotation.Nullable;
import java.util.List;


public class TooltipsRecorder {
    @Nullable
    public static TooltipsRecord pendingTooltips;

    @Nullable
    public static TooltipsRecord getPendingTooltips() {
        return pendingTooltips;
    }

    public static void setPendingTooltips(@Nullable TooltipsRecord pendingTooltips) {
        TooltipsRecorder.pendingTooltips = pendingTooltips;
    }

    public static boolean isWaitingToProcess(){
        return pendingTooltips != null && !(Minecraft.getInstance().screen != null && ((ScreenDuck) Minecraft.getInstance().screen).tc$isTakenOver());
    }

    public static boolean isTakenOverByTC(){
        return pendingTooltips != null || (Minecraft.getInstance().screen != null && ((ScreenDuck) Minecraft.getInstance().screen).tc$isTakenOver());
    }

    public static void OnScreenPre(ScreenEvent.Render.Pre event){
        if (isWaitingToProcess()){
            Screen screen = event.getScreen();
            System.out.println("OnScreenPre");
            TooltipsWidget tooltipsWidget = new TooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, pendingTooltips.provide(), pendingTooltips.itemStack(), event.getScreen());
            ((ScreenInvoker) screen).tc$addRenderableWidget(tooltipsWidget);
            ((ScreenDuck) screen).tc$setCurrentFocusTooltips(tooltipsWidget);
            pendingTooltips = null;
        }

    }

    public record TooltipsRecord(List<? extends FormattedText> components,
                                 ItemStack itemStack) implements IComponentProvider<List<? extends FormattedText>> {

        @Override
        public List<? extends FormattedText> provide() {
            return components;
        }
    }
}
