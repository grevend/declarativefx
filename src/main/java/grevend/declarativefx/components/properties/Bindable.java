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

package grevend.declarativefx.components.properties;

import grevend.declarativefx.Component;
import grevend.declarativefx.bindable.BindableCollection;
import grevend.declarativefx.bindable.BindableValue;
import grevend.declarativefx.util.Triplet;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Bindable<N extends Node> {

    default @NotNull Component<N> bind(@NotNull String property, @NotNull BindableValue bindableValue) {
        this.getLateBindings().add(new Triplet<>(property, bindableValue, null));
        return (Component<N>) this;
    }

    default @NotNull <E, R> Component<N> compute(@NotNull String property,
                                                 @NotNull BindableCollection<E> bindableCollection,
                                                 @NotNull Function<BindableCollection<E>, R> function) {
        var bindable = new BindableValue(function.apply(bindableCollection));
        bindableCollection.subscribe((change, changes) -> bindable.set(function.apply(bindableCollection)));
        this.bind(property, bindable);
        return (Component<N>) this;
    }

    default @NotNull Component<N> compute(@NotNull String property, @NotNull BindableValue dependency,
                                          @NotNull Function<BindableValue, Object> function) {
        this.getLateBindings().add(new Triplet<>(property, dependency, function));
        return (Component<N>) this;
    }

    default @NotNull Component<N> compute(@NotNull String property, @NotNull BindableValue dependency,
                                          @NotNull Supplier<Object> supplier) {
        this.getLateBindings().add(new Triplet<>(property, dependency, supplier));
        return (Component<N>) this;
    }

    @NotNull Map<String, BindableValue> getBindableValues();

    @NotNull Collection<Triplet<String, Object, Object>> getLateBindings();

    @Nullable BindableValue getProperty(@NotNull String property);

}
