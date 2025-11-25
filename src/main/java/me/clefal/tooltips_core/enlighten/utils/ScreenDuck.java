package me.clefal.tooltips_core.enlighten.utils;

import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;

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
}
