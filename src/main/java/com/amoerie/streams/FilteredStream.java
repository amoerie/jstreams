package com.amoerie.streams;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.amoerie.streams.functions.Filter;

class FilteredStream<E> extends Stream<E> {
    private Stream<E> stream;
    private Filter<E> filter;

    FilteredStream(Stream<E> stream, Filter<E> filter) {
        this.stream = stream;
        this.filter = filter;
    }

    private E getNextFilteredElement(Iterator<E> iterator) {
        while(iterator.hasNext()) {
            E next = iterator.next();
            if(filter.apply(next))
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
