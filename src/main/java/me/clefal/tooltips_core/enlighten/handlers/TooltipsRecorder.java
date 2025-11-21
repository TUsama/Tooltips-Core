package me.clefal.tooltips_core.enlighten.handlers;


import me.clefal.tooltips_core.enlighten.base.IComponentProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.neoforged.neoforge.client.event.RenderTooltipEvent;

import javax.annotation.Nullable;
import java.util.List;


public class TooltipsRecorder {
    public record TooltipsRecord(List<? extends FormattedText> components) implements IComponentProvider<List<? extends FormattedText>> {

        @Override
        public List<? extends FormattedText> provide() {
            return components;
        }
    }

    @Nullable
    public static TooltipsRecord pendingTooltips;

    public static void recordTooltips(List<? extends FormattedText> components){
        pendingTooltips = new TooltipsRecord(components);
    }

    @Nullable
    public static TooltipsRecord getPendingTooltips() {
        return pendingTooltips;
    }
}
