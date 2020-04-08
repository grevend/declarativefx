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

package grevend.declarativefx.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.intellij.lang.annotations.Flow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class BindableCollection<E> implements Collection<E> {

    private final Collection<E> collection;
    private final Collection<BiConsumer<CollectionChange, Collection<? extends E>>> consumers;

    private BindableCollection(@NotNull Collection<E> collection) {
        this.collection = collection;
        this.consumers = new ArrayList<>();
    }

    public static @NotNull <E> BindableCollection<E> of(@NotNull Collection<E> collection) {
        return new BindableCollection<>(collection);
    }

    @SafeVarargs
    public static @NotNull <E> BindableCollection<E> of(@NotNull E... collection) {
        return new BindableCollection<>(Arrays.stream(collection).collect(Collectors.toList()));
    }

    public void subscribe(@NotNull BiConsumer<CollectionChange, Collection<? extends E>> consumer) {
        this.consumers.add(consumer);
    }

    public void unsubscribe(@NotNull BiConsumer<CollectionChange, Collection<? extends E>> consumer) {
        this.consumers.remove(consumer);
    }

    public @NotNull Collection<E> getCollection() {
        return this.collection;
    }

    public @NotNull Collection<BiConsumer<CollectionChange, Collection<? extends E>>> getConsumers() {
        return this.consumers;
    }

    @Override
    public int size() {
        return this.collection.size();
    }

    @Override
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    @Override
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
        this.consumers.forEach(consumer -> consumer.accept(CollectionChange.ADD, e == null ? null : List.of(e)));
        return this.collection.add(e);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(@Nullable Object o) {
        if (this.collection.contains(o)) {
            if (o == null) {
                this.consumers.forEach(consumer -> consumer.accept(CollectionChange.REMOVE, null));
            } else {
                var elements = List.of((E) o);
                this.consumers.forEach(consumer -> consumer.accept(CollectionChange.REMOVE, elements));
            }
        }
        return this.collection.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return this.collection.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends E> c) {
        consumers.forEach(consumer -> consumer.accept(CollectionChange.ADD, c));
        return this.collection.addAll(c);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean removeAll(@NotNull Collection<?> c) {
        var res = this.collection.removeAll(c);
        consumers.forEach(consumer -> consumer.accept(CollectionChange.REMOVE, (Collection<E>) c));
        return res;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        var elements = new ArrayList<>(this.collection);
        elements.removeAll(c);
        consumers.forEach(consumer -> consumer.accept(CollectionChange.REMOVE, elements));
        return this.collection.retainAll(c);
    }

    @Override
    public void clear() {
        consumers.forEach(consumer -> consumer.accept(CollectionChange.REMOVE, this.collection));
        this.collection.clear();
    }

    public @NotNull ObservableList<E> toObservableList() {
        return FXCollections.observableArrayList(this);
    }

    @Override
    public String toString() {
        return "BindableCollection{" +
            "collection=" + collection +
            '}';
    }

}
