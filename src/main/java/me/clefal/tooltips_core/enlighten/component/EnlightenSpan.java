package me.clefal.tooltips_core.enlighten.component;

import net.minecraft.network.chat.Component;

import java.util.Objects;

public record EnlightenSpan(
        int start,
        int end,
        Component description
) {

    public EnlightenSpan {
        if (start < 0) {
            throw new IllegalArgumentException("start < 0");
        }

        if (end < start) {
            throw new IllegalArgumentException("end < start");
        }

        Objects.requireNonNull(description);
    }

    public boolean contains(int index) {
        return index >= start && index < end;
    }
}
