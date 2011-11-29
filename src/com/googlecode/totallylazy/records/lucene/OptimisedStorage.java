package com.googlecode.totallylazy.records.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.Version;

import java.io.IOException;

public class OptimisedStorage implements LuceneStorage {
    private final Directory directory;
    private final Version version;
    private final Analyzer analyzer;
    private IndexWriter writer;
    private Object writerLock = new Object();
    private IndexSearcher searcher;
    private Object readerLock = new Object();
    private IndexWriterConfig.OpenMode mode;

    public OptimisedStorage(Directory directory) {
        this(directory, Version.LUCENE_33, new KeywordAnalyzer(), IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
    }

    public OptimisedStorage(Directory directory, Version version, Analyzer analyzer, IndexWriterConfig.OpenMode mode) {
        this.directory = directory;
        this.version = version;
        this.analyzer = analyzer;
        this.mode = mode;
    }

    @Override
    public IndexWriter writer() throws IOException {
        ensureIndexIsSetup();
        closeSearcher();
        return writer;
    }

    @Override
    public IndexSearcher searcher() throws IOException {
        ensureIndexIsSetup();
        createSearcher();
        return searcher;
    }

    @Override
    public void close() throws IOException {
        closeSearcher();
        closeWriter();
        directory.close();
    }

    private void ensureIndexIsSetup() throws IOException {
        synchronized (writerLock) {
            if (writer == null) {
                writer = new IndexWriter(directory, new IndexWriterConfig(version, analyzer).setOpenMode(mode));
                writer.commit();
            }
        }
    }

    private void createSearcher() throws IOException {
        synchronized (readerLock) {
            if (searcher == null) {
                searcher = new IndexSearcher(directory);
            }
        }
    }

    private void closeWriter() throws IOException {
        synchronized (writerLock) {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        }
    }

    private void closeSearcher() throws IOException {
        synchronized (readerLock) {
            if (searcher != null) {
                searcher.close();
                searcher = null;
            }
        }
    }
}
