package me.clefal.tooltips_core.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import me.clefal.tooltips_core.enlighten.utils.MixinMethod;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = BakedGlyph.class
        //? >1.20.1
        ,remap = false
)
public class BakedGlyphMixin {

    @Shadow
    @Final
    private float u0;

    @Shadow
    @Final
    private float v0;

    @Shadow
    @Final
    private float u1;

    @Shadow
    @Final
    private float v1;

    @Inject(method = "renderEffect", at = @At(value = "HEAD"), cancellable = true)
    private void tc$tickRemoveCurrent(BakedGlyph.Effect effect, Matrix4f matrix, VertexConsumer buffer, int packedLight, CallbackInfo ci) {
        if (MixinMethod.tryDrawDashedLine(u0, v0, u1, v1, effect, matrix, buffer, packedLight)) {
            ci.cancel();
        }
    }

}
