package me.clefal.tooltips_core.enlighten.utils;

import com.clefal.nirvana_lib.relocated.io.vavr.Tuple;
import com.clefal.nirvana_lib.relocated.io.vavr.Tuple2;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.LinkedHashMap;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Map;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Set;
import com.clefal.nirvana_lib.relocated.io.vavr.collection.Stream;
import lombok.experimental.UtilityClass;
import me.clefal.tooltips_core.TooltipsCore;
import net.minecraft.ChatFormatting;
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
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class EnlightenUtil {
    private static final Function<String, String> getEnlighten = string -> string + ".enlighten";
    private static final Function<String, String> termFinder = s -> "enlighten.term." + s;
    private static Pattern group = Pattern.compile("\\[([^\\]]+)\\]\\(([^)]+)\\)");

    public static List<? extends FormattedText> reveal(Component component) {
        return reveal(List.of(component));
    }

    public static List<? extends FormattedText> reveal(List<? extends FormattedText> components) {
        List<FormattedText> formattedTexts = new ArrayList<>();
        for (FormattedText component : components) {
            if (component instanceof MutableComponent comp) {
                Component newComponent;
                if (comp.getContents() instanceof TranslatableContents contents) {
                    if (I18n.exists(getEnlighten.apply(contents.getKey()))) {
                        newComponent = resolveEnlightenComponent(comp, Component.translatable(getEnlighten.apply(contents.getKey())));
                    } else {
                        //TranslatableContents also can have internal enlighten.
                        MutableComponent copy = comp.copy();
                        copy.getSiblings().clear();
                        if (group.matcher(copy.getString()).find()){
                            newComponent = revealNestedEnlighten(comp);
                        }
                    }

                } else if (comp.getContents() instanceof PlainTextContents.LiteralContents contents && group.matcher(contents.text()).find()) {
                    newComponent = revealNestedEnlighten(comp);
                } else {
                    MutableComponent copy = comp.copy();
                    copy.getSiblings().clear();
                    newComponent = copy;

                }
                tryAppendSiblings(comp, newComponent);
                formattedTexts.add(newComponent);

            } else {
                formattedTexts.add(component);
            }


        }
        return formattedTexts;
    }

    private static void tryAppendSiblings(MutableComponent comp, Component newComponent) {
        if (!comp.getSiblings().isEmpty()) {
            ArrayList<Component> oldSiblings = new ArrayList<>(comp.getSiblings());
            //if the comp hasn't been modified, we can simply clear its siblings and add the new handled siblings to it.
            for (FormattedText formattedText : reveal(oldSiblings)) {
                if (formattedText instanceof Component component1) newComponent.getSiblings().add(component1);
            }

        }
    }

    private static MutableComponent resolveEnlightenComponent(MutableComponent target, Component enlighten) {
        Map<String, Component> enlightenMap = resolveEnlighten(enlighten);
        return handleWholeComponent(target, enlightenMap);
    }

    private static MutableComponent handleWholeComponent(MutableComponent target, Map<String, Component> enlightenMap) {
        Set<String> strings = enlightenMap.keySet();
        if (strings.contains("*")){
            MutableComponent copy = target.copy();
            copy.withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(enlightenMap.get("*").get()))).withUnderlined(true));
            return copy;
        }
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
                    strings.find(s1::contains)
                            .onEmpty(() -> beginning.append(Component.literal(s1).withStyle(target.getStyle())))
                            .forEach(x -> {
                                if (s1.contains("§")){
                                    Style newStyle = extractMcFormattingCodesAsStyle(s1);
                                    beginning.append(Component.literal(s1.replaceAll("§.", "")).withStyle(newStyle.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(tuple2s1.get(x).get()))).withUnderlined(true)));
                                } else {
                                    beginning.append(Component.literal(s1).withStyle(target.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(tuple2s1.get(x).get()))).withUnderlined(true)));
                                }
                            });

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

    public static List<String> splitBySubstrings(
            String input,
            Iterable<String> patterns
    ) {
        List<String> result = new ArrayList<>();
        int index = 0;

        List<Pattern> compiled = new ArrayList<>();
        for (String p : patterns) {
            if (p != null && !p.isEmpty()) {
                compiled.add(compileMcPattern(p));
            }
        }

        while (index < input.length()) {
            int matchIndex = -1;
            Matcher best = null;

            for (Pattern pattern : compiled) {
                Matcher m = pattern.matcher(input);
                if (m.find(index)) {
                    if (matchIndex == -1 || m.start() < matchIndex) {
                        matchIndex = m.start();
                        best = m;
                    }
                }
            }

            if (best == null) {
                result.add(input.substring(index));
                break;
            }

            if (best.start() > index) {
                result.add(input.substring(index, best.start()));
            }

            // ★ 关键：这里得到的是 “§r§7Gear Defense”
            result.add(best.group());

            index = best.end();
        }

        return result;
    }

    private static Pattern compileMcPattern(String literal) {
        // (§.)* + Gear Defense（逐字转义）
        String regex = "(?:§[^r])*" + Pattern.quote(literal);
        return Pattern.compile(regex);
    }

    public static Component revealNestedEnlighten(Component enlighten){
        String string = enlighten.getString();
        Matcher matcher = group.matcher(string);

        Stream<MatchResult> matchResults = Stream.ofAll(matcher.results());
        if (matchResults.isEmpty()) return enlighten.copy();

        LinkedHashMap<String, MutableComponent> terms = LinkedHashMap.ofEntries(matchResults.map(x -> Tuple.of(
                        x.group(1),
                        Component.translatable(termFinder.apply(x.group(2)))
                ))
                .filter(tuple2 -> {
                    boolean exists = ComponentUtils.isTranslationResolvable(tuple2._2());
                    if (!exists) {
                        TooltipsCore.LOGGER.warn("found non-exist term: {}", tuple2._2().getString());
                    }
                    return exists;
                }));
        Matcher secondMatch = group.matcher(string);


        StringBuilder sb = new StringBuilder();
        while (secondMatch.find()) {
            secondMatch.appendReplacement(sb, secondMatch.group(1));
        }
        secondMatch.appendTail(sb);
        string = sb.toString();

        Set<String> strings = terms.keySet();

        List<String> strings1 = splitBySubstrings(string, strings);
        if (!strings1.isEmpty()) {

            MutableComponent beginning = Component.literal("");
            for (int i = 0; i < strings1.size(); i++) {
                String s1 = strings1.get(i);
                strings.find(s1::contains)
                        .onEmpty(() -> beginning.append(Component.literal(s1).withStyle(enlighten.getStyle())))
                        .forEach(x -> {
                            if (s1.contains("§")){
                                Style newStyle = extractMcFormattingCodesAsStyle(s1);
                                beginning.append(Component.literal(s1.replaceAll("§.", "")).withStyle(newStyle.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(terms.get(x).get()))).withUnderlined(true)));
                            } else {
                                beginning.append(Component.literal(s1).withStyle(enlighten.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("enlighten: ").append(terms.get(x).get()))).withUnderlined(true)));
                            }

                        });
            }
            return beginning;
        } else {
            TooltipsCore.LOGGER.warn("Fail at splitting target: {}, the result string list is empty.", enlighten.getString());
            return enlighten;
        }

    }
    public static Style extractMcFormattingCodesAsStyle(String input) {
        Style empty = Style.EMPTY;
        if (input == null || input.isEmpty()) {
            return empty;
        }

        Matcher matcher = Pattern.compile("§(.)").matcher(input);
        while (matcher.find()) {
            ChatFormatting byCode = ChatFormatting.getByCode(matcher.group(1).charAt(0));

            if (byCode != null){
                empty = empty.applyFormat(byCode);
            }
        }

        return empty;
    }

    public static Component revealNestedEnlightenOnClick(Component enlighten){
        String string = enlighten.getString();
        Matcher matcher = group.matcher(string);

        Stream<MatchResult> matchResults = Stream.ofAll(matcher.results());
        if (matchResults.isEmpty()) return enlighten.copy();

        LinkedHashMap<String, MutableComponent> tuple2s = LinkedHashMap.ofEntries(matchResults.map(x -> Tuple.of(
                        x.group(1),
                        Component.translatable(termFinder.apply(x.group(2)))
                ))
                .filter(tuple2 -> {
                    boolean exists = ComponentUtils.isTranslationResolvable(tuple2._2());
                    if (!exists) {
                        TooltipsCore.LOGGER.warn("found non-exist term: {}", tuple2._2().getString());
                    }
                    return exists;
                }));

        Matcher secondMatch = group.matcher(string);


        StringBuilder sb = new StringBuilder();
        while (secondMatch.find()) {
            secondMatch.appendReplacement(sb, secondMatch.group(1));
        }
        secondMatch.appendTail(sb);
        string = sb.toString();

        Set<String> strings = tuple2s.keySet();

        List<String> strings1 = splitBySubstrings(string, strings);
        if (!strings1.isEmpty()) {

            MutableComponent beginning = Component.literal("");
            for (int i = 0; i < strings1.size(); i++) {
                String s1 = strings1.get(i);

                if (strings.contains(s1)) {
                    beginning.append(Component.literal(s1).withStyle(enlighten.getStyle().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.empty().append(tuple2s.get(s1).get()))).withUnderlined(true)));
                } else {
                    beginning.append(Component.literal(s1).withStyle(enlighten.getStyle()));
                }

            }
            return beginning;
        } else {
            TooltipsCore.LOGGER.warn("Fail at splitting target: {}, the result string list is empty.", enlighten.getString());
            return enlighten;
        }

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

    public static boolean isNested(Component component){
        String string = component.getString();
        return group.matcher(string).find();
    }
}
