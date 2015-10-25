package com.amoerie.jstreams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amoerie.jstreams.functions.Consumer;
import com.amoerie.jstreams.functions.Filter;
import com.amoerie.jstreams.functions.Mapper;
import com.amoerie.jstreams.functions.Reducer;

/**
 * Represents a collection of elements that are not known at construction time
 * This is a wrapper around the Iterator class, providing more functional methods than is standard provided by Java,
 * by combining some classic functional paradigms such as map and flatMap.
 *
 * @param <E> the type of each element in the stream
 */
public abstract class Stream<E> implements Iterable<E> {

    protected Stream() {
    }

    /* static methods (alphabetically) */

    /**
     * Creates a new stream from the provided array of elements
     *
     * @param elements the array of elements
     * @param <E>   the type of the elements
     * @return a new stream containing the elements of the array
     */
    public static <E> Stream<E> create(final E ... elements) {
        if (elements == null)
            throw new IllegalArgumentException("Unable to create a stream from this array because it is null!");
        return create(Arrays.asList(elements));
    }

    /**
     * Creates a new stream from the provided iterable
     * This is a lazy operation, it does not consume the iterable until a greedy operation is called, such as toList()
     *
     * @param elements an iterable containing elements
     * @param <E>      the type of an element
     * @return a new stream containing the elements of the iterable
     */
    public static <E> Stream<E> create(final Iterable<E> elements) {
        if (elements == null)
            throw new IllegalArgumentException("Unable to create a stream from this iterable because it is null!");
        return new IterableStream<E>(elements);
    }

    /**
     * Creates a new empty stream, containing no elements
     *
     * @param <E> the type of the elements of this stream
     * @return a new empty stream containing no elements
     */
    public static <E> Stream<E> empty() {
        return new EmptyStream<E>();
    }

    /**
     * Alias for {@link #create(Object[])}

     * @param elements the array of elements
     * @param <E>   the type of the elements
     * @return a new stream containing the elements of the array
     */
    public static <E> Stream<E> of(final E ... elements) {
        return create(elements);
    }

    /**
     * Alias for {@link #create(Iterable)}
     *
     * @param elements an iterable containing elements
     * @param <E>      the type of an element
     * @return a new stream containing the elements of the iterable
     */
    public static <E> Stream<E> of(Iterable<E> elements) {
        return create(elements);
    }

    /**
     * Creates a new singleton stream, containing exactly one element
     *
     * @param element the single element
     * @param <E>     the type of the single element
     * @return a new stream containing exactly one element
     */
    public static <E> Stream<E> singleton(final E element) {
        return new SingletonStream<E>(element);
    }

    /* Instance methods (alphabetically) */

    /**
     * Alias for {@link #some(Filter)}
     *
     * @param filter the filter that returns true or false for any given element
     * @return true if one of the elements satisfied the predicate or false otherwise
     */
    public boolean any(final Filter<E> filter) {
        return some(filter);
    }

    /**
     * Casts every element of this stream to another class
     *
     * @param clazz the class to cast to
     * @param <C>   the type of the class to cast to
     * @return a new stream containing every element casted to another class
     */
    public <C> Stream<C> cast(final Class<C> clazz) {
        if (clazz == null)
            throw new IllegalArgumentException("Unable to cast this stream because the class to cast to is null!");
        return new CastStream<E, C>(this, clazz);
    }

    /**
     * Concatenates this stream with another stream
     *
     * @param other the other stream to concatenate with
     * @return a new stream containing all the elements of this stream and the other stream
     */
    public Stream<E> concat(final Stream<E> other) {
		return new FlatStream<E>(create(new ArrayList<Stream<E>>(2) {
			private static final long serialVersionUID = -24564403429240129L;
			{
            add(Stream.this);
            add(other);
        }}));
    }

    /**
     * Filters this stream to only have unique elements.
     *
     * @return a new stream containing only unique elements.
     */
    public Stream<E> distinct() {
        return new DistinctStream<E>(this);
    }

    /**
     * Filters the elements of this stream with the given filter.
     *
     * @param filter the predicate that returns true or false for a given element
     * @return a new stream containing only the elements that satisfied the filter
     */
    public Stream<E> filter(final Filter<E> filter) {
        if (filter == null)
            throw new IllegalArgumentException("Unable to filter this stream because the filter is null!");
        return new FilteredStream<E>(this, filter);
    }

