package me.clefal.tooltips_core.enlighten.event;

import com.clefal.nirvana_lib.relocated.net.neoforged.bus.api.Event;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class SaveCurrentComponentsEvent extends Event {
    public ItemStack itemStack;
    public List<? extends FormattedText> components;

    public SaveCurrentComponentsEvent(ItemStack itemStack, List<? extends FormattedText> components) {
        this.itemStack = itemStack;
        this.components = components;
    }

    public static boolean tryPost(List<? extends FormattedText> list, ItemStack itemStack){
        if (Screen.hasAltDown() && !TooltipsRecorder.isTakenOverByTC()) {
            TooltipsCore.clientBus.post(new SaveCurrentComponentsEvent(itemStack, list));
            System.out.println("post!");
            return true;
        }
        return false;
    }
}
