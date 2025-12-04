package me.clefal.tooltips_core.enlighten.handlers;

import me.clefal.tooltips_core.enlighten.base.ComponentTooltipsWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class EnlightenTooltipsWidget extends ComponentTooltipsWidget {
    public final int identifier;

    public EnlightenTooltipsWidget(int x, int y, int width, int height, List<? extends FormattedText> components, ItemStack itemStack, Screen screen, int hashcode) {
        super(x, y, width, height, components, itemStack, screen);
        this.identifier = hashcode;
    }

    public boolean isSameTarget(int hashcode){
        return hashcode == identifier;
    }


}
