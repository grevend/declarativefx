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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class BindableValue<V> {

    private final List<Consumer<V>> consumers;
    private V value, defaultValue;

    public BindableValue() {
        this(null);
    }

    public BindableValue(@Nullable V value) {
        this.consumers = new ArrayList<>();
        this.value = value;
    }

    public void subscribe(@NotNull Consumer<V> consumer) {
        this.consumers.add(consumer);
        consumer.accept(this.get());
    }

    public void unsubscribe(@NotNull Consumer<V> consumer) {
        this.consumers.remove(consumer);
    }

    public @Nullable V get() {
        return this.value == null ? this.defaultValue : this.value;
    }

    public void set(@Nullable V value) {
        this.value = value;
        this.consumers.forEach(consumer -> consumer.accept(value));
    }

    public void update(@NotNull UnaryOperator<V> function) {
        this.set(function.apply(this.get()));
    }

    public @NotNull V get(@NotNull V defaultValue) {
        return this.value == null ? defaultValue : this.value;
    }

    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public @NotNull BindableValue<V> orElse(@NotNull V defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public @NotNull BindableValue<V> orElse(@NotNull Supplier<V> supplier) {
        return this.orElse(supplier.get());
    }

    public @NotNull <U> BindableValue<V> compute(@NotNull BindableValue<U> dependency,
                                                 @NotNull BiFunction<BindableValue<U>, BindableValue<V>, V> function) {
        dependency.subscribe((value) -> set(function.apply(dependency, this)));
        return this;
    }

    public @NotNull <U> BindableValue<V> compute(@NotNull BindableValue<U> dependency,
                                                 @NotNull Function<BindableValue<V>, V> function) {
        dependency.subscribe((value) -> set(function.apply(this)));
        return this;
    }

    public @NotNull <U> BindableValue<V> compute(@NotNull BindableValue<U> dependency,
                                                 @NotNull Supplier<V> supplier) {
        dependency.subscribe((value) -> set(supplier.get()));
        return this;
    }

    public @NotNull List<Consumer<V>> getConsumers() {
        return consumers;
    }

    @Override
    public @NotNull String toString() {
        return "BindableValue{" +
            "value=" + value +
            '}';
    }

}
