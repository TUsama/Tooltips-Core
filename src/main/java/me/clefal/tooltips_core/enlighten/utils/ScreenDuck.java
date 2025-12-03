package me.clefal.tooltips_core.enlighten.utils;

import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import javax.annotation.Nullable;
import java.util.Set;

public interface ScreenDuck {

    @Nullable
    TooltipsWidget tc$getCurrentFocusTooltips();

    void tc$setCurrentFocusTooltips(TooltipsWidget currentFocusTooltips);

    boolean tc$isTakenOver();

    TooltipsWidget addToFixed(TooltipsWidget widget);

    void removeFromFixed(TooltipsWidget widget);

    void addToRemoved(TooltipsWidget widget);

    Set<TooltipsWidget> getAllFixed();

    <T extends GuiEventListener & Renderable & NarratableEntry> T addFirstRenderableWidget(T widget);
}
