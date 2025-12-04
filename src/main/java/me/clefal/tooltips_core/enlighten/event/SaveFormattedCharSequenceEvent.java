package me.clefal.tooltips_core.enlighten.event;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SaveFormattedCharSequenceEvent extends Event {
    public ItemStack itemStack;
    public List<FormattedCharSequence> components;

    public SaveFormattedCharSequenceEvent(ItemStack itemStack, List<FormattedCharSequence> components) {
        this.itemStack = itemStack;
        this.components = components;
    }

    public static boolean tryPost(List<FormattedCharSequence> list, ItemStack itemStack){
        if (Screen.hasAltDown() && !TooltipsRecorder.isTakenOverByTC()) {
            TooltipsCore.clientBus.post(new SaveFormattedCharSequenceEvent(itemStack, list));
            return true;
        }
        return false;
    }
}
