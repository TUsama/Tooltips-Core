package me.clefal.tooltips_core.enlighten.event;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SaveCurrentComponentsEvent extends Event {
    private ItemStack itemStack;
    private List<? extends FormattedText> components;

    public SaveCurrentComponentsEvent(ItemStack itemStack, List<? extends FormattedText> components) {
        this.itemStack = itemStack;
        this.components = components;
    }

    public static SaveCurrentComponentsEvent tryPost(List<? extends FormattedText> list, ItemStack itemStack){
        return new SaveCurrentComponentsEvent(itemStack, list);
    }
}
