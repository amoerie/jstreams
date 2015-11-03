# JStreams 1.1

[![Build Status](https://travis-ci.org/amoerie/jstreams.svg)](https://travis-ci.org/amoerie/jstreams)

Contains Java 6 compatible streams that are immutable, lazy and chainable.

- For the C# developers: This library looks A LOT like Linq To Objects, the only difference being that the method names are more conventional ('filter' instead of 'Where', etc.).

- For the javascript developers: This library looks A LOT like lodash, underscore, ... 

*Important features*

1. Streams are immutable. Once a stream has been created, you cannot change it. The only thing you can do is make new streams starting from that stream. 
2. Streams are lazy. Enumerating the elements of a stream is always postponed as much as possible. There are some methods which absolutely require iteration, which I call "greedy operators". Some examples of greedy operators are `sortBy`, `length`, `toList` and `last`. Most operators are lazy, these are delayed until a greedy operator is called.
For example, you can do the following:
```java
      List<String> firstFiveStrings = Stream.create(infiniteStrings).take(5).toList();
```

3. Stream operators are chainable, allowing you to do complex things with simple compositions.

## Why did you make this, I thought Java had streams now?

The reasons I created this are:

- Android does not run Java 8. Even with Retrolambda, which introduces the nice arrow syntax, you still don't get the Streams because those are only included in the actual JDK 8.
- Other libraries that attempt this sort of thing are either outdated, not well documented or are very bloated.

This library is explicitly called JStreams to accentuate how it's mostly the same thing as Java 8 streams. If you can use JDK 8, you probably should not use this.

## How do I use this

You can use Maven! Add this to your `<dependencies>` in your pom.xml:

```xml
      <dependency>
            <groupId>com.amoerie</groupId>
            <artifactId>jstreams</artifactId>
            <version>1.0</version>
      </dependency>
```

Or if you use Gradle, add this to your `build.gradle` file 

```groovy
    compile 'com.amoerie:jstreams:1.0'
```

Or you can just download the latest jar file from [the releases page](https://github.com/amoerie/jstreams/releases)!

## Roadmap

- 1.1 will be released beginning of November 2015, fixing a few bugs, adding a couple of operators such as `distinct`, `toMap` and adding some aliases.

## Pull requests

I am taking pull requests! This library is quite minimal, so I expect there to be times when the API is a bit too limited to satisfy all your needs. 

#### Getting started

1. Import this project into your IDE of choice as a Gradle project. There is no other special setup you need to do.

#### Guidelines 

If you want to make sure your pull request gets accepted, follow these guidelines:

- Follow functional conventions. If you add a method that does not exist in any other language or framework, rethink what you're doing. If it does exist elsewhere, find the name that is most commonly used across languages/frameworks.

- Write at least 3 unit tests covering the empty stream scenario, a "happy case" scenario and an infinite stream scenario (if applicable). See the existing tests. 

- Write javadoc for the public parts of the API.
 
Other tips:

- Try to reuse existing operators. For example, `toList` and `length`are just specialized use cases of `reduce`
- Try to avoid writing code inside the Stream base class. See the private `FlatStream`, `MappedStream`, ... classes if you need an example.

## Documentation

### `public abstract class Stream<E> implements Iterable<E>`

Represents a collection of elements that are not known at construction time This is a wrapper around the Iterator class, providing more functional methods than is standard provided by Java, by combining some classic functional paradigms such as map and flatMap.

 * **Parameters:** `<E>` — the type of each element in the stream

### `public static <E> Stream<E> create(final E... elements)`

Creates a new stream from the provided array of elements

 * **Parameters:**
   * `elements` — the array of elements
   * `<E>` — the type of the elements
 * **Returns:** a new stream containing the elements of the array

### `public static <E> Stream<E> create(final Iterable<E> elements)`

Creates a new stream from the provided iterable This is a lazy operation, it does not consume the iterable until a greedy operation is called, such as toList()

 * **Parameters:**
   * `elements` — an iterable containing elements
   * `<E>` — the type of an element
 * **Returns:** a new stream containing the elements of the iterable

### `public static <E> Stream<E> empty()`

Creates a new empty stream, containing no elements

 * **Parameters:** `<E>` — the type of the elements of this stream
 * **Returns:** a new empty stream containing no elements

### `public static <E> Stream<E> of(final E... elements)`

Alias for {@link #create(Object[])}

 * **Parameters:**
   * `elements` — the array of elements
   * `<E>` — the type of the elements
 * **Returns:** a new stream containing the elements of the array

### `public static <E> Stream<E> of(Iterable<E> elements)`

Alias for {@link #create(Iterable)}

 * **Parameters:**
   * `elements` — an iterable containing elements
   * `<E>` — the type of an element
 * **Returns:** a new stream containing the elements of the iterable

### `public static <E> Stream<E> singleton(final E element)`

Creates a new singleton stream, containing exactly one element

 * **Parameters:**
   * `element` — the single element
   * `<E>` — the type of the single element
 * **Returns:** a new stream containing exactly one element

### `public boolean any(final Filter<E> filter)`

Alias for {@link #some(Filter)}

 * **Parameters:** `filter` — the filter that returns true or false for any given element
 * **Returns:** true if one of the elements satisfied the predicate or false otherwise

### `public <C> Stream<C> cast(final Class<C> clazz)`

Casts every element of this stream to another class

 * **Parameters:**
   * `clazz` — the class to cast to
   * `<C>` — the type of the class to cast to
 * **Returns:** a new stream containing every element casted to another class

### `@SuppressWarnings("unchecked") public Stream<E> concat(final Stream<E> other)`

Concatenates this stream with another stream

 * **Parameters:** `other` — the other stream to concatenate with
 * **Returns:** a new stream containing all the elements of this stream and the other stream

### `public Stream<E> defaultIfEmpty(final E defaultElement)`

Adds a default element to this stream if and only if it is empty

 * **Parameters:** `defaultElement` — the default element to use when the stream is empty
 * **Returns:** a new stream containing either all the elements of the current stream or a singleton stream with only the default element

### `public Stream<E> distinct()`

Filters this stream to only have unique elements.

 * **Returns:** a new stream containing only unique elements.

### `public Stream<E> filter(final Filter<E> filter)`

Filters the elements of this stream with the given filter.

 * **Parameters:** `filter` — the predicate that returns true or false for a given element
 * **Returns:** a new stream containing only the elements that satisfied the filter

### `public E first()`

Gets the first element of this stream

 * **Returns:** the first element of this stream or null if the stream is empty

### `public void forEach(final Consumer<E> consumer)`

Iterates over all the elements of this stream and feeds them one by one to the provided {@code consumer}

 * **Parameters:** `consumer` — the function to be executed for each element of the stream

### `public <R> Stream<R> flatMap(final Mapper<E, Stream<R>> mapper)`

Maps each element of this stream to a separate stream, and then flattens the result to one single stream

 * **Parameters:**
   * `mapper` — the function that turns one element into a stream of values
   * `<R>` — the type of one mapped element
 * **Returns:** a new stream containing all elements of all the jstreams the mapper function created

### `public <K> Stream<Group<K, E>> groupBy(final Mapper<E, K> keyMapper)`

Groups this stream into chunks based on the key per element that is retrieved via the keySelector

 * **Parameters:**
   * `keyMapper` — a function that returns the grouping key for a given element
   * `<K>` — the type of the key
 * **Returns:** a stream containing groups as its elements

### `public String join(final String delimiter)`

Joins the stream using the given delimiter

 * **Parameters:** `delimiter` — the delimiter to be inserted between each element
 * **Returns:** a string containing all of the elements with the given delimiter between each element

### `public E last()`

Gets the last element of this stream

 * **Returns:** the last element of this stream or null if the stream is empty

### `public int length()`

Calculates the amount of elements in this stream

 * **Returns:** the length of this stream

### `public Stream<E> limit(final int number)`

Alias for {@link #take(int)}

 * **Parameters:** `number` — the number of items to take
 * **Returns:** a new stream containing only the first n elements of this stream

### `public <C> Stream<C> ofClass(final Class<C> clazz)`

Filters out the elements that are of a certain class

 * **Parameters:**
   * `clazz` — the class that should be filtered out
   * `<C>` — the type of the class
 * **Returns:** a new stream containing only the elements of the provided class

### `public <R> Stream<R> map(final Mapper<E, R> mapper)`

Maps each element of this stream to another value

 * **Parameters:**
   * `mapper` — the function that takes an element as its input and returns any other value
   * `<R>` — the type of the element after it has been mapped
 * **Returns:** a new stream containing the mapped elements

### `public <R> R reduce(final Reducer<E, R> reducer, final R initialValue)`

Reduces this stream to a single value by repeatedly applying the same reduction operator to the current value and the next element. For example, to reduce a stream of integers to a sum: <pre> {@code int sum = numbers.reduce(new Reducer<Integer, Integer>() { public Integer reduce(Integer sum, Integer number) { return sum + number; } }, 0) } </pre>

 * **Parameters:**
   * `reducer` — the reduction function that turns the current value and the next element into the next value
   * `initialValue` — the initial value to start from. This is also the value that will be returned when the stream is empty.
   * `<R>` — the type of the result of the reduced stream
 * **Returns:** the final value after reducing every element

### `public Stream<E> skip(final int number)`

Skips a certain number of elements of this stream

 * **Parameters:** `number` — the number of items to skip
 * **Returns:** a new stream containing the remaining elements of this stream after skipping a certain number of elements

### `public boolean some(final Filter<E> filter)`

Determines whether any of the elements in this stream satisfy the given predicate

 * **Parameters:** `filter` — the filter that returns true or false for any given element
 * **Returns:** true if one of the elements satisfied the predicate or false otherwise

### `public Stream<E> sort(final Comparator<E> comparator)`

Sorts this stream using the provided comparator. This operator is lazy but greedy, meaning that it will wait as long as possible to actually materialize your stream to sort it. Once you start iterating over the elements, it will sort just in time. Note that multiple iterations will also a separate sort every time.

 * **Parameters:** `comparator` — the comparator to use as the basis for the sorting
 * **Returns:** a new stream containing all elements of this stream in the order as specified by the comparator

### `public <T extends Comparable<T>> Stream<E> sortBy(final Mapper<E, T> mapper)`

Sorts this stream based on a property of each element, provided that that property implements Comparable.

 * **Parameters:**
   * `mapper` — the function that extracts a value from an element so it can be used as the basis for the comparison
   * `<T>` — the type of the property that is the basis for the comparison
 * **Returns:** a new stream containing all elements of this stream sorted by the given property

### `public <T extends Comparable<T>> Stream<E> sortByDescending(final Mapper<E, T> mapper)`

Sorts this stream descendingly based on a mapped value of each element, provided that that value implements Comparable.

 * **Parameters:**
   * `mapper` — the function that extracts a value from an element so it can be used as the basis for the comparison
   * `<T>` — the type of the property that is the basis for the comparison
 * **Returns:** a new stream containing all elements of this stream sorted by the given property

### `public Stream<E> take(final int number)`

Takes a certain number of elements from this stream and drops the remaining elements

 * **Parameters:** `number` — the number of items to take
 * **Returns:** a new stream containing only the first n elements of this stream

### `public List<E> toList()`

Turns this stream into a list

 * **Returns:** a new list containing all the elements of this stream

### `public <K> Map<K, E> toMap(final Mapper<E, K> keyMapper)`

Creates a {@link Map} from this stream. Note that the map will only contain one element for each key. If two elements with the same key are encountered, only the last one is retained. If you expect there to be scenarios where a key can be present multiple times, use {@link #groupBy(Mapper)} instead.

 * **Parameters:**
   * `keyMapper` — the mapper function that computes a key for each element
   * `<K>` — the type of the key for each entry in the map
 * **Returns:** a new map containing entries for each element (that had a unique key)

### `public <K, V> Map<K, V> toMap(final Mapper<E, K> keyMapper, final Mapper<E, V> valueMapper)`

Creates a {@link Map} from this stream. Note that the map will only contain one element for each key. If two elements with the same key are encountered, only the last one is retained. If you expect there to be scenarios where a key can be present multiple times, use {@link #groupBy(Mapper)} instead.

 * **Parameters:**
   * `keyMapper` — the mapper function that computes a key for each element
   * `valueMapper` — the mapper function that computes a value for each element
   * `<K>` — the type of the key for each entry in the map
   * `<V>` — the type of the value for each entry in the map
 * **Returns:** a new map containing entries for each element (that had a unique key)

### `public Set<E> toSet()`

Turns this stream into a set

 * **Returns:** a new set containing the elements of this stream

### `public Stream<E> without(final Stream<E> other)`

Filters out elements from this stream based on the elements from another. Only elements that are NOT in the other stream are allowed to pass through.

 * **Parameters:** `other` — the stream containing elements that are forbidden to pass through
 * **Returns:** a new stream containing only elements that cannot be found in the other stream
