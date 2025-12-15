package me.clefal.tooltips_core.mixin.mas;

import com.robertx22.mine_and_slash.database.data.stats.tooltips.NormalStatTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = NormalStatTooltip.class, remap = false)
public class DisableFormulaMixin {
    @Redirect(method = "getTooltipList", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private boolean tc$removeFormula(List instance, Object e) {
        return false;
    }
}
