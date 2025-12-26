package me.clefal.tooltips_core.enlighten.component;

import com.mojang.blaze3d.vertex.VertexConsumer;
import me.clefal.tooltips_core.mixin.BakedGlyphAccessor;
import me.clefal.tooltips_core.mixin.BakedGlyphEffectAccessor;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.joml.Matrix4f;

public class DashedLineEffect extends BakedGlyph.Effect {
    public DashedLineEffect(float x0, float y0, float x1, float y1, float depth, float r, float g, float b, float a) {
        super(x0, y0, x1, y1, depth, r, g, b, a);
    }

    public DashedLineEffect merge(DashedLineEffect effect){
        if (this.x1 < effect.x1){
            return new DashedLineEffect(x0, y0, effect.x1, y1, depth, r, g, b, a);
        } else {
            return this;
        }
    }

    public void render(BakedGlyph bakedglyph, Matrix4f matrix, VertexConsumer buffer, int packedLight){
        if (this instanceof BakedGlyphEffectAccessor accessor) {
            float x0 = accessor.getX0();
            BakedGlyphAccessor bakedglyph1 = (BakedGlyphAccessor) bakedglyph;
            float u0 = bakedglyph1.getU0();
            float u1 = bakedglyph1.getU1();
            float v0 = bakedglyph1.getV0();
            float v1 = bakedglyph1.getV1();
            float length = 4.0f;
            while (x0 + length <= accessor.getX1() + 6) {
                float x1 = Math.min(x0 + length, accessor.getX1());
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

                x0 += 6;
            }



        }
    }
}
