package me.clefal.tooltips_core.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.clefal.tooltips_core.enlighten.component.DashedLineEffect;
import me.clefal.tooltips_core.enlighten.utils.EnlightenUtil;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = Font.StringRenderOutput.class)
public abstract class FontMixin {

    @Shadow
    protected abstract void addEffect(BakedGlyph.Effect effect);

    @Shadow
    private float x;

    @Shadow
    private float y;

    @Inject(method = "accept(ILnet/minecraft/network/chat/Style;I)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font$StringRenderOutput;addEffect(Lnet/minecraft/client/gui/font/glyphs/BakedGlyph$Effect;)V",
            ordinal = 1
    )

    )
    private void tc$tickRemoveCurrent(int positionInCurrentSequence, Style style, int codePoint, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) float f3, @Local(ordinal = 1) float f, @Local(ordinal = 2) float f1, @Local(ordinal = 3)float f2, @Local(ordinal = 4) float f6, @Local(ordinal = 5) float f7) {
        HoverEvent hoverEvent = style.getHoverEvent();
        if (hoverEvent != null && EnlightenUtil.isEnlighten(hoverEvent)){
            this.addEffect(new DashedLineEffect(this.x + f7 - 1.0F, this.y + f7 + 9.0F, this.x + f7 + f6, this.y + f7 + 9.0F - 1.0F, 0.01F, f, f1, f2, f3));
        }
    }
}
