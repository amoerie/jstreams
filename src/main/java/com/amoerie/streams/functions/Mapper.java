package com.amoerie.streams.functions;

public interface Mapper<E, R> {
    R map(E e);
}
