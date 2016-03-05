package com.googlecode.totallylazy.callables;

import com.googlecode.totallylazy.Combiner;
import com.googlecode.totallylazy.Strings;

public class JoinString implements Combiner<String> {
    public static final Combiner<String> instance = new JoinString();
    private JoinString() {}

    @Override
    public String call(String a, String b) throws Exception {
        return a + b;
    }

    @Override
    public String identityElement() {
        return Strings.EMPTY;
    }

    @Override
    public String toString() {
        return "join";
    }
}