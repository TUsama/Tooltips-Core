package me.clefal.tooltips_core.mixin;

import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import me.clefal.tooltips_core.enlighten.utils.MixinMethod;
import me.clefal.tooltips_core.enlighten.utils.MutableComponentDuck;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.Optional;

@Mixin(value = MutableComponent.class
//? >1.20.1
        ,remap = false
)
public abstract class MutableComponentMixin implements Component, MutableComponentDuck {

    @Unique
    public Optional<Map<String, Component>> tooltips_Core$grabEligibles(StyledContentConsumer<List<String>> acceptor, Style p_style, Map<String, Component> enlightenMap) {
        return MixinMethod.grab(this.getContents(),((MutableComponent)((Object) this)), acceptor, p_style, enlightenMap);
    }

}