    /**
     * Gets the first element of this stream
     *
     * @return the first element of this stream or null if the stream is empty
     */
    public E first() {
        Iterator<E> iterator = iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    /**
	 * Executes given operation on every element of this stream, in the order of
	 * iteration
	 * 
	 * @param consumer
	 *            the function to be executed on elements of the stream
	 */
	public void forEach(Consumer<E> consumer) {
		if (consumer == null)
			throw new IllegalArgumentException("Unable to apply forEach because the consumer is null!");
		for (E e : this) {
			consumer.apply(e);
		}
	}

    /**
     * Maps each element of this stream to a separate stream, and then flattens the result to one single stream
     *
     * @param mapper the function that turns one element into a stream of values
     * @param <R>    the type of one mapped element
     * @return a new stream containing all elements of all the jstreams the mapper function created
     */
    public <R> Stream<R> flatMap(final Mapper<E, Stream<R>> mapper) {
        if (mapper == null)
            throw new IllegalArgumentException("Unable to flatMap this stream because the mapper is null!");
        return new FlatStream<R>(new MappedStream<E, Stream<R>>(this, mapper));
    }

    /**
     * Groups this stream into chunks based on the key per element that is retrieved via the keySelector
     *
     * @param keyMapper a function that returns the grouping key for a given element
     * @param <K>       the type of the key
     * @return a stream containing groups as its elements
     */
    public <K> Stream<Group<K, E>> groupBy(final Mapper<E, K> keyMapper) {
        if (keyMapper == null)
            throw new IllegalArgumentException("Unable to group this stream because the keyMapper is null!");
        return new GroupedStream<K, E>(this, keyMapper);
    }

    /**
     * Joins the stream using the given delimiter
     *
     * @param delimiter the delimiter to be inserted between each element
     * @return a string containing all of the elements with the given delimiter between each element
     */
    public String join(final String delimiter) {
        if (delimiter == null)
            throw new IllegalArgumentException("Unable to join this stream because the provided delimiter is null");
        return this.reduce(new Reducer<E, StringBuilder>() {
            private boolean isFirstElement = true;
            @Override
            public StringBuilder reduce(StringBuilder s, E e) {
                return ((isFirstElement && !(isFirstElement = false) ? s : s.append(delimiter))).append(String.valueOf(e));
            }
        }, new StringBuilder()).toString();
    }

    /**
     * Gets the last element of this stream
     *
     * @return the last element of this stream or null if the stream is empty
     */
    public E last() {
        E last = null;
        for (E e : this)
            last = e;
        return last;
    }

    /**
     * Calculates the amount of elements in this stream
     *
     * @return the length of this stream
     */
    public int length() {
        return reduce(new Reducer<E, Integer>() {
            @Override
            public Integer reduce(Integer length, E element) {
                return length + 1;
            }
        }, 0);
    }

    /**
     * Alias for {@link #take(int)}
     *
     * @param number the number of items to take
     * @return a new stream containing only the first n elements of this stream
     */
    public Stream<E> limit(final int number) {
        return take(number);
    }

    /**
     * Filters out the elements that are of a certain class
     *
     * @param clazz the class that should be filtered out
     * @param <C>   the type of the class
     * @return a new stream containing only the elements of the provided class
     */
    public <C> Stream<C> ofClass(final Class<C> clazz) {
        return this.filter(new Filter<E>() {
            @Override
            public boolean apply(E e) {
                return clazz.isInstance(e);
            }
        }).cast(clazz);
    }

    /**
     * Maps each element of this stream to another value
     *
     * @param mapper the function that takes an element as its input and returns any other value
     * @param <R>    the type of the element after it has been mapped
     * @return a new stream containing the mapped elements
     */
    public <R> Stream<R> map(final Mapper<E, R> mapper) {
        if (mapper == null)
            throw new IllegalArgumentException("Unable to map this stream because the mapper is null!");
        return new MappedStream<E, R>(this, mapper);
    }

    /**
     * Reduces this stream to a single value by repeatedly applying the same reduction operator to the
     * current value and the next element.
     * For example, to reduce a stream of integers to a sum:
     * <pre>
     * {@code int sum = numbers.reduce(new Reducer<Integer, Integer>() {
     *          public Integer reduce(Integer sum, Integer number) {
     *              return sum + number;
     *          }
     *     }, 0)
     * }
     * </pre>
     *
     * @param reducer      the reduction function that turns the current value and the next element into the next value
     * @param initialValue the initial value to start from. This is also the value that will be returned when the stream is empty.
     * @param <R>          the type of the result of the reduced stream
     * @return the final value after reducing every element
     */
    public <R> R reduce(final Reducer<E, R> reducer, final R initialValue) {
        if (reducer == null)
            throw new IllegalArgumentException("Unable to reduce this stream because the reducer is null!");
        R accumulator = initialValue;
        for (E e : this) {
            accumulator = reducer.reduce(accumulator, e);
        }
        return accumulator;
    }

    /**
     * Skips a certain number of elements of this stream
     *
     * @param number the number of items to skip
     * @return a new stream containing the remaining elements of this stream after skipping a certain number of elements
     */
    public Stream<E> skip(final int number) {
        if (number < 0)
            throw new IllegalArgumentException("Unable to skip a number of elements of this stream because the number is negative!");
        return new SkipStream<E>(this, number);
    }

    /**
     * Determines whether any of the elements in this stream satisfy the given predicate
     *
     * @param filter the filter that returns true or false for any given element
     * @return true if one of the elements satisfied the predicate or false otherwise
     */
    public boolean some(final Filter<E> filter) {
        if (filter == null)
            throw new IllegalArgumentException("Unable to determine if some element satisfies this filter because the filter is null!");
        return this.filter(filter).iterator().hasNext();
    }

    /**
     * Sorts this stream using the provided comparator. This operator is lazy but greedy, meaning that it will wait as long as possible to actually materialize your stream
     * to sort it. Once you start iterating over the elements, it will sort just in time.
     * Note that multiple iterations will also a separate sort every time.
     *
     * @param comparator the comparator to use as the basis for the sorting
     * @return a new stream containing all elements of this stream in the order as specified by the comparator
     */
    public Stream<E> sort(final Comparator<E> comparator) {
        if (comparator == null) throw new IllegalArgumentException("Unable to sort stream, comparator cannot be null!");
        return new SortedStream<E>(this, comparator);
    }

    /**
     * Sorts this stream based on a property of each element, provided that that property implements Comparable.
     *
     * @param mapper the function that extracts a value from an element so it can be used as the basis for the comparison
     * @param <T>    the type of the property that is the basis for the comparison
     * @return a new stream containing all elements of this stream sorted by the given property
     */
    public <T extends Comparable<T>> Stream<E> sortBy(final Mapper<E, T> mapper) {
        if (mapper == null)
            throw new IllegalArgumentException("Unable to sort stream because the mapper is null!");
        return sort(new Comparator<E>() {
            @Override
            public int compare(E left, E right) {
                return mapper.map(left).compareTo(mapper.map(right));
            }
        });
    }

    /**
     * Sorts this stream descendingly based on a mapped value of each element, provided that that value implements Comparable.
     *
     * @param mapper the function that extracts a value from an element so it can be used as the basis for the comparison
     * @param <T>    the type of the property that is the basis for the comparison
     * @return a new stream containing all elements of this stream sorted by the given property
     */
    public <T extends Comparable<T>> Stream<E> sortByDescending(final Mapper<E, T> mapper) {
        if (mapper == null)
            throw new IllegalArgumentException("Unable to sort stream because the mapper is null!");
        return sort(new Comparator<E>() {
            @Override
            public int compare(E left, E right) {
                return mapper.map(right).compareTo(mapper.map(left));
            }
        });
    }

    /**
     * Takes a certain number of elements from this stream and drops the remaining elements
     *
     * @param number the number of items to take
     * @return a new stream containing only the first n elements of this stream
     */
    public Stream<E> take(final int number) {
        if (number < 0)
            throw new IllegalArgumentException("Unable to take a number of elements of this stream because the number is negative!");
        return new TakeStream<E>(this, number);
    }

    /**
     * Turns this stream into a list
     *
     * @return a new list containing all the elements of this stream
     */
    public List<E> toList() {
        return reduce(new Reducer<E, List<E>>() {
            @Override
            public List<E> reduce(List<E> list, E element) {
                list.add(element);
                return list;
            }
        }, new ArrayList<E>());
    }

    /**
     * Creates a {@link Map} from this stream.
     * Note that the map will only contain one element for each key. If two elements with the same key are encountered, only the last one is retained.
     * If you expect there to be scenarios where a key can be present multiple times, use {@link #groupBy(Mapper)} instead.
     * @param keyMapper the mapper function that computes a key for each element
     * @param <K> the type of the key for each entry in the map
     * @return a new map containing entries for each element (that had a unique key)
     */
    public <K> Map<K, E> toMap(final Mapper<E, K> keyMapper) {
        return toMap(keyMapper, new Mapper<E, E>() {
            @Override
            public E map(E e) {
                return e;
            }
        });
    }

    /**
     * Creates a {@link Map} from this stream.
     * Note that the map will only contain one element for each key. If two elements with the same key are encountered, only the last one is retained.
     * If you expect there to be scenarios where a key can be present multiple times, use {@link #groupBy(Mapper)} instead.
     * @param keyMapper the mapper function that computes a key for each element
     * @param valueMapper the mapper function that computes a value for each element
     * @param <K> the type of the key for each entry in the map
     * @param <V> the type of the value for each entry in the map
     * @return a new map containing entries for each element (that had a unique key)
     */
    public <K, V> Map<K, V> toMap(final Mapper<E, K> keyMapper, final Mapper<E, V> valueMapper) {
        if(keyMapper == null)
            throw new IllegalArgumentException("Cannot convert this stream to a Map because the keyMapper is null");
        return this.reduce(new Reducer<E, Map<K, V>>() {
            @Override
            public Map<K, V> reduce(Map<K, V> map, E e) {
                map.put(keyMapper.map(e), valueMapper.map(e));
                return map;
            }
        }, new HashMap<K, V>());
    }

    /**
     * Turns this stream into a set
     *
     * @return a new set containing the elements of this stream
     */
    public Set<E> toSet() {
        return reduce(new Reducer<E, Set<E>>() {
            @Override
            public Set<E> reduce(Set<E> set, E element) {
                set.add(element);
                return set;
            }
        }, new HashSet<E>());
    }

    /**
     * Filters out elements from this stream based on the elements from another.
     * Only elements that are NOT in the other stream are allowed to pass through.
     *
     * @param other the stream containing elements that are forbidden to pass through
     * @return a new stream containing only elements that cannot be found in the other stream
     */
    public Stream<E> without(final Stream<E> other) {
        if (other == null) throw new IllegalArgumentException("The argument 'other' cannot be null!");
        return new WithoutStream<E>(this, other);
    }
}
