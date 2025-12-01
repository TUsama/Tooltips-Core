package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.LinkedHashMap;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.enlighten.base.TooltipsWidget;
import me.clefal.tooltips_core.enlighten.component.DashedLineEffect;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import me.clefal.tooltips_core.enlighten.handlers.TooltipsRecorder;
import me.clefal.tooltips_core.mixin.BakedGlyphAccessor;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    public static FormattedText revealComponent(FormattedText text) {
        if (text instanceof Component component){
            MutableComponent copy = component.copy();
            if (copy.getContents() instanceof PlainTextContents.LiteralContents literalContents && literalContents.text().equals("enlighten: ")){
                MutableComponent empty = Component.empty();
                empty.getSiblings().addAll(copy.getSiblings());
                empty.withStyle(copy.getStyle());
                copy = empty;
            }
            return EnlightenUtil.reveal(copy).get(0);
        }
        return text;
    }

    public static void tryDrawDashedLine(float u0, float v0, float u1, float v1, BakedGlyph.Effect effect, Matrix4f matrix, VertexConsumer buffer, int packedLight, CallbackInfo ci) {
        if (effect instanceof DashedLineEffect && effect instanceof BakedGlyphAccessor accessor) {
            float singleLineLength = 1.5f;
            float interval = 0.8f;
            float currentX = 0;
            boolean shouldDraw = true;
            while (currentX < accessor.getX1()) {
                if (shouldDraw) {
                    buffer.addVertex(matrix, currentX, accessor.getY0(), accessor.getDepth())
                            .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                            .setUv(u0, v0)
                            .setLight(packedLight);

                    buffer.addVertex(matrix, currentX + singleLineLength, accessor.getY0(), accessor.getDepth())
                            .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                            .setUv(u0, v1)
                            .setLight(packedLight);

                    buffer.addVertex(matrix, currentX + singleLineLength, accessor.getY1(), accessor.getDepth())
                            .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                            .setUv(u1, v1)
                            .setLight(packedLight);

                    buffer.addVertex(matrix, currentX, accessor.getY1(), accessor.getDepth())
                            .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                            .setUv(u1, v0)
                            .setLight(packedLight);

                    currentX += singleLineLength;
                    shouldDraw = false;
                } else {

                    currentX += interval;
                    shouldDraw = true;
                }
            }


            ci.cancel();
        }
    }
}
