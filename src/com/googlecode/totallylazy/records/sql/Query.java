package com.googlecode.totallylazy.records.sql;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.records.Keyword;
import com.googlecode.totallylazy.records.Record;

import java.util.Comparator;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Query {
    private final Keyword table;
    private final Sequence<Keyword> select;
    private final String selectFunction;
    private final Sequence<Predicate<? super Record>> where;
    private final Option<Comparator<? super Record>> comparator;

    private Query(Keyword table, Sequence<Keyword> select, String selectFunction, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator) {
        this.table = table;
        this.select = select;
        this.selectFunction = selectFunction;
        this.where = where;
        this.comparator = comparator;
    }

    @Override
    public String toString() {
        final Pair<String, Sequence<Object>> pair = expressionAndParameters();
        return String.format(String.format("SQL:'%s' VALUES:'%s'", pair.first(), pair.second()));
    }

    public Sql sql(){
        return Sql.sql(table);
    }

    public Pair<String, Sequence<Object>> expressionAndParameters() {
        final Pair<String, Sequence<Object>> whereClause = sql().whereClause(where);
        return pair(String.format("select %s from %s %s %s", selectClause(), table, whereClause.first(), sql().orderByClause(comparator)), whereClause.second());
    }

    private Object selectClause() {
        return applyFunction( select.isEmpty() ? "*" : sequence(select).toString(table.toString() + ".", ",", "") );
    }

    private Object applyFunction(String columns) {
        return selectFunction == null ? columns : String.format(selectFunction, columns);
    }

    public static Query query(Keyword table, Sequence<Keyword> select, String selectFunction, Sequence<Predicate<? super Record>> where, Option<Comparator<? super Record>> comparator) {
        return new Query(table, select, selectFunction, where, comparator);
    }

    public static Query query(Keyword table) {
        return query(table, Sequences.<Keyword>empty(), null, Sequences.<Predicate<? super Record>>empty(), Option.<Comparator<? super Record>>none());
    }

    public Query select(Keyword... columns){
        return select(sequence(columns));
    }

    public Query select(Sequence<Keyword> columns){
        return query(table, columns, selectFunction, where, comparator);
    }

    public Query where(Predicate<? super Record> predicate) {
        return query(table, select, selectFunction, where.add(predicate), comparator);
    }

    public Query orderBy(Comparator<? super Record> comparator) {
        return query(table, select, selectFunction, where, Option.<Comparator<? super Record>>some(comparator));
    }

    public Query count() {
        return query(table, select, "count(%s)", where, comparator);
    }
}
