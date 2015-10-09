package com.amoerie.streams.functions;

public interface Reducer<E, R> {
    R reduce(R r, E e);
}
