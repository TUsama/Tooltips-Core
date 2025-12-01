package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.LinkedHashMap;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@UtilityClass
public class MixinMethod {

    public static @NotNull Optional<Map<String, Component>> grab(ComponentContents contents, MutableComponent compo, FormattedText.StyledContentConsumer<List<String>> acceptor, Style p_style, Map<String, Component> enlightenMap) {
        ArrayList<Tuple2<String, Component>> tuple2s = new ArrayList<>();
        Style style = compo.getStyle().applyTo(p_style);
        Optional<List<String>> optional = contents.visit(acceptor, style);
        boolean present = optional.isPresent();
        if (present){
            List<String> strings = optional.get();
            for (String string : strings) {
                tuple2s.add(Tuple.of(string, enlightenMap.get(string).get()));
            }
        }
        return Optional.of(LinkedHashMap.ofEntries(tuple2s));
    }
}
