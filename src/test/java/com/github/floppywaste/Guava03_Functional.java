package com.github.floppywaste;

import static com.google.common.base.CharMatcher.is;
import static com.google.common.base.Functions.toStringFunction;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Lists.transform;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


public class Guava03_Functional {

    /*
     * Immutability is the mother of all trust.
     */
    static final List<Integer> INT_LIST = ImmutableList.of(1, 2, 3, 4);
    static final List<Character> CHAR = ImmutableList.of('a', 'b', '3', 'd');
    static final Map<String, Integer> EXAMPLE_MAP = ImmutableMap.of("a", 1, "b", 2, "c", 3);

    @Test
    public void Functions_transform_aka_map() throws Exception {
        List<Integer> transformedList = Lists.transform(INT_LIST, square());
        assertThat(transformedList).containsExactly(1, 4, 9, 16);

        Iterable<Integer> transformedIterable = Iterables.transform(INT_LIST, square());
        assertThat(transformedIterable).containsExactly(1, 4, 9, 16);

        Collection<Integer> transformedCollection = Collections2.transform(INT_LIST, square());
        assertThat(transformedCollection).containsExactly(1, 4, 9, 16);

        Map<String, Integer> transformedMap = Maps.transformValues(EXAMPLE_MAP, square());
        assertThat(transformedMap).containsEntry("a", 1).containsEntry("b", 4).containsEntry("c", 9);
    }

    @Test
    public void Functions_predefined() throws Exception {
        assertThat(Lists.transform(INT_LIST, Functions.identity())).containsExactly(1, 2, 3, 4);

        assertThat(Lists.transform(INT_LIST, Functions.toStringFunction())).containsExactly("1", "2", "3", "4");

        assertThat(Lists.transform(INT_LIST, Functions.constant(100))).containsExactly(100, 100, 100, 100);
    }

    @Test
    public void Functions_composable() throws Exception {
        Function<Integer, Integer> square = square();
        Function<Object, String> stringFunction = toStringFunction();
        Function<Integer, String> composed = Functions.compose(stringFunction, square);

        assertThat(transform(INT_LIST, composed)).containsExactly("1", "4", "9", "16");
    }

    @Test
    public void Functions_lazyness_keepALowProfile() throws Exception {
        System.out.println("executing transform...");
        List<Integer> lazyView = Lists.transform(INT_LIST, square());
        System.out.println("transform view created.");

        System.out.println("accessing second element of view...");
        System.out.println(lazyView.get(1));
    }

    @Test
    public void Predicate_filter() throws Exception {
        Predicate<Integer> predicate = greater2();

        assertThat(Iterables.filter(INT_LIST, predicate)).containsExactly(3, 4);
    }

    @Test
    public void CharMatcher_implementsPredicate() throws Exception {
        Predicate<Character> filterPredicate = CharMatcher.is('a').or(CharMatcher.is('d'));
        assertThat(Iterables.filter(CHAR, filterPredicate)).containsExactly('a', 'd');

        // same inline and with static imports
        assertThat(filter(CHAR, is('a').or(is('d')))).containsExactly('a', 'd');
    }

    @Test
    public void Predicate_anyOrAll() throws Exception {
        assertThat(Iterables.any(CHAR, CharMatcher.WHITESPACE)).isFalse();
        assertThat(Iterables.any(CHAR, CharMatcher.DIGIT)).isTrue();

        assertThat(Iterables.all(CHAR, CharMatcher.DIGIT)).isFalse();
        assertThat(Iterables.all(CHAR, CharMatcher.ASCII)).isTrue();
    }

    @Test
    public void Functions_forPredicate() throws Exception {
        Predicate<Character> predicate = CharMatcher.DIGIT;
        Function<Character, Boolean> transformingFunction = Functions.forPredicate(predicate);
        assertThat(Iterables.transform(CHAR, transformingFunction)).containsExactly(false, false, true, false);
    }

    // in java 8 you can use lambdas instead
    static Function<Integer, Integer> square() {
        return new Function<Integer, Integer>() {

            @Override
            public Integer apply(final Integer input) {
                int result = input * input;
                System.out.printf("Calculating square of %d yielding %d\n", input, result);
                return result;
            }
        };
    }

    // in java 8 you can use lambdas instead
    static Predicate<Integer> greater2() {
        return new Predicate<Integer>() {

            @Override
            public boolean apply(final Integer input) {
                return input > 2;
            }
        };
    }

}
