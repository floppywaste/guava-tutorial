package com.github.floppywaste;

import static com.google.common.base.CharMatcher.WHITESPACE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.MapEntry.entry;

import java.util.Map;

import org.junit.Test;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Splitter.MapSplitter;
import com.google.common.base.Strings;


public class Guava02_Strings {

    @Test
    public void CharMatcher_matchesChars() throws Exception {
        assertThat(CharMatcher.ASCII.matches('a')).isTrue();
        assertThat(CharMatcher.ASCII.matches('ä')).isFalse();
        assertThat(CharMatcher.ASCII.matchesAnyOf("äscii?")).isTrue();
        assertThat(CharMatcher.ASCII.matchesAllOf("äscii?")).isFalse();
        assertThat(CharMatcher.ASCII.matchesNoneOf("äöü")).isTrue();
    }

    @Test
    public void CharMatcher_buildAndCompose() throws Exception {
        assertThat(CharMatcher.anyOf("ABC").countIn("AbC")).isEqualTo(2);
        assertThat(CharMatcher.forPredicate(c -> c > 66).countIn("AbC")).isEqualTo(2);
        assertThat(CharMatcher.ASCII.negate().replaceFrom("@b¢d€fg", "?")).isEqualTo("@b?d?fg");
        assertThat(CharMatcher.DIGIT.or(CharMatcher.WHITESPACE).retainFrom("#1 2a3b")).isEqualTo("1 23");
        assertThat(CharMatcher.DIGIT.or(CharMatcher.WHITESPACE).removeFrom("#1 2a3b")).isEqualTo("#ab");
    }

    @Test
    public void Strings_padding__whoWantsUnequalLineWidths() throws Exception {
        assertThat(Strings.padEnd("This line must have 40 characters", 40, '_')).isEqualTo(
                "This line must have 40 characters_______");
        assertThat(Strings.padStart("This line must have 40 characters", 40, '_')).isEqualTo(
                "_______This line must have 40 characters");
    }

    @Test
    public void Joiner_greatForWritingCSV() throws Exception {
        assertThat(Joiner.on(", ").skipNulls().join("a", "b", "c", null, "e")).isEqualTo("a, b, c, e");
        assertThat(Joiner.on(", ").useForNull("_").join("a", "b", "c", null, "e")).isEqualTo("a, b, c, _, e");
    }

    @Test
    public void Splitter_greatForParsingCSV() throws Exception {
        assertThat(Splitter.on(",").omitEmptyStrings().trimResults().split("a, b, ,d")).containsExactly("a", "b", "d");
        assertThat(Splitter.on(",").omitEmptyStrings().trimResults().split("a, b, ,d")).containsExactly("a", "b", "d");
    }

    @Test
    public void Splitter_mapSplitter() throws Exception {
        String jsonString = "{a:1, b:2, d:4}";

        Splitter pairsSplitter = Splitter.on(',').trimResults(CharMatcher.anyOf("{}").or(WHITESPACE));
        MapSplitter keyValueSplitter = pairsSplitter.withKeyValueSeparator(Splitter.on(':').trimResults());
        Map<String, String> jsonAsMap = keyValueSplitter.split(jsonString);

        assertThat(jsonAsMap).contains(entry("a", "1"));
        assertThat(jsonAsMap).contains(entry("b", "2"));
        assertThat(jsonAsMap).contains(entry("d", "4"));
    }

    @Test
    public void CaseFormat_inCaseYouWannaFormatCases() throws Exception {
        assertThat(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "UpperCamel")).isEqualTo("upper_camel");
        assertThat(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "lower-hyphen")).isEqualTo("lowerHyphen");
    }
}
