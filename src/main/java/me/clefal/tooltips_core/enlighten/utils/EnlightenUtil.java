package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Set;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.TooltipsCore;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.*;
        //? 1.20.1 {
/*import net.minecraft.network.chat.contents.LiteralContents;
        *///?} else {
import net.minecraft.network.chat.contents.PlainTextContents;
        //?}


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
    private Pattern group = Pattern.compile("\\[(.+)\\]\\((.+)\\)");

    public static List<? extends FormattedText> reveal(Component component) {
        return reveal(List.of(component));
    }

    public static List<? extends FormattedText> reveal(List<? extends FormattedText> components) {
        List<FormattedText> formattedTexts = new ArrayList<>();
        for (FormattedText component : components) {
            if (component instanceof MutableComponent comp) {
                Component newComponent;
                if (comp.getContents() instanceof TranslatableContents contents && I18n.exists(getEnlighten.apply(contents.getKey()))) {
                    TooltipsCore.LOGGER.warn("found enlighten for {}, prepare to reveal", contents.getKey());
                    MutableComponent mutableComponent = resolveEnlightenComponent(comp, Component.translatable(getEnlighten.apply(contents.getKey())));
                    newComponent = mutableComponent;
                } else {
                    MutableComponent copy = comp.copy();
                    copy.getSiblings().clear();
                    newComponent = copy;
                }
                if (!comp.getSiblings().isEmpty()) {
                    ArrayList<Component> oldSiblings = new ArrayList<>(comp.getSiblings());
                    //if the comp hasn't been modified, we can simply clear its siblings and add the new handled siblings to it.
                    for (FormattedText formattedText : reveal(oldSiblings)) {
                        if (formattedText instanceof Component component1) newComponent.getSiblings().add(component1);
                    }

                }
                formattedTexts.add(newComponent);

            } else {
                formattedTexts.add(component);
            }


        }
        return formattedTexts;
    }

    private static MutableComponent resolveEnlightenComponent(MutableComponent target, Component enlighten) {
        Map<String, Component> enlightenMap = resolveEnlighten(enlighten);
        return handleWholeComponent(target, enlightenMap);
    }

    private static MutableComponent handleWholeComponent(MutableComponent target, Map<String, Component> enlightenMap) {
        Set<String> strings = enlightenMap.keySet();
        Optional<Map<String, Component>> tuple2s = ((MutableComponentDuck) target).tooltips_Core$grabEligibles((style, content) -> {
            List<String> javaList = strings.filter(content::contains)
                    .toJavaList();
            if (javaList.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(javaList);
            }
        }, Style.EMPTY, enlightenMap);
        if (tuple2s.isPresent()) {
            Map<String, Component> tuple2s1 = tuple2s.get();
            Set<String> matched = tuple2s1.keySet();
            MutableComponent newComp = MutableComponent.create(target.getContents());
            String string = newComp.getString();
            List<String> strings1 = splitBySubstrings(string, matched.toJavaList());
            if (!strings1.isEmpty()) {
                MutableComponent beginning = Component.literal("");
                for (int i = 0; i < strings1.size(); i++) {
                    String s1 = strings1.get(i);
                    if (matched.contains(s1)) {
                        beginning.append(Component.literal(s1).withStyle(target.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(tuple2s1.get(s1).get()))).withUnderlined(true)));
                    } else {
                        beginning.append(Component.literal(s1).withStyle(target.getStyle()));
                    }

                }
                return beginning;
            } else {
                TooltipsCore.LOGGER.warn("Fail at splitting target: {}, the result string list is empty.", target.getString());
                return target;
            }

        } else {
            TooltipsCore.LOGGER.debug("Component '{}' has its enlighten, but none of the enlighten can be found in its string.", target.getString());
            return target;
        }
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
                            matcher.group(1),
                            Component.translatable(termFinder.apply(matcher.group(2)))
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
