package com.amoerie.jstreams;

import java.util.Iterator;

class CastStream<E, C> extends Stream<C> {

    private final Class<C> clazz;
    private final Stream<E> stream;

    public CastStream(Stream<E> stream, Class<C> clazz) {
        this.stream = stream;
        this.clazz = clazz;
    }

    @Override
    public Iterator<C> iterator() {
        final Iterator<E> iterator = stream.iterator();
        return new Iterator<C>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public C next() {
                return clazz.cast(iterator.next());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
