package me.clefal.tooltips_core.enlighten.base;

import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.GuiGraphicsInvoker;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector2d;
import org.joml.Vector2i;
import org.joml.Vector2ic;
//? 1.20.1 {
/*import net.minecraftforge.client.ForgeHooksClient;
*///?} else {
import net.neoforged.neoforge.client.ClientHooks;
//?}

import javax.annotation.Nullable;
import java.util.List;

public class TooltipsWidget extends AbstractWidget {
    private ItemStack itemStack;
    private Screen screen;
    private List<ClientTooltipComponent> components;
    private MemorizedTooltipsPositioner positioner = new MemorizedTooltipsPositioner();
    private boolean isPositionInit = false;
    private final static ResourceLocation PIN = TooltipsCore.gui("pin");
    private boolean isDragging = false;

    public TooltipsWidget(int x, int y, int width, int height, List<? extends FormattedText> components, ItemStack itemStack, Screen screen) {
        super(x, y, width, height, Component.empty());
        List<ClientTooltipComponent> clientTooltipComponents =
                //? 1.20.1 {
                /*ForgeHooksClient.
                *///?} else {
                ClientHooks.
                //?}



                        gatherTooltipComponents(itemStack, EnlightenUtil.reveal(components), x, Minecraft.getInstance().getWindow().getGuiScaledWidth(), Minecraft.getInstance().getWindow().getGuiScaledHeight(), Minecraft.getInstance().font);
        this.components = clientTooltipComponents;
        this.screen = screen;
    }

    //this method will only be invoked in my mod ig.
    //see TooltipsMovementHandler
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (isHovered || isDragging) {
            this.isDragging = true;
            positioner.accurateLastPosition = new Vector2d(positioner.accurateLastPosition.x() + dragX, positioner.accurateLastPosition.y() + dragY);
            positioner.lastPosition = new Vector2i(((int) positioner.accurateLastPosition.x), ((int) positioner.accurateLastPosition.y));

            this.setPosition(positioner.lastPosition.x, positioner.lastPosition.y);
            return true;
        }
        return false;
    }
    public void resetDragging(){
        this.isDragging = false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        System.out.println("mouse click: " + button);
        ScreenDuck screen1 = (ScreenDuck) screen;
        System.out.println(screen1.getAllFixed().size());
        if (isHovered && button == 2) {
            if (this == screen1.tc$getCurrentFocusTooltips()) {
                screen1.addToFixed(this);
                screen1.tc$setCurrentFocusTooltips(null);
            } else if (screen1.getAllFixed().contains(this)) {
                screen1.removeFromFixed(this);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        var lastRectangle = positioner.lastRectangle;
        if (lastRectangle != null) {
            if (!isPositionInit){
                //? 1.20.1 {
            /*this.setWidth(lastRectangle.z());
            this.setHeight(lastRectangle.w());
            this.setPosition(lastRectangle.x(), lastRectangle.y());
            *///?} else {
                this.setRectangle(lastRectangle.z(), lastRectangle.w(), lastRectangle.x(), lastRectangle.y());
                //?}

                isPositionInit = true;
            }

        }
        ((GuiGraphicsInvoker) guiGraphics).tc$renderTooltipInternal(Minecraft.getInstance().font, this.components, getX(), getY(), positioner);
        guiGraphics.blit(PIN, getX() + width, getY() - 10, 5, 5, 0, 0, 32, 32, 32, 32);
        //guiGraphics.fill(getX() + width - 8, getY() - 8, getX() + width, getY(), ChatFormatting.GRAY.getColor());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public record DragInfo(Vector2ic from, Vector2ic to) {

    }
}
