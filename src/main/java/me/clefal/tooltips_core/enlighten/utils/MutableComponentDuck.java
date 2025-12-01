package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;

import java.util.List;
import java.util.Optional;

public interface MutableComponentDuck {
    Optional<Map<String, Component>> tooltips_Core$grabEligibles(FormattedText.StyledContentConsumer<List<String>> acceptor, Style p_style, Map<String, Component> enlightenMap);

}
