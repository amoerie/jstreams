package com.amoerie.jstreams;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class TestModels {
    public static class Apple extends Fruit {
        public Apple() {
            super("apple");
        }
    }

    public static class Fruit {
        private final String name;

        public Fruit(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Fruit fruit = (Fruit) o;

            return !(name != null ? !name.equals(fruit.name) : fruit.name != null);

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Fruit{" +
                    "name='" + name + '\'' +
                    '}';
        }
    }

    public static class FruitBasket {
        private final List<Fruit> fruitList;

        public FruitBasket(List<Fruit> fruitList) {
            this.fruitList = fruitList;
        }

        public List<Fruit> getFruitList() {
            return fruitList;
        }

        @Override
        public String toString() {
            return "FruitBasket{" +
                    "fruitList=" + fruitList +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            FruitBasket basket = (FruitBasket) o;

            return !(fruitList != null ? !fruitList.equals(basket.fruitList) : basket.fruitList != null);

        }

        @Override
        public int hashCode() {
            return fruitList != null ? fruitList.hashCode() : 0;
        }
    }

    public static FruitBasket makeFruitBasket(Fruit...fruits) {
        return fruits == null
                ? new FruitBasket(Collections.<Fruit>emptyList())
                : new FruitBasket(Arrays.asList(fruits));
    }
}
