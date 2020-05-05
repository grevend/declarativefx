/*
 * MIT License
 *
 * Copyright (c) 2020 David Greven
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package grevend.declarativefx.bindable;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class BindableCollection<E> implements Collection<E>, Bindable {

    private final Collection<E> collection;
    private final Collection<BiConsumer<Change, Collection<? extends E>>> consumers;

    @Contract(pure = true)
    private BindableCollection(@NotNull Collection<E> collection) {
        this.collection = collection;
        this.consumers = new ArrayList<>();
    }

    @Contract(" -> new")
    public static @NotNull <E> BindableCollection<E> empty() {
        return new BindableCollection<>(new ArrayList<>());
    }

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull <E> BindableCollection<E> of(@NotNull Collection<E> collection) {
        return new BindableCollection<>(collection);
    }

    @Contract("_, _ -> new")
    public static @NotNull <E> BindableCollection<E> of(@NotNull Collection<E> collection, @NotNull Predicate<E> filter) {
        return new BindableCollection<>(collection.stream().filter(filter).collect(Collectors.toList()));
    }

    @SafeVarargs
    @Contract("_ -> new")
    public static @NotNull <E> BindableCollection<E> of(@Nullable E... collection) {
        return new BindableCollection<>(
            collection == null ? List.of() : Arrays.stream(collection).collect(Collectors.toList()));
    }

    public void subscribe(@NotNull BiConsumer<Change, Collection<? extends E>> consumer) {
        this.consumers.add(consumer);
    }

    public void unsubscribe(@NotNull BiConsumer<Change, Collection<? extends E>> consumer) {
        this.consumers.remove(consumer);
    }

    public @NotNull Collection<E> getCollection() {
        return this.collection;
    }

    public @NotNull Collection<BiConsumer<Change, Collection<? extends E>>> getConsumers() {
        return this.consumers;
    }

    @Override
    public int size() {
        return this.collection.size();
    }

    @Override
    @Contract(pure = true)
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    @Override
    @Contract(pure = true)
    public boolean contains(@Nullable Object o) {
        return this.collection.contains(o);
    }

    @Override
    public @NotNull Iterator<E> iterator() {
        return this.collection.iterator();
    }

    @Override
    @Flow(source = "this.collection", sourceIsContainer = true, targetIsContainer = true)
    public @NotNull Object[] toArray() {
        return this.collection.toArray();
    }

    @Override
    @Flow(source = "this.collection", sourceIsContainer = true, targetIsContainer = true)
    public @NotNull <T> T[] toArray(@NotNull T[] a) {
        return this.collection.toArray(a);
    }

    @Override
    public boolean add(@Nullable E e) {
        var res = this.collection.add(e);
        this.consumers.forEach(consumer -> consumer.accept(Change.ADD, e == null ? null : List.of(e)));
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(@Nullable Object o) {
        var res = this.collection.remove(o);
        if (this.collection.contains(o)) {
            if (o == null) {
                this.consumers.forEach(consumer -> consumer.accept(Change.REMOVE, null));
            } else {
                var elements = List.of((E) o);
                this.consumers.forEach(consumer -> consumer.accept(Change.REMOVE, elements));
            }
        }
        return res;
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.collection.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        var res = this.collection.addAll(c);
        consumers.forEach(consumer -> consumer.accept(Change.ADD, c));
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(@NotNull Collection<?> c) {
        var res = this.collection.removeAll(c);
        consumers.forEach(consumer -> consumer.accept(Change.REMOVE, (Collection<E>) c));
        return res;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        var elements = new ArrayList<>(this.collection);
        elements.removeAll(c);
        var res = this.collection.retainAll(c);
        consumers.forEach(consumer -> consumer.accept(Change.REMOVE, elements));
        return res;
    }

    @Override
    public void clear() {
        var elements = new ArrayList<>(this.collection);
        this.collection.clear();
        consumers.forEach(consumer -> consumer.accept(Change.REMOVE, elements));
    }

    @NotNull
    public ObservableList<E> toObservableList() {
        return FXCollections.observableArrayList(this);
    }

    @NotNull
    @Override
    public String toString() {
        return "BindableCollection{collection=" + collection + '}';
    }

}
