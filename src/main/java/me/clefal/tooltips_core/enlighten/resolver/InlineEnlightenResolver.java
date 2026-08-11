package me.clefal.tooltips_core.enlighten.resolver;

import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.TooltipsCore;
import me.clefal.tooltips_core.enlighten.component.EnlightenResolution;
import me.clefal.tooltips_core.enlighten.component.EnlightenSpan;
import me.clefal.tooltips_core.enlighten.syntax.EnlightenSyntax;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@UtilityClass
public class InlineEnlightenResolver {

    public static EnlightenResolution resolve(Component source) {
        String input = source.getString();

        Matcher matcher = EnlightenSyntax.INLINE_PATTERN.matcher(input);

        StringBuilder output = new StringBuilder();
        List<EnlightenSpan> spans = new ArrayList<>();

        int cursor = 0;

        while (matcher.find()) {

            // [x](y) 前面的普通文本
            output.append(
                    input,
                    cursor,
                    matcher.start()
            );

            String displayText = matcher.group(1);
            String reference = matcher.group(2);

            /*
             * 注意：
             * Span 的位置针对最终显示文本 output，
             * 而不是原始带 [x](y) 的文本。
             */
            int start = output.length();

            output.append(displayText);

            int end = output.length();

            Component description =
                    EnlightenSyntax.termComponent(reference);

            if (ComponentUtils.isTranslationResolvable(description)) {
                spans.add(
                        new EnlightenSpan(
                                start,
                                end,
                                description
                        )
                );
            } else {
                TooltipsCore.LOGGER.warn(
                        "found non-exist term: {}",
                        EnlightenSyntax.termKey(reference)
                );
            }

            cursor = matcher.end();
        }

        // 最后一段普通文本
        output.append(
                input,
                cursor,
                input.length()
        );

        return new EnlightenResolution(
                output.toString(),
                spans
        );
    }
}
