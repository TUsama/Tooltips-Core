package me.clefal.tooltips_core.enlighten.utils;

import me.clefal.tooltips_core.enlighten.base.AbstractTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.ComponentTooltipsWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public interface ScreenDuck {

    @Nullable
    AbstractTooltipsWidget tc$getCurrentFocusTooltips();

    void tc$setCurrentFocusTooltips(AbstractTooltipsWidget currentFocusTooltips);

    boolean tc$isTakenOver();

    AbstractTooltipsWidget addToFixed(AbstractTooltipsWidget widget);

    void removeFromFixed(AbstractTooltipsWidget widget);

    void addToRemoved(AbstractTooltipsWidget widget);

    Set<AbstractTooltipsWidget> getAllFixed();

    List<AbstractTooltipsWidget> getAll();

    <T extends GuiEventListener & Renderable & NarratableEntry> T addFirstRenderableWidget(T widget);

    <T extends GuiEventListener & NarratableEntry> T addWidgetToFirst(T listener);

    <T extends GuiEventListener & NarratableEntry> T raiseToFirstWidget(T listener);
}
