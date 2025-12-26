package me.clefal.tooltips_core.mixin;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.clefal.tooltips_core.enlighten.component.DashedLineEffect;
import me.clefal.tooltips_core.enlighten.utils.DashedLineDuck;
import me.clefal.tooltips_core.enlighten.utils.MixinMethod;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.Style;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(value = Font.StringRenderOutput.class
        //? >1.20.1
        ,remap = false
)
public abstract class StringRenderOutputMixin implements DashedLineDuck {

    @Shadow
    private float x;
    @Shadow
    private float y;

    @Shadow
    @Final
    private Matrix4f pose;

    @Shadow
    @Final
    private int packedLightCoords;

    @Shadow
    @Nullable
    private List<BakedGlyph.Effect> effects;
    private Map<Style, DashedLineEffect> map = new HashMap<>();

    @WrapOperation(method = "accept(ILnet/minecraft/network/chat/Style;I)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font$StringRenderOutput;addEffect(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph$Effect;)V",
            ordinal = 1
    ))
    private void tc$injectDashedLineEffect(Font.StringRenderOutput instance, BakedGlyph.Effect effect, Operation<Void> original, @Local(ordinal = 0) float f3, @Local(ordinal = 1) float f, @Local(ordinal = 2) float f1, @Local(ordinal = 3) float f2, @Local(ordinal = 4) float f6, @Local(ordinal = 5) float f7, @Local(argsOnly = true) Style style) {
        if (MixinMethod.injectDashedLine(instance, effect, original, f3, f, f1, f2, f6, f7, style, x, y)) {
            //also init this list otherwise the mixin in finish() won't work
            //if this return false, then we don't need to handle the init by ourselves.
            //I can't invoke BakedGlyph bakedglyph = Font.this.getFontSet(Style.DEFAULT_FONT).whiteGlyph(); in tc$renderDashedLineEffect, so I have to use the variable capture to get the
            if (this.effects == null) {
                this.effects = new ArrayList<>();
            }
        }
    }

    @Definition(id = "getBuffer", method = "Lnet/minecraft/client/renderer/MultiBufferSource;getBuffer(Lnet/minecraft/client/renderer/RenderType;)Lcom/mojang/blaze3d/vertex/VertexConsumer;")
    @Expression("? = ?.getBuffer(?)")
    @Inject(method = "finish", at = @At(value = "MIXINEXTRAS:EXPRESSION", shift = At.Shift.AFTER))
    private void tc$renderDashedLineEffect(int backgroundColor, float x, CallbackInfoReturnable<Float> cir, @Local VertexConsumer vertexconsumer, @Local BakedGlyph bakedglyph) {
        MixinMethod.renderAllDashedLine(((Font.StringRenderOutput) ((Object) this)), bakedglyph, this.pose, vertexconsumer, this.packedLightCoords);
    }

    @Override
    public Map<Style, DashedLineEffect> getEffect() {
        return map;
    }

    @Override
    public void mergeEffect(Style style, DashedLineEffect effect) {
        DashedLineEffect ifPresent = this.map.get(style);
        if (ifPresent != null) {
            this.map.put(style, ifPresent.merge(effect));
        } else {
            this.map.put(style, effect);
        }
    }

    @Override
    public Iterable<DashedLineEffect> getAllEffect() {
        return this.map.values();
    }


}
