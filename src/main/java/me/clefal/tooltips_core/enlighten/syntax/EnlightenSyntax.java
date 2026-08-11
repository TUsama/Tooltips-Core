package me.clefal.tooltips_core.enlighten.syntax;

import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.TooltipsCore;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class EnlightenSyntax {

    public static final Pattern INLINE_PATTERN =
            Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)");

    private static final String ENLIGHTEN_SUFFIX = ".enlighten";
    private static final String TERM_PREFIX = "enlighten.term.";

    public static String enlightenKey(String translationKey) {
        return translationKey + ENLIGHTEN_SUFFIX;
    }

    public static String termKey(String reference) {
        return TERM_PREFIX + reference;
    }

    public static Component termComponent(String reference) {
        return Component.translatable(termKey(reference));
    }

    public static boolean containsInline(String text) {
        return INLINE_PATTERN.matcher(text).find();
    }

    public static List<InlineTerm> findInlineTerms(String text) {
        List<InlineTerm> result = new ArrayList<>();

        Matcher matcher = INLINE_PATTERN.matcher(text);

        while (matcher.find()) {
            String display = matcher.group(1);
            String reference = matcher.group(2);

            Component term = termComponent(reference);

            if (!ComponentUtils.isTranslationResolvable(term)) {
                TooltipsCore.LOGGER.warn(
                        "found non-exist term: {}",
                        termKey(reference)
                );
                continue;
            }

            result.add(new InlineTerm(
                    display,
                    reference,
                    matcher.start(),
                    matcher.end(),
                    term
            ));
        }

        return result;
    }

    /**
     * 解析 .enlighten 翻译项的 value。
     *
     * 例如：
     *
     * [暴击](critical),[暴击伤害](critical_damage)
     *
     * 转换为：
     *
     * "暴击"
     *     -> Component.translatable("enlighten.term.critical")
     *
     * "暴击伤害"
     *     -> Component.translatable("enlighten.term.critical_damage")
     */
    public static Map<String, Component> parseDefinitions(String source) {
        Map<String, Component> result = new LinkedHashMap<>();

        if (source == null || source.isBlank()) {
            return result;
        }

        String[] definitions = source.split(",");

        for (String definition : definitions) {
            String trimmed = definition.trim();

            if (trimmed.isEmpty()) {
                continue;
            }

            Matcher matcher = INLINE_PATTERN.matcher(trimmed);

            /*
             * 一个 definition 应该完整符合：
             *
             * [display](reference)
             *
             * 所以这里用 matches()，而不是 find()。
             */
            if (!matcher.matches()) {
                TooltipsCore.LOGGER.warn(
                        "invalid enlighten definition: {}",
                        trimmed
                );
                continue;
            }

            String displayText = matcher.group(1);
            String reference = matcher.group(2);

            Component term = termComponent(reference);

            if (!ComponentUtils.isTranslationResolvable(term)) {
                TooltipsCore.LOGGER.warn(
                        "found non-exist term: {}",
                        termKey(reference)
                );
                continue;
            }

            Component previous = result.put(
                    displayText,
                    term
            );

            if (previous != null) {
                TooltipsCore.LOGGER.warn(
                        "duplicate enlighten display text: {}",
                        displayText
                );
            }
        }

        return result;
    }

    public record InlineTerm(
            String displayText,
            String reference,
            int start,
            int end,
            Component termComponent
    ) {}
}
