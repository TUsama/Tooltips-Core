package me.clefal.tooltips_core.enlighten.event;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SaveFormattedCharSequenceEvent extends Event {
    public ItemStack itemStack;
    public List<FormattedCharSequence> components;
    public ClientTooltipPositioner positioner;

    public SaveFormattedCharSequenceEvent(ItemStack itemStack, List<FormattedCharSequence> components, ClientTooltipPositioner positioner) {
        this.itemStack = itemStack;
        this.components = components;
        this.positioner = positioner;
    }

    public static boolean tryPost(List<FormattedCharSequence> list, ItemStack itemStack, ClientTooltipPositioner positioner){
        if (Screen.hasAltDown() && !TooltipsRecorder.isTakenOverByTC()) {
            TooltipsCore.clientBus.post(new SaveFormattedCharSequenceEvent(itemStack, list, positioner));
            return true;
        }
        return false;
    }
}
