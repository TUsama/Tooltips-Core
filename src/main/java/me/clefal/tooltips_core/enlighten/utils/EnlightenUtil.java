package me.clefal.tooltips_core.enlighten.utils;

import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.enlighten.component.EnlightenResolution;
import me.clefal.tooltips_core.enlighten.resolver.EnlightenComponentBuilder;
import me.clefal.tooltips_core.enlighten.resolver.InlineEnlightenResolver;
import me.clefal.tooltips_core.enlighten.resolver.TranslationEnlightenResolver;
import me.clefal.tooltips_core.enlighten.syntax.EnlightenSyntax;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class EnlightenUtil {

    public static List<? extends FormattedText> reveal(Component component) {
        return reveal(List.of(component));
    }

    public static List<? extends FormattedText> reveal(
            List<? extends FormattedText> components
    ) {
        List<FormattedText> result = new ArrayList<>();

        for (FormattedText text : components) {
            if (!(text instanceof MutableComponent component)) {
                result.add(text);
                continue;
            }

            result.add(revealComponent(component));
        }

        return result;
    }

    private static Component revealComponent(MutableComponent component) {
        MutableComponent body = component.copy();
        body.getSiblings().clear();

        Component result = resolveBody(body);

        appendResolvedSiblings(component, result);

        return result;
    }

    private static Component resolveBody(
            MutableComponent component
    ) {
        EnlightenResolution resolution = null;

        if (component.getContents() instanceof TranslatableContents contents) {
            if (TranslationEnlightenResolver.canResolve(contents)) {
                resolution = TranslationEnlightenResolver.resolve(component, contents);
            } else if (EnlightenSyntax.containsInline(component.getString())) {
                resolution = InlineEnlightenResolver.resolve(component);
            }
        } else if (
                EnlightenSyntax.containsInline(component.getString())
        ) {
            resolution = InlineEnlightenResolver.resolve(component);
        }

        if (resolution == null) {
            return component;
        }

        return EnlightenComponentBuilder.build(resolution, component.getStyle());
    }

    private static void appendResolvedSiblings(
            MutableComponent original,
            Component result
    ) {
        for (FormattedText sibling : reveal(original.getSiblings())) {
            if (sibling instanceof Component component) {
                result.getSiblings().add(component);
            }
        }
    }
}
