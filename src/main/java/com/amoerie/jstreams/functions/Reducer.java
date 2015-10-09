package com.amoerie.jstreams.functions;

public interface Reducer<E, R> {
    R reduce(R r, E e);
}
