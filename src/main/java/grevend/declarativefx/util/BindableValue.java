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

public class BindableValue {

    private final List<Consumer<Object>> consumers;
    private Object value, defaultValue;

    public BindableValue() {
        this(null);
    }

    public BindableValue(@Nullable Object value) {
        this.consumers = new ArrayList<>();
        this.value = value;
    }

    public void subscribe(@NotNull Consumer<Object> consumer) {
        this.consumers.add(consumer);
        consumer.accept(this.get());
    }

    public void unsubscribe(@NotNull Consumer<Object> consumer) {
        this.consumers.remove(consumer);
    }

    public @Nullable Object get() {
        return this.value == null ? this.defaultValue : this.value;
    }

    public @Nullable Object getDefault() {
        return defaultValue;
    }

    public void set(@Nullable Object value) {
        this.value = value;
        this.consumers.forEach(consumer -> consumer.accept(value));
    }

    public void update(@NotNull UnaryOperator<Object> function) {
        this.set(function.apply(this.get()));
    }

    public @NotNull Object get(@NotNull Object defaultValue) {
        return this.value == null ? defaultValue : this.value;
    }

    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public @NotNull BindableValue orElse(@NotNull Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public @NotNull BindableValue orElse(@NotNull Supplier<Object> supplier) {
        return this.orElse(supplier.get());
    }

    public @NotNull BindableValue compute(@NotNull BindableValue dependency,
                                          @NotNull BiFunction<BindableValue, BindableValue, Object> function) {
        dependency.subscribe((value) -> set(function.apply(dependency, this)));
        return this;
    }

    public @NotNull BindableValue compute(@NotNull BindableValue dependency,
                                          @NotNull Function<BindableValue, Object> function) {
        dependency.subscribe((value) -> set(function.apply(this)));
        return this;
    }

    public @NotNull BindableValue compute(@NotNull BindableValue dependency,
                                          @NotNull Supplier<Object> supplier) {
        dependency.subscribe((value) -> set(supplier.get()));
        return this;
    }

    public @NotNull List<Consumer<Object>> getConsumers() {
        return consumers;
    }

    @Override
    public @NotNull String toString() {
        return "BindableValue{" +
            "value=" + value +
            '}';
    }

}
