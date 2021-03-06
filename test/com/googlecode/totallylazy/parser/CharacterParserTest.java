package com.googlecode.totallylazy.parser;

import org.junit.Test;

import static com.googlecode.totallylazy.Segment.constructors.characters;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static com.googlecode.totallylazy.parser.CharacterParser.character;
import static org.hamcrest.MatcherAssert.assertThat;

public class CharacterParserTest {
    @Test
    public void doesNotThrowIfItRunsOutOfCharacters() throws Exception {
        Failure<Character> result = cast(character('A').parse(""));
        assertThat(result.message(), is("A expected, [EOF] encountered."));
    }

    @Test
    public void canParseACharacter() throws Exception {
        Result<Character> result = character('A').parse("ABC");
        assertThat(result.value(), is('A'));
        assertThat(result.remainder(), is(characters("BC")));
    }

    @Test
    public void handlesNoMatch() throws Exception {
        Failure<Character> result = cast(character('A').parse("CBA"));
        assertThat(result.message(), is("A expected, C encountered."));
    }
}
