package com.amoerie.streams;

import com.amoerie.streams.TestModels.*;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.amoerie.streams.TestModels.makeFruitBasket;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class TestsForStream
{
    public static class TestsForEmpty {

        @Test
        public void shouldCreateEmptyStream() {
            assertThat(Stream.<Apple>empty().toList(), is(Collections.<Apple>emptyList()));
        }

    }

    public static class TestsForSingleton {
        @Test
        public void shouldCreateStreamWithOneElement() {
            Apple element = new Apple();
            assertThat(Stream.singleton(element).toSet(), is(Collections.singleton(element)));
        }
    }

    public static class TestsForCast
    {
        @Test
        public void shouldCastEveryFruitToAnApple() {
            TestModels.Fruit[] fruits = { new Apple(), new Apple() };
            FruitBasket fruitBasket = makeFruitBasket(fruits);
            Iterable<Apple> apples = Stream.create(fruitBasket.getFruitList()).cast(Apple.class).toList();
            assertThat(apples, everyItem(CoreMatchers.<Apple>instanceOf(Apple.class)));
        }

        @Test
        public void shouldCastEmptyStreamToEmptyStream() {
            Stream<Fruit> fruits = Stream.empty();
            Stream<Apple> apples = fruits.cast(Apple.class);
            assertThat(apples.toList(), is(Stream.<Apple>empty().toList()));
        }
    }

    public static class TestsForConcat {

        @Test
        public void shouldReturnAStreamContainingElementsOfBothStreams() {
            Apple apple1 = new Apple();
            Apple apple2 = new Apple();
            Fruit pear = new Fruit("pear");
            Stream<Fruit> apples = Stream.create(makeFruitBasket(apple1, apple2).getFruitList());
            Stream<Fruit> otherFruits = Stream.create(makeFruitBasket(pear).getFruitList());
            Stream<Fruit> allFruits = apples.concat(otherFruits);
            List<Fruit> allFruitsList = allFruits.toList();
            assertThat(allFruitsList.size(), is(3));
            assertThat(allFruitsList, hasItem(apple1));
            assertThat(allFruitsList, hasItem(apple2));
            assertThat(allFruitsList, hasItem(pear));
        }

    }

    public static class TestsForFilter {
        private static final List<Fruit> fruitList = Arrays.asList(new Fruit("banana"),
                new Fruit("apple"),
                new Fruit("pear"),
                new Fruit("pineapple"),
                new Fruit("strawberry"),
                new Fruit("grapes"),
                new Fruit("kiwi"));

        private static final Stream<Fruit> fruitBasket = Stream.create(fruitList);

        @Test
        public void noFilterShouldReturnAllFruits() {
            List<Fruit> allFruits  = fruitBasket.filter(new Func1<Fruit, Boolean>() {
                @Override
                public Boolean call(Fruit fruit) {
                    return true;
                }
            }).toList();
            assertThat(allFruits, is(fruitList));
        }

        @Test
        public void onlyFruitsThatStartWithPShouldReturn2Fruits() {
            List<Fruit> pFruits = fruitBasket.filter(new Func1<Fruit, Boolean>() {
                @Override
                public Boolean call(Fruit fruit) {
                    return fruit.getName().startsWith("p");
                }
            }).toList();
            assertThat(pFruits, is(Arrays.asList(new Fruit[]{
                    new Fruit("pear"),
                    new Fruit("pineapple"),
            })));
        }

        @Test
        public void filterThatAlwaysReturnsFalseShouldReturnNoFruits() {
            List<Fruit> fruits = fruitBasket.filter(new Func1<Fruit, Boolean>() {
                @Override
                public Boolean call(Fruit fruit) {
                    return false;
                }
            }).toList();
            assertThat(fruits, is(Collections.<Fruit>emptyList()));
        }
    }

    public static class TestsForFlatMap {
        @Test
        public void anEmptyFruitBasketShouldFlatMapToNoFruits() {
            List<Fruit> fruits = Stream.create(Collections.singletonList(makeFruitBasket()))
                    .flatMap(new Func1<FruitBasket, Stream<Fruit>>() {
                        @Override
                        public Stream<Fruit> call(FruitBasket fruitBasket) {
                            return Stream.create(fruitBasket.getFruitList());
                        }
                    })
                    .toList();
            assertThat(fruits, is(Collections.<Fruit>emptyList()));
        }

        @Test
        public void oneFruitBasketShouldFlatMapToAllFruitsInTheBasket() {
            List<FruitBasket> allBaskets = Collections.singletonList(makeFruitBasket(new Fruit("apple"), new Fruit("pear")));
            List<Fruit> allFruits = Stream.create(allBaskets).flatMap(new Func1<FruitBasket, Stream<Fruit>>() {
                @Override
                public Stream<Fruit> call(FruitBasket fruitBasket) {
                    return Stream.create(fruitBasket.getFruitList());
                }
            }).toList();
            assertThat(allFruits, is(makeFruitBasket(new Fruit("apple"), new Fruit("pear")).getFruitList()));
        }
        @Test
        public void twoFruitBasketsShouldFlatMapToTheFruitsInBothBaskets() {
            List<FruitBasket> allBaskets = Arrays.asList(
                    makeFruitBasket(new Fruit("apple"), new Fruit("pear")),
                    makeFruitBasket(new Fruit("strawberries")));
            List<Fruit> allFruits = Stream.create(allBaskets).flatMap(new Func1<FruitBasket, Stream<Fruit>>() {
                @Override
                public Stream<Fruit> call(FruitBasket fruitBasket) {
                    return Stream.create(fruitBasket.getFruitList());
                }
            }).toList();
            assertThat(allFruits, is(makeFruitBasket(new Fruit("apple"), new Fruit("pear"), new Fruit("strawberries")).getFruitList()));
        }

        @Test
        public void threeFruitBasketsWithOneEmptyShouldStillMapToAllTheFruits() {
            List<FruitBasket> allBaskets = Arrays.asList(
                    makeFruitBasket(new Fruit("apple"), new Fruit("pear")),
                    makeFruitBasket(),
                    makeFruitBasket(new Fruit("strawberries")));
            List<Fruit> allFruits = Stream.create(allBaskets).flatMap(new Func1<FruitBasket, Stream<Fruit>>() {
                @Override
                public Stream<Fruit> call(FruitBasket fruitBasket) {
                    return Stream.create(fruitBasket.getFruitList());
                }
            }).toList();
            assertThat(allFruits, is(makeFruitBasket(new Fruit("apple"), new Fruit("pear"), new Fruit("strawberries")).getFruitList()));
        }
    }

    public static class TestsForMap {
        @Test
        public void anEmptyFruitBasketShouldMapToNoFruitNames() {
            List<String> fruitNames = Stream.create(new ArrayList<Fruit>())
                    .map(new Func1<Fruit, String>() {
                        @Override
                        public String call(Fruit fruit) {
                            return fruit.getName();
                        }
                    })
                    .toList();
            assertThat(fruitNames, is(Collections.<String>emptyList()));
        }

        @Test
        public void aFilledFruitBasketShouldMapToTheirNames() {
            List<String> fruitNames = Stream.create(makeFruitBasket(new Fruit("apple"), new Fruit("pear")).getFruitList())
                    .map(new Func1<Fruit, String>() {
                        @Override
                        public String call(Fruit fruit) {
                            return fruit.getName();
                        }
                    })
                    .toList();
            assertThat(fruitNames, is(Arrays.asList(new String[] { "apple", "pear" })));
        }
    }

    public static class TestsForSortBy {

        @Test
        public void shouldSortByNameCorrectly() {
            List<Fruit> fruits = Arrays.asList(new Fruit("orange"), new Fruit("banana"), new Fruit("pear"));
            List<Fruit> expectedSortedFruits = Arrays.asList(new Fruit("banana"), new Fruit("orange"), new Fruit("pear"));
            List<Fruit> sortedFruits = Stream.create(fruits).sortBy(new Func1<Fruit, Comparable>() {
                @Override
                public Comparable call(Fruit fruit) {
                    return fruit.getName();
                }
            }).toList();
            assertThat(sortedFruits, is(expectedSortedFruits));
        }

    }
}