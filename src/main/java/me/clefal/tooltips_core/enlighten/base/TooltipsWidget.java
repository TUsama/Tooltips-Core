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
import net.minecraft.client.gui.components.AbstractWidget;
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

public class TooltipsWidget extends AbstractWidget {
    private ItemStack itemStack;
    private Screen screen;
    private List<? extends FormattedText> originals;
    private List<? extends FormattedText> revealed;
    private List<ClientTooltipComponent> components;
    private MemorizedTooltipsPositioner positioner = new MemorizedTooltipsPositioner();
    private final BiMap<Rect2i, ClientTooltipComponent> linesPosition = HashBiMap.create();
    private boolean isPositionInit = false;
    private final static ResourceLocation PIN = TooltipsCore.gui("pin");
    @Getter
    private boolean isDragging = false;

    public TooltipsWidget(int x, int y, int width, int height, List<? extends FormattedText> components, ItemStack itemStack, Screen screen) {
        super(x, y, width, height, Component.empty());
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
        ScreenDuck screen1 = (ScreenDuck) screen;
        if (isHovered) {
            if (button == 2){
                if (this == screen1.tc$getCurrentFocusTooltips()) {
                    screen1.addToFixed(this);
                    screen1.tc$setCurrentFocusTooltips(null);
                } else if (screen1.getAllFixed().contains(this)) {
                    screen1.removeFromFixed(this);
                }
            } else if (button == 1) {
                screen1.removeFromFixed(this);
            } else if (button == 0) {
                Style styleAt = getStyleAt(mouseX, mouseY, Minecraft.getInstance().font);
                if (styleAt!= null && styleAt.getHoverEvent() != null && EnlightenUtil.isEnlighten(styleAt.getHoverEvent())){
                    Component value = styleAt.getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT);
                    if (value != null){
                        Option<TooltipsWidget> sameTargetWidget = getSameTargetWidget(screen, styleAt.hashCode());
                        if (sameTargetWidget.isEmpty()) {
                            TooltipsCore.clientBus.post(new DirectlyAddEnlightenToFixedEvent(ItemStack.EMPTY, List.of(value.copy()), styleAt.hashCode()));
                        } else {
                            TooltipsWidget tooltipsWidget = sameTargetWidget.get();
                            MemorizedTooltipsPositioner positioner1 = tooltipsWidget.positioner;
                            positioner1.accurateLastPosition = new Vector2d(mouseX, mouseY);
                            positioner1.lastPosition = new Vector2i(((int) positioner1.accurateLastPosition.x), ((int) positioner1.accurateLastPosition.y));

                            tooltipsWidget.setPosition(positioner1.lastPosition.x, positioner1.lastPosition.y);
                        }
                    }
                }
            }
            return true;
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
        Font font = Minecraft.getInstance().font;
        ((GuiGraphicsInvoker) guiGraphics).tc$renderTooltipInternal(font, this.components, getX(), getY(), positioner);
        guiGraphics.blit(PIN, getX() + width, getY() - 10, 5, 5, 0, 0, 32, 32, 32, 32);
        Style styleAt = getStyleAt(mouseX, mouseY, font);
        if (styleAt != null && styleAt.getHoverEvent() != null){
            int i = styleAt.hashCode();
            if (getSameTargetWidget(screen, styleAt.hashCode()).isEmpty()) {
                guiGraphics.renderComponentHoverEffect(font, styleAt, mouseX, mouseY);
            }
        }
    }

    //from PinnedTooltips
    public void updateSize(Font font) {
        var width = 0;
        var height = 0;
        this.linesPosition.clear();
        for (ClientTooltipComponent component : this.components) {
            var componentWidth = component.getWidth(font);
            var componentHeight = component.getHeight();
            this.linesPosition.put(new Rect2i(0, height, componentWidth, componentHeight), component);
            width = Math.max(width, componentWidth);
            height += componentHeight;
        }
    }

    @Nullable
    public Style getStyleAt(double mouseX, double mouseY, Font font) {
        var relativeX = (int) (mouseX - getX());
        var relativeY = (int) (mouseY - getY());
        var line = linesPosition.keySet().stream().filter(rect -> rect.contains(relativeX, relativeY)).findFirst().orElse(null);
        var component = linesPosition.get(line);
        if (component instanceof ClientTextTooltipAccess textTooltip) {
            return font.getSplitter().componentStyleAtWidth(textTooltip.getText(), relativeX);
        }
        return null;
    }

    private static Option<TooltipsWidget> getSameTargetWidget(Screen screen, int hashcode){

        return Option.ofOptional(((ScreenDuck) screen).getAllFixed().stream()
                        .filter(x -> (x instanceof EnlightenTooltipsWidget enlightenTooltipsWidget && enlightenTooltipsWidget.isSameTarget(hashcode)))
                        .findFirst());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }


}
