package me.clefal.tooltips_core.enlighten.resolver;

import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.enlighten.component.EnlightenResolution;
import me.clefal.tooltips_core.enlighten.component.EnlightenSpan;
import me.clefal.tooltips_core.enlighten.component.EnlightenStyle;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;

import java.util.List;

@UtilityClass
public class EnlightenComponentBuilder {

    public static MutableComponent build(
            EnlightenResolution resolution,
            Style baseStyle
    ) {
        MutableComponent result =
                Component.empty();

        List<EnlightenSpan> spans =
                resolution.spans();

        RunAccumulator accumulator =
                new RunAccumulator(result);

        int[] currentSpanIndex = {0};

        StringDecomposer.iterateFormatted(
                resolution.text(),
                baseStyle,
                (sourceIndex, parsedStyle, codePoint) -> {

                    /*
                     * 已经经过的 Span 直接跳过。
                     */
                    while (currentSpanIndex[0] < spans.size() && sourceIndex >= spans.get(currentSpanIndex[0]).end()) {
                        currentSpanIndex[0]++;
                    }

                    EnlightenSpan activeSpan = null;

                    if (currentSpanIndex[0] < spans.size()) {
                        EnlightenSpan candidate = spans.get(currentSpanIndex[0]);

                        if (candidate.contains(sourceIndex)) {
                            activeSpan = candidate;
                        }
                    }

                    Style finalStyle = parsedStyle;

                    if (activeSpan != null) {
                        finalStyle = EnlightenStyle.apply(parsedStyle, activeSpan.description());
                    }

                    accumulator.append(
                            codePoint,
                            finalStyle
                    );

                    return true;
                }
        );

        accumulator.flush();

        return result;
    }


    private static final class RunAccumulator {

        private final MutableComponent target;

        private final StringBuilder buffer =
                new StringBuilder();

        private Style style;

        private RunAccumulator(
                MutableComponent target
        ) {
            this.target = target;
        }

        public void append(
                int codePoint,
                Style newStyle
        ) {
            if (style != null && !style.equals(newStyle)) flush();


            style = newStyle;
            buffer.appendCodePoint(codePoint);
        }

        public void flush() {
            if (buffer.isEmpty()) {
                return;
            }

            target.append(Component.literal(buffer.toString()).withStyle(style));

            buffer.setLength(0);
        }
    }
}