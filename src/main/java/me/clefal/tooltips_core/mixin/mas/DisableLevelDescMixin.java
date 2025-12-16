//? 1.20.1 {
/*package me.clefal.tooltips_core.mixin.mas;

import com.google.common.collect.ImmutableList;
import com.robertx22.mine_and_slash.gui.texts.textblocks.RequirementBlock;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = RequirementBlock.class, remap = false)
public class DisableLevelDescMixin {

    @Redirect(method = "getAvailableComponents", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/ImmutableList$Builder;add(Ljava/lang/Object;)Lcom/google/common/collect/ImmutableList$Builder;", ordinal = 2))
    private ImmutableList.Builder<Object> tc$removeLevelDesc(ImmutableList.Builder instance, Object element) {
        return instance;
    }
}
*///?}