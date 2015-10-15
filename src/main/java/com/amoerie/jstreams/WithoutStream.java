package com.amoerie.jstreams;

import com.amoerie.jstreams.functions.Filter;

import java.util.Iterator;
import java.util.Set;

class WithoutStream<E> extends Stream<E> {

    private final Stream<E> originalStream;
    private final Stream<E> forbiddenElementsStream;

    public WithoutStream(Stream<E> stream, Stream<E> forbiddenElementsStream) {
        this.originalStream = stream;
        this.forbiddenElementsStream = forbiddenElementsStream;
    }

    @Override
    public Iterator<E> iterator() {
        final Set<E> forbiddenElementsSet = forbiddenElementsStream.toSet();
        return this.originalStream.filter(new Filter<E>() {
            @Override
            public boolean apply(E e) {
                return !forbiddenElementsSet.contains(e);
            }
        }).iterator();
    }
}
