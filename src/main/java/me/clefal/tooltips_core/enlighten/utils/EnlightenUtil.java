package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.TooltipsCore;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.contents.TranslatableContents;

import java.util.ArrayList;
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
        List<FormattedText> formattedTexts = new ArrayList<>();
        for (FormattedText component : components) {
            if (component instanceof MutableComponent comp && comp.getContents() instanceof TranslatableContents contents && I18n.exists(getEnlighten.apply(contents.getKey()))) {
                formattedTexts.add(resolveEnlightenComponent(comp, Component.translatable(getEnlighten.apply(contents.getKey()))));
            } else {
                formattedTexts.add(component);
            }
        }

        return formattedTexts;
    }

    private static Component resolveEnlightenComponent(MutableComponent target, Component enlighten) {
        Map<String, Component> enlightenMap = resolveEnlighten(enlighten);
        List<Component> resultComponent = new ArrayList<>();
        handleWholeComponent(target, resultComponent, enlightenMap);
        MutableComponent empty = Component.empty();
        for (Component component : resultComponent) {
            empty.append(component);
        }
        return empty;
    }

    private static void handleWholeComponent(MutableComponent target, List<Component> resultComponent, Map<String, Component> enlightenMap) {
        if (target.getSiblings().isEmpty()) {
            String string = target.getString();
            resultComponent.addAll(handleSingleSegment(target.getStyle(), enlightenMap, string));
        } else {
            target.getContents().visit(content -> {
                resultComponent.addAll(handleSingleSegment(target.getStyle(), enlightenMap, content));
                return Optional.empty();
            });

            for (Component sibling : target.getSiblings()) {
                if (sibling instanceof MutableComponent mutableComponent) {
                    handleWholeComponent(mutableComponent, resultComponent, enlightenMap);
                } else {
                    resultComponent.add(sibling);
                }
            }
        }
    }

    private static List<Component> handleSingleSegment(Style old, Map<String, Component> enlightenMap, String targetString) {
        List<Component> resultComponent = new ArrayList<>();
        Map<String, Component> filter = enlightenMap.filter(x -> targetString.contains(x._1));
        List<String> strings = splitBySubstrings(targetString, filter.keySet().toJavaList());
        for (String s : strings) {
            if (filter.keySet().contains(s)) {
                resultComponent.add(Component.literal(s).withStyle(old.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(filter.get(s).get())))));
            } else {
                resultComponent.add(Component.literal(s).withStyle(old));
            }
        }
        return resultComponent;
    }

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
