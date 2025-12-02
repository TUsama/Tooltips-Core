package me.clefal.tooltips_core.enlighten.handlers;


import me.clefal.tooltips_core.enlighten.base.IComponentProvider;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;


public class TooltipsRecorder {
    @Nullable
    public static TooltipsRecord pendingTooltips;

    public static List<TooltipsRecord> addToFixed = new ArrayList<>();

    @Nullable
    public static TooltipsRecord getPendingTooltips() {
        return pendingTooltips;
    }

    public static void setPendingTooltips(@Nullable TooltipsRecord pendingTooltips) {
        TooltipsRecorder.pendingTooltips = pendingTooltips;
    }

    public static boolean isWaitingToProcess() {
        return pendingTooltips != null && !(Minecraft.getInstance().screen != null && ((ScreenDuck) Minecraft.getInstance().screen).tc$isTakenOver());
    }

    public static boolean isTakenOverByTC() {
        return pendingTooltips != null || (Minecraft.getInstance().screen != null && ((ScreenDuck) Minecraft.getInstance().screen).tc$isTakenOver());
    }


    public record TooltipsRecord(List<? extends FormattedText> components,
                                 ItemStack itemStack) implements IComponentProvider<List<? extends FormattedText>> {

        @Override
        public List<? extends FormattedText> provide() {
            return components;
        }
    }
}
