package me.clefal.tooltips_core.enlighten.base;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.event.DirectlyAddEnlightenToFixedEvent;
import me.clefal.tooltips_core.enlighten.handlers.EnlightenTooltipsWidget;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.ClientTextTooltipAccess;
import me.clefal.tooltips_core.mixin.GuiGraphicsInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.joml.Vector2i;
//? 1.20.1 {
/*import net.minecraftforge.client.ForgeHooksClient;
*///?} else {
import net.neoforged.neoforge.client.ClientHooks;
//?}

import java.util.ArrayList;
import java.util.List;

public class ComponentTooltipsWidget extends AbstractTooltipsWidget {
    private ItemStack itemStack;

    private List<? extends FormattedText> originals;
    private List<? extends FormattedText> revealed;
    private List<ClientTooltipComponent> components;


    public ComponentTooltipsWidget(int x, int y, int width, int height, List<? extends FormattedText> components, ItemStack itemStack, Screen screen) {
        super(x, y, width, height, Component.empty(), screen);
        this.originals = tryCopy(components);
        this.revealed = EnlightenUtil.reveal(components);
        List<ClientTooltipComponent> clientTooltipComponents =
                //? 1.20.1 {
                /*ForgeHooksClient.
                *///?} else {
                ClientHooks.
                //?}



                        gatherTooltipComponents(ItemStack.EMPTY, this.revealed, x, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), Minecraft.getInstance().font);
        this.components = clientTooltipComponents;
        this.screen = screen;
        updateSize(Minecraft.getInstance().font);
    }

    private static List<? extends FormattedText> tryCopy(List<? extends FormattedText> originals){
        ArrayList<FormattedText> formattedTexts = new ArrayList<>();
        for (FormattedText original : originals) {
            if (original instanceof Component component){
                formattedTexts.add(component.copy());
            } else {
                formattedTexts.add(original);
            }
        }
        return formattedTexts;
    }


    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        Font font = Minecraft.getInstance().font;
        ((GuiGraphicsInvoker) guiGraphics).tc$renderTooltipInternal(font, this.components, getX(), getY(), positioner);
    }

    @Override
    public List<ClientTooltipComponent> getClientTooltipComponent() {
        return components;
    }
}
