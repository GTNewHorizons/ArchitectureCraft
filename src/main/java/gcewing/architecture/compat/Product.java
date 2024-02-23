package gcewing.architecture.compat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.common.collect.UnmodifiableIterator;

class Product<T> implements Iterable<T[]> {

    private final Class<T> clazz;
    private final Iterable<? extends T>[] iterables;

    Product(Class<T> clazz, Iterable<? extends T>[] iterables) {
        this.clazz = clazz;
        this.iterables = iterables;
    }

    public Iterator<T[]> iterator() {
        return (this.iterables.length <= 0 ? Collections.singletonList(Cartesian.createArray(this.clazz, 0)).iterator()
                : new ProductIterator(this.clazz, this.iterables));
    }

    static class ProductIterator<T> extends UnmodifiableIterator<T[]> {

        private int index;
        private final Iterable<? extends T>[] iterables;
        private final Iterator<? extends T>[] iterators;
        /** Array used as the result of next() */
        private final T[] results;

        private ProductIterator(Class<T> clazz, Iterable<? extends T>[] iterables) {
            this.index = -2;
            this.iterables = iterables;
            this.iterators = Cartesian.createArray(Iterator.class, this.iterables.length);

            for (int i = 0; i < this.iterables.length; ++i) {
                this.iterators[i] = iterables[i].iterator();
            }

            this.results = Cartesian.createArray(clazz, this.iterators.length);
        }

        /**
         * Called when no more data is available in this Iterator.
         */
        private void endOfData() {
            this.index = -1;
            Arrays.fill(this.iterators, null);
            Arrays.fill(this.results, null);
        }

        public boolean hasNext() {
            if (this.index == -2) {
                this.index = 0;

                for (Iterator<? extends T> iterator1 : this.iterators) {
                    if (!iterator1.hasNext()) {
                        this.endOfData();
                        break;
                    }
                }

                return true;
            } else {
                if (this.index >= this.iterators.length) {
                    for (this.index = this.iterators.length - 1; this.index >= 0; --this.index) {
                        Iterator<? extends T> iterator = this.iterators[this.index];

                        if (iterator.hasNext()) {
                            break;
                        }

                        if (this.index == 0) {
                            this.endOfData();
                            break;
                        }

                        iterator = this.iterables[this.index].iterator();
                        this.iterators[this.index] = iterator;

                        if (!iterator.hasNext()) {
                            this.endOfData();
                            break;
                        }
                    }
                }

                return this.index >= 0;
            }
        }

        public T[] next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            } else {
                while (this.index < this.iterators.length) {
                    this.results[this.index] = this.iterators[this.index].next();
                    ++this.index;
                }

                return this.results.clone();
            }
        }
    }
}
