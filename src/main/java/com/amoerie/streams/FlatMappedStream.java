package com.amoerie.streams;

import java.util.Iterator;

import rx.functions.Func1;

public class FlatMappedStream<E, R> extends Stream<R> {

    private final Stream<E> stream;
    private final Func1<E, Stream<R>> flatMapper;

    public FlatMappedStream(Stream<E> stream, Func1<E, Stream<R>> flatMapper) {
        this.stream = stream;
        this.flatMapper = flatMapper;
    }

    @Override
    public Iterator<R> iterator() {
        final Stream<Stream<R>> mappedStream = new MappedStream<>(stream, flatMapper);
        final Stream<R> flatMappedStream = new FlatStream<>(mappedStream);
        return flatMappedStream.iterator();
    }
}
