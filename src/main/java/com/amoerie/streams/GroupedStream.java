package com.amoerie.streams;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import rx.functions.Func1;

/**
 * Created by alexa on 06-Oct-15.
 */
class GroupedStream<K, E> extends Stream<Group<K, E>> {

    private final Stream<E> stream;
    private final Func1<E, K> keySelector;

    public GroupedStream(Stream<E> stream, Func1<E, K> keySelector) {
        this.stream = stream;
        this.keySelector = keySelector;
    }

    @Override
    public Iterator<Group<K, E>> iterator() {
        final Map<K, List<E>> groupMap = new LinkedHashMap<>();
        for(E element: stream) {
            K key = keySelector.call(element);
            List<E> elementsWithThisKey;
            if(groupMap.containsKey(key)) {
                elementsWithThisKey = groupMap.get(key);
            } else {
                elementsWithThisKey = new ArrayList<>();
                groupMap.put(key, elementsWithThisKey);
            }
            elementsWithThisKey.add(element);
        }
        final Iterator<Map.Entry<K, List<E>>> iterator = groupMap.entrySet().iterator();
        return new Iterator<Group<K, E>>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public Group<K, E> next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                Map.Entry<K, List<E>> next = iterator.next();
                return new GroupImpl<>(next.getKey(), Stream.create(next.getValue()));
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
