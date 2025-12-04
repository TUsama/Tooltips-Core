package me.clefal.tooltips_core.enlighten.handlers;


import lombok.Getter;
import me.clefal.tooltips_core.enlighten.base.AbstractTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.ComponentTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.FormattedCharSequenceTooltipsWidget;
import me.clefal.tooltips_core.enlighten.base.IComponentProvider;
import me.clefal.tooltips_core.enlighten.utils.ScreenDuck;
import me.clefal.tooltips_core.mixin.ScreenInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;

//? 1.20.1 {
/*import net.minecraftforge.client.event.ScreenEvent;
*///?} else {
import net.neoforged.neoforge.client.event.ScreenEvent;
//?}

import javax.annotation.Nullable;
import java.util.List;


public class TooltipsRecorder {
    @Nullable
    @Getter
    private static AbstractTooltipsRecord<?> pendingTooltips;

    public static boolean ifTooltipsRecordPending() {
        return pendingTooltips != null;
    }

    public static void setPendingTooltips(@Nullable AbstractTooltipsRecord<?> pendingTooltips) {
        TooltipsRecorder.pendingTooltips = pendingTooltips;
    }

    public static boolean isTakenOverByTC() {
        return pendingTooltips != null || (Minecraft.getInstance().screen != null && ((ScreenDuck) Minecraft.getInstance().screen).tc$isTakenOver());
    }


    public abstract static class AbstractTooltipsRecord<T> implements IComponentProvider<T> {

        public ItemStack itemStack;

        public AbstractTooltipsRecord(ItemStack itemStack) {
            this.itemStack = itemStack;
        }

        public abstract <R extends AbstractTooltipsWidget> R setUpWidget(ScreenEvent.Render.Pre event);
    }

    public static class ComponentTooltipsRecord extends AbstractTooltipsRecord<List<? extends FormattedText>> {

        public List<? extends FormattedText> components;

        public ComponentTooltipsRecord(List<? extends FormattedText> components, ItemStack itemStack) {
            super(itemStack);
            this.components = components;
        }

        @Override
        public List<? extends FormattedText> provide() {
            return components;
        }

        @Override
        public ComponentTooltipsWidget setUpWidget(ScreenEvent.Render.Pre event) {
            Screen screen = event.getScreen();
            ComponentTooltipsWidget componentTooltipsWidget = new ComponentTooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, provide(), itemStack, screen);
            ((ScreenInvoker) screen).callAddRenderableWidget(componentTooltipsWidget);
            ((ScreenDuck) screen).tc$setCurrentFocusTooltips(componentTooltipsWidget);

            return componentTooltipsWidget;
        }
    }

    public static class EnlightenTooltipsRecord extends ComponentTooltipsRecord {

        public int hashcode;

        public EnlightenTooltipsRecord(List<? extends FormattedText> components, ItemStack itemStack, int hashcode) {
            super(components, itemStack);
            this.hashcode = hashcode;
        }

        @Override
        public ComponentTooltipsWidget setUpWidget(ScreenEvent.Render.Pre event) {
            Screen screen = event.getScreen();
            ComponentTooltipsWidget fixed;
            if (pendingTooltips instanceof TooltipsRecorder.EnlightenTooltipsRecord enlightenTooltipsRecord) {

                fixed = new EnlightenTooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, enlightenTooltipsRecord.provide(), enlightenTooltipsRecord.itemStack, screen, enlightenTooltipsRecord.hashcode);

            } else {

                fixed = new ComponentTooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, provide(), itemStack, screen);

            }

            ((ScreenDuck) screen).addFirstRenderableWidget(fixed);
            ((ScreenDuck) screen).addToFixed(fixed);
            return fixed;
        }
    }

    public static class FormattedCharSequenceTooltipsRecord extends AbstractTooltipsRecord<List<FormattedCharSequence>> {
        public List<FormattedCharSequence> components;

        public FormattedCharSequenceTooltipsRecord(ItemStack itemStack, List<FormattedCharSequence> components) {
            super(itemStack);
            this.components = components;
        }

        @Override
        public List<FormattedCharSequence> provide() {
            return List.of();
        }

        @Override
        public FormattedCharSequenceTooltipsWidget setUpWidget(ScreenEvent.Render.Pre event) {
            Screen screen = event.getScreen();
            FormattedCharSequenceTooltipsWidget formattedCharSequenceTooltipsWidget = new FormattedCharSequenceTooltipsWidget(event.getMouseX(), event.getMouseY(), 5, 5, event.getScreen(), provide());

            ((ScreenInvoker) screen).callAddRenderableWidget(formattedCharSequenceTooltipsWidget);
            ((ScreenDuck) screen).tc$setCurrentFocusTooltips(formattedCharSequenceTooltipsWidget);

            setPendingTooltips(null);
            return formattedCharSequenceTooltipsWidget;
        }
    }
}
