package me.clefal.tooltips_core.enlighten.base;

import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

import net.minecraft.world.item.ItemStack;
//? 1.20.1 {
/*import net.minecraftforge.client.ForgeHooksClient;
*///?} else {
import net.neoforged.neoforge.client.ClientHooks;
//?}

import java.util.List;

public class EnlightenTooltipsWidget extends ComponentTooltipsWidget {
    public final int identifier;
    private Component revealed;
    private List<ClientTooltipComponent> clientTooltipComponent;

    public EnlightenTooltipsWidget(int x, int y, int width, int height, List<? extends FormattedText> components, ItemStack itemStack, Screen screen, int hashcode) {
        super(x, y, width, height, components, itemStack, screen);
        this.identifier = hashcode;
        this.revealed = ((Component) EnlightenUtil.reveal(originals).get(0));


        List<ClientTooltipComponent> clientTooltipComponents =
                //? 1.20.1 {
                /*ForgeHooksClient.
                        *///?} else {
                        ClientHooks.
                         //?}
                        gatherTooltipComponents(ItemStack.EMPTY, List.of(this.revealed), x, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), Minecraft.getInstance().font);

        if (clientTooltipComponents.isEmpty()) throw new RuntimeException("try to init enlighten, but nothing left after gatherTooltipComponents fired!");
        this.clientTooltipComponent = clientTooltipComponents;
        updateSize(Minecraft.getInstance().font);
    }

    @Override
    public List<ClientTooltipComponent> getClientTooltipComponent() {
        if (clientTooltipComponent == null) {
            return List.of();
        }
        return this.clientTooltipComponent;
    }

    public boolean isSameTarget(int hashcode) {
        return hashcode == identifier;
    }


}
