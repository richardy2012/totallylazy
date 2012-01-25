package com.googlecode.totallylazy.parser;

import com.googlecode.totallylazy.Sequence;
import org.junit.Test;

import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static com.googlecode.totallylazy.parser.ManyParser.many;
import static org.hamcrest.MatcherAssert.assertThat;

public class ManyParserTest {
    @Test
    public void supportMany() throws Exception {
        Success<Sequence<Character>> result = cast(many(character('C')).parse(characters("CCCCCDDDD")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(characters("DDDD")));
    }

    @Test
    public void supportChaining() throws Exception {
        Success<Sequence<Character>> result = cast(character('C').many().parse(characters("CCCCCDDDD")));
        assertThat(result.value(), is(characters("CCCCC")));
        assertThat(result.remainder(), is(characters("DDDD")));

    }
}
