package com.amoerie.jstreams.functions;

public interface Mapper<E, R> {
    R map(E e);
}
