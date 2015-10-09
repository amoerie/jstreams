package com.amoerie.jstreams;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class SortedStream<E> extends Stream<E> {

    private final Stream<E> stream;
    private final Comparator<E> comparator;

    public SortedStream(Stream<E> stream, Comparator<E> comparator) {
        this.stream = stream;
        this.comparator = comparator;
    }

    @Override
    public Iterator<E> iterator() {
        final List<E> list = stream.toList();
        Collections.sort(list, comparator);
        final Iterator<E> iterator = list.iterator();
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public E next() {
                return iterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
