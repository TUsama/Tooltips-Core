package me.clefal.tooltips_core.enlighten.resolver;

import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.enlighten.component.EnlightenResolution;
import me.clefal.tooltips_core.enlighten.component.EnlightenSpan;
import me.clefal.tooltips_core.enlighten.syntax.EnlightenSyntax;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@UtilityClass
public class TranslationEnlightenResolver {

    public static boolean canResolve(
            TranslatableContents contents
    ) {
        return I18n.exists(
                EnlightenSyntax.enlightenKey(
                        contents.getKey()
                )
        );
    }

    public static EnlightenResolution resolve(
            MutableComponent target,
            TranslatableContents contents
    ) {
        String text = target.getString();

        Component definition = Component.translatable(EnlightenSyntax.enlightenKey(contents.getKey()));

        Map<String, Component> terms =
                EnlightenSyntax.parseDefinitions(definition.getString());

        return new EnlightenResolution(
                text,
                findSpans(text, terms)
        );
    }

    private static List<EnlightenSpan> findSpans(
            String text,
            Map<String, Component> terms
    ) {
        /*
         * "*" 表示整个 Component。
         */
        Component whole = terms.get("*");

        if (whole != null) {
            return List.of(new EnlightenSpan(0, text.length(), whole));
        }

        List<String> termNames = terms.keySet()
                .stream()
                /*
                 * 很重要：
                 *
                 * 如果同时存在：
                 * 暴击
                 * 暴击伤害
                 *
                 * 优先匹配更长的。
                 */
                .sorted(
                        Comparator.comparingInt(String::length)
                                .reversed()
                )
                .toList();

        List<EnlightenSpan> result =
                new ArrayList<>();

        int cursor = 0;

        while (cursor < text.length()) {

            String matched = null;

            for (String term : termNames) {
                if (text.startsWith(term, cursor)) {
                    matched = term;
                    break;
                }
            }

            if (matched == null) {
                cursor++;
                continue;
            }

            result.add(
                    new EnlightenSpan(
                            cursor,
                            cursor + matched.length(),
                            terms.get(matched)
                    )
            );

            cursor += matched.length();
        }

        return result;
    }
}