package me.clefal.tooltips_core.mixin;

import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = BakedGlyph.class
        //? >1.20.1
        ,remap = false
)
public interface BakedGlyphAccessor {

    @Accessor("u0")
    float getU0();

    @Accessor("v0")
    float getV0();

    @Accessor("u1")
    float getU1();

    @Accessor("v1")
    float getV1();

}
