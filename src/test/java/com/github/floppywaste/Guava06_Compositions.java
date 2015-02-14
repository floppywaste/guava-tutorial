package com.github.floppywaste;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.io.CharSource;
import com.google.common.io.Resources;
import com.google.common.math.DoubleMath;


public class Guava06_Compositions {

    @Test
    public void example_processingCsvFile() throws Exception {
        // get reader abstraction
        CharSource charSource = Resources.asCharSource(Resources.getResource("test.csv"), Charsets.UTF_8);

        // open a stream and read characters into strings
        ImmutableList<String> lines = charSource.readLines();

        // parse the lines
        List<List<String>> csvTable = Lists.transform(lines, fromSplitter(Splitter.on(',')));

        // group lines by the device column
        Multimap<String, List<String>> groupedByDevice = Multimaps.index(csvTable, elementAt(0));

        // extract and parse the value column for aggregation
        Multimap<String, Integer> valuesPerDevice =
                Multimaps.transformValues(groupedByDevice, Functions.compose(toInt(), elementAt(2)));

        // aggregate the values
        Map<String, Double> averageValuePerDevice = Maps.transformValues(valuesPerDevice.asMap(), mean());

        assertThat(averageValuePerDevice).containsEntry("Sensor A", 150.);
        assertThat(averageValuePerDevice).containsEntry("Sensor B", 406.25);
    }

    private Function<String, List<String>> fromSplitter(final Splitter splitter) {
        return new Function<String, List<String>>() {

            @Override
            public List<String> apply(final String input) {
                return splitter.splitToList(input);
            }
        };
    }

    <T> Function<List<T>, T> elementAt(final int index) {
        return new Function<List<T>, T>() {

            @Override
            public T apply(final List<T> input) {
                return input.get(index);
            }
        };
    }

    private Function<String, Integer> toInt() {
        return new Function<String, Integer>() {

            @Override
            public Integer apply(final String input) {
                return Integer.valueOf(input);
            }
        };
    }

    private Function<Collection<? extends Number>, Double> mean() {
        return new Function<Collection<? extends Number>, Double>() {

            @Override
            public Double apply(final Collection<? extends Number> input) {
                return DoubleMath.mean(input);
            }
        };
    }

}
