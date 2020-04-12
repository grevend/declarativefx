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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class BindableValue implements Bindable {

    private final List<Consumer<Object>> consumers;
    private Object value, defaultValue;

    @Contract(pure = true)
    public BindableValue() {
        this(null);
    }

    @Contract(pure = true)
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

    @Nullable
    public Object get() {
        return this.value == null ? this.defaultValue : this.value;
    }

    @Nullable
    public Object getDefault() {
        return defaultValue;
    }

    public void set(@Nullable Object value) {
        this.value = value;
        this.consumers.forEach(consumer -> consumer.accept(value));
    }

    public void update(@NotNull UnaryOperator<Object> function) {
        this.set(function.apply(this.get()));
    }

    @NotNull
    public Object get(@NotNull Object defaultValue) {
        return this.value == null ? defaultValue : this.value;
    }

    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    @NotNull
    public BindableValue orElse(@NotNull Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    @NotNull
    public BindableValue orElse(@NotNull Supplier<Object> supplier) {
        return this.orElse(supplier.get());
    }

    @NotNull
    public BindableValue compute(@NotNull BindableValue dependency, @NotNull BiFunction<BindableValue, BindableValue, Object> function) {
        dependency.subscribe((value) -> set(function.apply(dependency, this)));
        return this;
    }

    @NotNull
    public BindableValue compute(@NotNull BindableValue dependency, @NotNull Function<BindableValue, Object> function) {
        dependency.subscribe((value) -> set(function.apply(this)));
        return this;
    }

    @NotNull
    public BindableValue compute(@NotNull BindableValue dependency, @NotNull Supplier<Object> supplier) {
        dependency.subscribe((value) -> set(supplier.get()));
        return this;
    }

    @NotNull
    public List<Consumer<Object>> getConsumers() {
        return consumers;
    }

    @NotNull
    @Override
    public String toString() {
        return "BindableValue{value=" + value + '}';
    }

}
