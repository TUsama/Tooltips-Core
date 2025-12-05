package me.clefal.tooltips_core.mixin.mas;

import com.robertx22.mine_and_slash.database.data.spells.components.Spell;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = Spell.class, remap = false)
public class SpellMixin {
    @ModifyArg(
            method = "GetTooltipString",
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z",
                    ordinal = 0))
    private <E> E tc$revealSpell(E e){
        return  ((E) EnlightenUtil.reveal(((Component) e)).get(0));
    }

}
