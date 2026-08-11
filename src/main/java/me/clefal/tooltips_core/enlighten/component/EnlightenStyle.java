package me.clefal.tooltips_core.enlighten.component;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import lombok.experimental.UtilityClass;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

//? if >1.20.1 {
import net.minecraft.network.chat.contents.PlainTextContents;
//?} else {
/*import net.minecraft.network.chat.contents.LiteralContents;
*///?}

@UtilityClass
public class EnlightenStyle {

    private static final String MARKER = "enlighten: ";

    public static Style apply(
            Style base,
            Component description
    ) {
        return base
                .withHoverEvent(createHover(description))
                .withUnderlined(true);
    }

    public static HoverEvent createHover(Component description) {
        return new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                Component.literal(MARKER)
                        .append(description)
        );
    }

    public static boolean isEnlighten(HoverEvent event) {
        return event.getAction() == HoverEvent.Action.SHOW_TEXT
                && event.getValue(HoverEvent.Action.SHOW_TEXT)
                .getString()
                .contains(MARKER);
    }


    public static Tuple2<Boolean, Component> trimEnlighten(Component text) {
        MutableComponent copy = text.copy();
        //? 1.20.1 {
        /*if (copy.getContents() instanceof LiteralContents contents && contents.text().equals("enlighten: "))
         *///?} else {
        if (copy.getContents() instanceof PlainTextContents.LiteralContents contents && contents.text().equals("enlighten: "))
        //?}
            {
            MutableComponent empty = Component.empty();
            empty.getSiblings().addAll(copy.getSiblings());
            empty.withStyle(copy.getStyle());
            copy = empty;
            return Tuple.of(true, copy);
            }
        return Tuple.of(false, text);
    }
}
