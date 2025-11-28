package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.TooltipsCore;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class EnlightenUtil {
    private static final Function<String, String> getEnlighten = string -> string + ".enlighten";
    private static final Function<String, String> termFinder = s -> "enlighten.term." + s;
    private Pattern group = Pattern.compile("\\[.+\\]\\(.+\\)");

    public static Component reveal(Component component) {
        return ((Component) reveal(List.of(component)).get(0));
    }

    public static List<? extends FormattedText> reveal(List<? extends FormattedText> components) {
        System.out.println("before loop: " + components.size());
        for (FormattedText component : components) {
            if (component instanceof MutableComponent comp && comp.getContents() instanceof TranslatableContents contents && I18n.exists(getEnlighten.apply(contents.getKey()))) {
                System.out.println("found enlighten, prepare to reveal");
                resolveEnlightenComponent(comp, Component.translatable(getEnlighten.apply(contents.getKey())));
            }
        }
        System.out.println("after loop: " + components.size());
        return components;
    }

    private static void resolveEnlightenComponent(MutableComponent target, Component enlighten) {
        Map<String, Component> enlightenMap = resolveEnlighten(enlighten);
        handleWholeComponent(target, enlightenMap);
    }

    private static void handleWholeComponent(MutableComponent target, Map<String, Component> enlightenMap) {
        ((MutableComponentDuck) target).tooltips_Core$grabEligibles((style, content) -> {
            if (enlightenMap.keySet().contains(content)){
                return Optional.of(content);
            }
            return Optional.empty();
        }, Style.EMPTY)
                .ifPresent(x -> {
                    for (Tuple2<String, Component> stringComponentTuple2 : x) {
                        if (stringComponentTuple2._2 instanceof MutableComponent mutableComponent){
                            mutableComponent.withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(enlightenMap.get(stringComponentTuple2._1).get()))));
                        }
                    }
                });
    }
/*
    private static Map<String, Component> checkSingleSegment(Map<String, Component> enlightenMap, String targetString) {
        Map<String, Component> filter = enlightenMap.filter(x -> targetString.contains(x._1));
        List<String> strings = splitBySubstrings(targetString, filter.keySet().toJavaList());
        boolean flag = false;
        for (String s : strings) {
            if (strings.contains(s)) {
                flag = true;
                break;
            }
        }
        return flag;
    }
*/
    private static Map<String, Component> resolveEnlighten(Component component) {
        String string = component.getString();
        String[] split = string.split(",");

        return Stream.of(split)
                .filter(s -> group.asMatchPredicate().test(s))
                .map(s -> {
                    Matcher matcher = group.matcher(s);
                    matcher.find();
                    return Tuple.of(
                            matcher.group(0),
                            Component.translatable(termFinder.apply(matcher.group(1)))
                    );
                })
                .filter(tuple2 -> {
                    boolean exists = ComponentUtils.isTranslationResolvable(tuple2._2());
                    if (!exists) {
                        TooltipsCore.LOGGER.warn("found non-exist term: {}", tuple2._2().getString());
                    }
                    return exists;
                })
                .transform(x -> x.toMap(Function.identity()));
    }

    public static List<String> splitBySubstrings(String input, List<String> patterns) {
        List<String> result = new ArrayList<>();

        int index = 0;

        while (index < input.length()) {
            int matchIndex = -1;
            String matched = null;

            for (String p : patterns) {
                if (p == null || p.isEmpty()) continue;

                int i = input.indexOf(p, index);
                if (i != -1 && (matchIndex == -1 || i < matchIndex)) {
                    matchIndex = i;
                    matched = p;
                }
            }


            if (matchIndex == -1) {
                result.add(input.substring(index));
                break;
            }


            if (matchIndex > index) {
                result.add(input.substring(index, matchIndex));
            }

            result.add(matched);

            index = matchIndex + matched.length();
        }

        return result;
    }

    public static boolean isEnlighten(HoverEvent event) {
        return event.getAction().equals(HoverEvent.Action.SHOW_TEXT) && event.getValue(HoverEvent.Action.SHOW_TEXT).getString().contains("enlighten: ");
    }

}
