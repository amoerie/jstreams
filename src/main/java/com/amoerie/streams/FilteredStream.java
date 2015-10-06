package com.amoerie.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;

import rx.functions.Func1;

class FilteredStream<E> extends Stream<E> {
    private Stream<E> stream;
    private Func1<E, Boolean> filter;

    FilteredStream(Stream<E> stream, Func1<E, Boolean> filter) {
        this.stream = stream;
        this.filter = filter;
    }

    private E getNextFilteredElement(Iterator<E> iterator) {
        while(iterator.hasNext()) {
            E next = iterator.next();
            if(filter.call(next))
                return next;
        }
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        final Iterator<E> iterator = stream.iterator();
        return new Iterator<E>() {
            private E nextFilteredElement;

            @Override
            public boolean hasNext() {
                if(nextFilteredElement == null) {
                    nextFilteredElement = getNextFilteredElement(iterator);
                }
                return nextFilteredElement != null;
            }

            @Override
            public E next() {
                if(nextFilteredElement == null) {
                    nextFilteredElement = getNextFilteredElement(iterator);
                }
                if(nextFilteredElement == null) {
                    throw new NoSuchElementException();
                }
                E next = nextFilteredElement;
                nextFilteredElement = null;
                return next;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
