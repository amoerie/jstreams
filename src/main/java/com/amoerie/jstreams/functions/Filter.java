package com.amoerie.jstreams.functions;

public interface Filter<E> {
    boolean apply(E e);
}
