package com.amoerie.streams;

import java.util.Iterator;

import rx.functions.Func1;

class MappedStream<E, R> extends Stream<R> {

    private Stream<E> stream;
    private Func1<E, R> mapper;

    MappedStream(Stream<E> stream, Func1<E, R> mapper) {
        this.stream = stream;
        this.mapper = mapper;
    }

    @Override
    public Iterator<R> iterator() {
        final Iterator<E> iterator = stream.iterator();
        return new Iterator<R>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public R next() {
                return mapper.call(iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
