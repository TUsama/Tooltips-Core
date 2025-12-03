package me.clefal.tooltips_core.enlighten.event;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DirectlyAddEnlightenToFixedEvent extends Event {

    public ItemStack itemStack;
    public List<? extends FormattedText> components;
    public int styleHashcode;

    public DirectlyAddEnlightenToFixedEvent(ItemStack itemStack, List<? extends FormattedText> components, int styleHashcode) {
        this.itemStack = itemStack;
        this.components = components;
        this.styleHashcode = styleHashcode;
    }
}
