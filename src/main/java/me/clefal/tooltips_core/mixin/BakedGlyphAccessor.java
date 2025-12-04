package me.clefal.tooltips_core.mixin;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BakedGlyph.Effect.class
        //? >1.20.1
        ,remap = false
)
public interface BakedGlyphAccessor {
    @Accessor("x0")
    float getX0();

    @Accessor("y0")
    float getY0();

    @Accessor("x1")
    float getX1();

    @Accessor("y1")
    float getY1();

    @Accessor("depth")
    float getDepth();

    @Accessor("r")
    float getR();

    @Accessor("g")
    float getG();

    @Accessor("b")
    float getB();

    @Accessor("a")
    float getA();
}
