package me.clefal.tooltips_core.mixin;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
@Mixin(value = Screen.class
        //? >1.20.1
        ,remap = false
)
public interface ScreenInvoker {
    @Invoker <T extends GuiEventListener & Renderable & NarratableEntry> T callAddRenderableWidget(T widget);

}
