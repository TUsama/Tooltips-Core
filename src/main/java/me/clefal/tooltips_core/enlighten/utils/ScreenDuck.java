package me.clefal.tooltips_core.enlighten.utils;

import me.clefal.tooltips_core.enlighten.base.AbstractTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.ComponentTooltipsWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;

import javax.annotation.Nullable;
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

    <T extends GuiEventListener & Renderable & NarratableEntry> T addFirstRenderableWidget(T widget);
}
