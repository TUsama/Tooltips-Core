package me.clefal.tooltips_core.enlighten.component;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public record EnlightenResolution(
        String text,
        List<EnlightenSpan> spans
) {

    public EnlightenResolution {
        Objects.requireNonNull(text);
        Objects.requireNonNull(spans);

        spans = spans.stream()
                .sorted(Comparator.comparingInt(EnlightenSpan::start))
                .toList();
    }

    public static EnlightenResolution plain(String text) {
        return new EnlightenResolution(text, List.of());
    }
}
