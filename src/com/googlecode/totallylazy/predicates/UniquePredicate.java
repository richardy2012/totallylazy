package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.Callers;

import java.util.HashSet;
import java.util.Set;

public class UniquePredicate<T, S> extends LogicalPredicate<T> {
    private final Function1<? super T, ? extends S> callable;
    private final Set<S> valuesSeen = new HashSet<S>();

    public UniquePredicate(Function1<? super T, ? extends S> callable) {
        this.callable = callable;
    }

    @Override
    public boolean matches(T other) {
        return valuesSeen.add(Callers.call(callable, other));
    }
}
