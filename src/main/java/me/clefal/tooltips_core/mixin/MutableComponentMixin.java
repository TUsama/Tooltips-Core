package me.clefal.tooltips_core.mixin;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.LinkedHashMap;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Optional;

@Mixin(value = MutableComponent.class)
public abstract class MutableComponentMixin implements Component {

    @Unique
    public Optional<Map<String, Component>> tooltips_Core$grabEligibles(StyledContentConsumer<String> acceptor, Style p_style) {
        ArrayList<Tuple2<String, Component>> tuple2s = new ArrayList<>();
        Style style = this.getStyle().applyTo(p_style);
        Optional<String> optional = this.getContents().visit(acceptor, style);
        if (optional.isPresent()) {
            tuple2s.add(Tuple.of(optional.get(), this));
        } else {
            for (Component component : this.getSiblings()) {
                Optional<String> optional1 = component.visit(acceptor, style);
                if (optional1.isPresent()) {
                    tuple2s.add(Tuple.of(optional1.get(), this));
                }
            }
        }
        return Optional.of(LinkedHashMap.ofEntries(tuple2s));
    }
}
