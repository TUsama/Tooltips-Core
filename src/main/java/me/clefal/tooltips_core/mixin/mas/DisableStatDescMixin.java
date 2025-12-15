package me.clefal.tooltips_core.mixin.mas;

import com.robertx22.mine_and_slash.database.data.stats.Stat;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = Stat.class, remap = false)
public class DisableStatDescMixin {

    @Inject(method = "getCutDescTooltip", at = @At("HEAD"), cancellable = true)
    private void tc$removeStatDesc(CallbackInfoReturnable<List<MutableComponent>> cir) {
        cir.setReturnValue(List.of());
    }


}
