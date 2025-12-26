package me.clefal.tooltips_core.enlighten.utils;

import me.clefal.tooltips_core.enlighten.component.DashedLineEffect;
import net.minecraft.network.chat.Style;

import java.util.Map;

public interface DashedLineDuck {

    Map<Style, DashedLineEffect> getEffect();
    void mergeEffect(Style style, DashedLineEffect effect);
    Iterable<DashedLineEffect> getAllEffect();
}
