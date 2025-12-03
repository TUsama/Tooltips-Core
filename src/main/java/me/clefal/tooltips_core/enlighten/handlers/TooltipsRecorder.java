package me.clefal.tooltips_core.enlighten.handlers;


import me.clefal.tooltips_core.enlighten.base.IComponentProvider;
import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;


public class TooltipsRecorder {
    @Nullable
    public static TooltipsRecord pendingTooltips;

    private static List<TooltipsRecord> addToFixed = new ArrayList<>();

    public static void setAsFixed(TooltipsRecord record){
        addToFixed.add(record);
    }

    public static void consumeFixed(Consumer<List<TooltipsRecord>> consumer){
        consumer.accept(addToFixed);
    }

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


    public static class TooltipsRecord implements IComponentProvider<List<? extends FormattedText>> {

        public List<? extends FormattedText> components;
        public ItemStack itemStack;

        public TooltipsRecord(List<? extends FormattedText> components, ItemStack itemStack) {
            this.components = components;
            this.itemStack = itemStack;
        }

        @Override
        public List<? extends FormattedText> provide() {
            return components;
        }
    }

    public static class EnlightenTooltipsRecord extends TooltipsRecord{

        public int hashcode;

        public EnlightenTooltipsRecord(List<? extends FormattedText> components, ItemStack itemStack, int hashcode) {
            super(components, itemStack);
            this.hashcode = hashcode;
        }
    }
}
