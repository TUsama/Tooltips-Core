package me.clefal.tooltips_core.enlighten.base;

import com.clefal.nirvana_lib.relocated.io.vavr.control.Option;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.config.TooltipsCoreConfig;
import me.clefal.tooltips_core.enlighten.event.DirectlyAddEnlightenToFixedEvent;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.ClientTextTooltipAccess;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2d;
import org.joml.Vector2i;

import java.util.List;

public abstract class AbstractTooltipsWidget extends AbstractWidget {

    protected MemorizedTooltipsPositioner positioner = new MemorizedTooltipsPositioner();
    protected final BiMap<Rect2i, ClientTooltipComponent> linesPosition = HashBiMap.create();
    protected boolean isPositionInit = false;
    protected final static ResourceLocation PIN = TooltipsCore.gui("pin");
    @Getter
    protected boolean isDragging = false;

    protected Screen screen;

    public AbstractTooltipsWidget(int x, int y, int width, int height, Component message, Screen screen) {
        super(x, y, width, height, message);
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
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float v) {
        var lastRectangle = positioner.lastRectangle;
        Font font = Minecraft.getInstance().font;
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
        if (isPositionInit){

            int i = 5;
            //? 1.20.1 {
            /*this.isHovered = mouseX >= this.getX() - i - 2 && mouseY >= this.getY() - i && mouseX < this.getX() + this.width + i && mouseY < this.getY() + this.height + i;
            *///?} else {
            this.isHovered = guiGraphics.containsPointInScissor(mouseX, mouseY) && mouseX >= this.getX() - i - 2 && mouseY >= this.getY() - i && mouseX < this.getX() + this.width + i && mouseY < this.getY() + this.height + i;
            //?}


            guiGraphics.blit(PIN, getX() + width, getY() - 10, 5, 5, 0, 0, 32, 32, 32, 32);
            //put this here can fix a glitch, see postRenderTooltipsWidget method.
            Style styleAt = getStyleAt(mouseX, mouseY, font);
            if (styleAt != null && styleAt.getHoverEvent() != null){
                if (getSameTargetWidget(screen, styleAt.hashCode()).isEmpty()) {
                    guiGraphics.renderComponentHoverEffect(font, styleAt, mouseX, mouseY);
                }
            }
            guiGraphics.drawString(Minecraft.getInstance().font, "111", getX(), getY(), ChatFormatting.WHITE.getColor());
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (isHovered) {
            if (button == 2){
                onRightClick(mouseX, mouseY);
            } else if (button == 1) {
                onMiddleClick(mouseX, mouseY);
            } else if (button == 0) {
                onLeftClick(mouseX, mouseY);
            }
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected void onLeftClick(double mouseX, double mouseY) {
        Style styleAt = getStyleAt(mouseX, mouseY, Minecraft.getInstance().font);
        if (styleAt!= null && styleAt.getHoverEvent() != null && EnlightenUtil.isEnlighten(styleAt.getHoverEvent())){
            Component value = styleAt.getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT);
            if (value != null){
                Option<AbstractTooltipsWidget> sameTargetWidget = getSameTargetWidget(screen, styleAt.hashCode());
                if (sameTargetWidget.isEmpty()) {
                    TooltipsCore.clientBus.post(new DirectlyAddEnlightenToFixedEvent(ItemStack.EMPTY, List.of(value.copy()), styleAt.hashCode()));
                } else {
                    AbstractTooltipsWidget tooltipsWidget = sameTargetWidget.get();
                    MemorizedTooltipsPositioner positioner1 = tooltipsWidget.positioner;
                    positioner1.accurateLastPosition = new Vector2d(mouseX, mouseY);
                    positioner1.lastPosition = new Vector2i(((int) positioner1.accurateLastPosition.x), ((int) positioner1.accurateLastPosition.y));

                    tooltipsWidget.setPosition(positioner1.lastPosition.x, positioner1.lastPosition.y);
                }
            }
        }
    }

    protected void onMiddleClick(double mouseX, double mouseY) {

        ((ScreenDuck) this.screen).removeFromFixed(this);
    }

    protected void onRightClick(double mouseX, double mouseY) {
        ScreenDuck screen1 = (ScreenDuck) this.screen;
        if (this == screen1.tc$getCurrentFocusTooltips()) {
            screen1.addToFixed(this);
            screen1.tc$setCurrentFocusTooltips(null);
        } else if (screen1.getAllFixed().contains(this)) {
            screen1.removeFromFixed(this);
        }
    }

    public abstract List<ClientTooltipComponent> getClientTooltipComponent();

    //from PinnedTooltips
    public void updateSize(Font font) {
        var width = 0;
        var height = 0;
        this.linesPosition.clear();
        for (ClientTooltipComponent component : getClientTooltipComponent()) {
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


    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {

    }

    public static Option<AbstractTooltipsWidget> getSameTargetWidget(Screen screen, int hashcode){

        return Option.ofOptional(((ScreenDuck) screen).getAllFixed().stream()
                .filter(x -> (x instanceof EnlightenTooltipsWidget enlightenTooltipsWidget && enlightenTooltipsWidget.isSameTarget(hashcode)))
                .findFirst());
    }
}
