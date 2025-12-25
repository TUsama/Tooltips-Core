package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.LinkedHashMap;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.enlighten.component.DashedLineEffect;
import me.clefal.tooltips_core.enlighten.event.SaveCurrentComponentsEvent;
import me.clefal.tooltips_core.mixin.BakedGlyphAccessor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.network.chat.*;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

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
        if (present) {
            List<String> strings = optional.get();
            for (String string : strings) {
                tuple2s.add(Tuple.of(string, enlightenMap.get(string).get()));
            }
        }
        return Optional.of(LinkedHashMap.ofEntries(tuple2s));
    }

    public static boolean tryDrawDashedLine(float u0, float v0, float u1, float v1, BakedGlyph.Effect effect, Matrix4f matrix, VertexConsumer buffer, int packedLight) {
        if (effect instanceof DashedLineEffect && effect instanceof BakedGlyphAccessor accessor) {
            float x0 = accessor.getX0() + 1;
            float x1 = x0 + 4;

            //? 1.20.1 {
            /*buffer.vertex(matrix, x0, accessor.getY0(), accessor.getDepth())
                    .color(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .uv(u0, v0)
                    .uv2(packedLight).endVertex();

            buffer.vertex(matrix, x1, accessor.getY0(), accessor.getDepth())
                    .color(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .uv(u0, v1)
                    .uv2(packedLight).endVertex();

            buffer.vertex(matrix, x1, accessor.getY1(), accessor.getDepth())
                    .color(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .uv(u1, v1)
                    .uv2(packedLight).endVertex();

            buffer.vertex(matrix, x0, accessor.getY1(), accessor.getDepth())
                    .color(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .uv(u1, v0)
                    .uv2(packedLight).endVertex();
            *///?} else {
            buffer.addVertex(matrix, x0, accessor.getY0(), accessor.getDepth())
                    .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .setUv(u0, v0)
                    .setLight(packedLight);

            buffer.addVertex(matrix, x1, accessor.getY0(), accessor.getDepth())
                    .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .setUv(u0, v1)
                    .setLight(packedLight);

            buffer.addVertex(matrix, x1, accessor.getY1(), accessor.getDepth())
                    .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .setUv(u1, v1)
                    .setLight(packedLight);

            buffer.addVertex(matrix, x0, accessor.getY1(), accessor.getDepth())
                    .setColor(accessor.getR(), accessor.getG(), accessor.getB(), accessor.getA())
                    .setUv(u1, v0)
                    .setLight(packedLight);
            //?}


            return true;
        }

        return false;
    }

    public static void revealOnHover(GuiGraphics instance, Font font, int mouseX, int mouseY, Operation<Void> original, Component component, ItemStack stack, int width) {
        var tuple2 = EnlightenUtil.trimEnlighten(component);

        Component component1 = ((Component) EnlightenUtil.reveal(tuple2._2).get(0));

        //if (EnlightenUtil.isNested(component1)) component1 = EnlightenUtil.revealNestedEnlighten(component1.copy());

        if (!SaveCurrentComponentsEvent.tryPost(List.of(component1), stack)) {
            List<FormattedCharSequence> split = new ArrayList<>(font.split(component1, Math.max(width / 2, 200)));
            split.add(FormattedCharSequence.EMPTY);
            original.call(instance, font, split, mouseX, mouseY);
        }
    }
}
