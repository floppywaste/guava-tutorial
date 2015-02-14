package com.github.floppywaste;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.hash.Hashing.md5;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;
import com.google.common.io.Resources;


public class Guava05_IO {

    ByteSource byteSource = Resources.asByteSource(Resources.getResource("test.csv"));
    CharSource charSource = Resources.asCharSource(Resources.getResource("test.csv"), Charsets.UTF_8);

    @Test
    public void byteSource_hash() throws Exception {
        HashCode hash = byteSource.hash(Hashing.md5());
        assertThat(hash.toString()).isEqualTo("551618522380a4e6dc7097eeb41a56f1");
    }

    @Test
    public void charSource_readLines() throws Exception {
        ImmutableList<String> lines = charSource.readLines();
        for (String line : lines) {
            System.out.println(line);
        }

        Set<String> devices = charSource.readLines(firstColumnExtractor());
        for (String device : devices) {
            System.out.println(device);
        }
    }

    private LineProcessor<Set<String>> firstColumnExtractor() {
        return new LineProcessor<Set<String>>() {
            final Set<String> result = Sets.newHashSet();

            @Override
            public boolean processLine(final String line) {
                String firstColumn = Splitter.on(',').split(line).iterator().next();
                result.add(firstColumn);
                return true;
            }

            @Override
            public Set<String> getResult() {
                return result;
            }
        };
    }

    @Test
    public void CharSource_concat() throws Exception {
        CharSource concat = CharSource.concat(charSource, charSource);
        for (String line : concat.readLines()) {
            System.out.println(line);
        }
    }

    @Test
    public void byteSource_copyTo_byteSink() throws Exception {
        File outputFile = new File("out.csv");
        ByteSink byteSink = Files.asByteSink(outputFile);

        byteSource.copyTo(byteSink);

        HashCode sourceHash = byteSource.hash(md5());
        HashCode sinkHash = Files.hash(outputFile, md5());
        assertThat(sourceHash).isEqualTo(sinkHash);

        outputFile.delete();
    }

    @Test
    public void Files_toString() throws Exception {
        File file = new File(Resources.getResource("test.csv").toURI());
        System.out.println(Files.toString(file, Charsets.UTF_8));
    }

    @Test
    public void BaseEncoding_stillBeta() throws Exception {
        String jsonString = "{tokenExpiration : 1423927631039}";
        
        String encoded = BaseEncoding.base64Url().encode(jsonString.getBytes(UTF_8));

        assertThat(encoded).isEqualTo("e3Rva2VuRXhwaXJhdGlvbiA6IDE0MjM5Mjc2MzEwMzl9");
    }

}
