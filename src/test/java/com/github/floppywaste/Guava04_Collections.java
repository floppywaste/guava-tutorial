package com.github.floppywaste;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Test;

import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.collect.AbstractSequentialIterator;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ForwardingList;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;


public class Guava04_Collections {

    static final List<Character> CHARACTER_LIST = ImmutableList.of('a', 'b', '3', 'd');
    static final List<Integer> INTEGER_LIST = ImmutableList.of(1, 2, 3, 4, 5, 6, 7);

    @SuppressWarnings("unchecked")
    @Test
    public void Iterables_partitionAndConcat() throws Exception {
        List<Character> part1 = ImmutableList.of('a', 'b');
        List<Character> part2 = ImmutableList.of('3', 'd');

        assertThat(Iterables.partition(CHARACTER_LIST, 2)).containsExactly(part1, part2);

        assertThat(Iterables.concat(part1, part2)).containsExactlyElementsOf(CHARACTER_LIST);

    }

    @Test
    public void Iterables_cycle() throws Exception {
        assertThat(Iterables.limit(Iterables.cycle(1, 2), 10)).containsExactly(1, 2, 1, 2, 1, 2, 1, 2, 1, 2);
    }

    @Test
    public void FluentIterable_similarToJava8StreamAPI() throws Exception {
        FluentIterable<Character> fluent =
                (FluentIterable<Character>) Iterables.unmodifiableIterable(Lists.newArrayList(CHARACTER_LIST));

        assertThat(
                fluent.filter(CharMatcher.JAVA_LETTER).cycle().limit(6).transform(c -> c.toString().toUpperCase())
                        .join(Joiner.on(";"))).isEqualTo("A;B;D;A;B;D");
    }

    @Test
    public void Maps_uniqueIndex() throws Exception {
        Map<Integer, Character> indexed = Maps.uniqueIndex(CHARACTER_LIST, c -> Character.getNumericValue(c));

        assertThat(indexed).containsEntry(10, 'a').containsEntry(11, 'b').containsEntry(3, '3').containsEntry(13, 'd');
    }

    @Test
    public void MultiMaps_index() throws Exception {
        Multimap<Integer, Integer> indexed = Multimaps.index(INTEGER_LIST, i -> i % 3);

        // assertj has special guava assertions
        assertThat(indexed).containsKeys(0, 1, 2);

        assertThat(indexed.get(0)).containsExactly(3, 6);
        assertThat(indexed.get(1)).containsExactly(1, 4, 7);
        assertThat(indexed.get(2)).containsExactly(2, 5);
    }

    @Test
    public void Multiset_greatForCountingArbitraryEvents() throws Exception {
        Multiset<String> reporter1 = HashMultiset.create();
        Multiset<String> reporter2 = HashMultiset.create();

        reporter1.add("success");
        reporter1.add("success");
        reporter2.add("success");
        reporter1.add("fail");
        reporter1.add("success");
        reporter2.add("fail");
        reporter1.add("success");
        reporter1.add("fail");
        reporter1.add("success");
        reporter2.add("success");
        reporter2.add("success");

        assertThat(reporter1.count("success")).isEqualTo(5);
        assertThat(reporter1.count("fail")).isEqualTo(2);

        assertThat(reporter2.count("success")).isEqualTo(3);
        assertThat(reporter2.count("fail")).isEqualTo(1);

        Multiset<String> totalReporter = Multisets.sum(reporter1, reporter2);

        assertThat(totalReporter.count("success")).isEqualTo(8);
        assertThat(totalReporter.count("fail")).isEqualTo(3);

    }

    @Test
    public void AbstractSequentialIterator_infiniteSequences() throws Exception {
        Iterator<Integer> allNaturalNumbers = new AbstractSequentialIterator<Integer>(1) {
            @Override
            protected Integer computeNext(final Integer previous) {
                return previous + 1;
            }
        };

        Iterator<Integer> first10 = Iterators.limit(allNaturalNumbers, 10);

        assertThat(first10).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }

    @Test
    public void ForwardingCollections_extendBehaviorWithDecorators() throws Exception {
        List<Integer> pickyList = new ForwardingList<Integer>() {

            List<Integer> delegate = Lists.newArrayList();

            @Override
            protected List<Integer> delegate() {
                return delegate;
            }

            @Override
            public void add(final int index, final Integer elem) {
                if (elem > 0) {
                    super.add(index, elem);
                }
            }

            @Override
            public boolean add(final Integer elem) {
                if (elem > 0) {
                    return standardAdd(elem);
                }
                return false;
            }
            
            // skipped addAll(...)

        };

        pickyList.add(-1);
        pickyList.add(0);
        pickyList.add(1);
        pickyList.add(2);
        
        assertThat(pickyList).containsExactly(1, 2);

    }
}
