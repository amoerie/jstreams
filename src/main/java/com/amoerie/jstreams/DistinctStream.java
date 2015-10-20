package com.amoerie.jstreams;

import com.amoerie.jstreams.functions.Filter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class DistinctStream<E> extends Stream<E> {

    private final Stream<E> stream;

    DistinctStream(final Stream<E> stream) {
        this.stream = stream;
    }

    @Override
    public Iterator<E> iterator() {
        final Set<E> seenElements = new HashSet<E>();
        return stream.filter(new Filter<E>() {
            @Override
            public boolean apply(E e) {
                return seenElements.add(e);
            }
        }).iterator();
    }
}
