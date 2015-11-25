package com.amoerie.jstreams;

import java.util.Iterator;

import com.amoerie.jstreams.functions.Mapper;

class MappedStream<E, R> extends Stream<R> {

    private final Stream<E> stream;
    private final Mapper<E, R> mapper;

    MappedStream(Stream<E> stream, Mapper<E, R> mapper) {
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
                return mapper.map(iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
