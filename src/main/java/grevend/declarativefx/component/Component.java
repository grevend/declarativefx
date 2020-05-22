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

package grevend.declarativefx.component;

import grevend.declarativefx.bindable.Bindable;
import grevend.declarativefx.bindable.BindableCollection;
import grevend.declarativefx.bindable.BindableValue;
import grevend.declarativefx.event.EventHandler;
import grevend.declarativefx.util.Verbosity;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Component<N extends Node> extends Iterable<Component<? extends Node>> {

    @NotNull
    N getNode();

    @NotNull
    BindableCollection<Component<? extends Node>> getChildren();

    @NotNull
    Component<N> setChildren(BindableCollection<Component<? extends Node>> children);

    @NotNull
    Component<N> addChild(@NotNull Component<? extends Node> child);

    @NotNull
    Component<N> removeChild(@NotNull Component<? extends Node> child);

    @NotNull
    Component<N> addClass(@NotNull String clazz);

    @NotNull
    Component<N> removeClass(@NotNull String clazz);

    @NotNull
    Component<N> addChildren(@NotNull Collection<Component<? extends Node>> children);

    @NotNull
    Component<N> removeChildren(@NotNull Collection<Component<? extends Node>> children);

    @NotNull <E extends Event> Component<N> on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler);

    @NotNull <T> Component<N> on(@NotNull String property, @NotNull ChangeListener<T> listener);

    @NotNull
    Component<N> on(@NotNull String property, @NotNull InvalidationListener listener);

    @NotNull
    default Component<N> on(@NotNull EventHandler<ActionEvent> handler) {
        return this.on(ActionEvent.ACTION, handler);
    }

    @NotNull
    default Component<N> on(@NotNull String property, @NotNull BiConsumer<Component<? extends Node>, Object> consumer) {
        return this.on(property, (observable, oldValue, newValue) -> consumer.accept(this, newValue));
    }

    @NotNull
    Component<N> bind(@NotNull String property, @NotNull Bindable value);

    @NotNull
    default <E, R> Component<N> compute(@NotNull String property, @NotNull BindableCollection<E> collection, @NotNull Function<BindableCollection<E>, R> function) {
        var bindable = new BindableValue(function.apply(collection));
        collection.subscribe((change, changes) -> bindable.set(function.apply(collection)));
        return this.bind(property, bindable);
    }

    @NotNull
    Component<N> compute(@NotNull String property, @NotNull Bindable dependency, @NotNull Function<Bindable, Object> function);

    @NotNull
    Component<N> compute(@NotNull String property, @NotNull Bindable dependency, @NotNull Supplier<Object> supplier);

    @NotNull
    Component<N> set(@NotNull String property, @Nullable Object value);

    @Nullable
    Object get(@NotNull String property);

    @NotNull
    Map<String, Bindable> getProperties();

    @NotNull
    String stringify(@NotNull Verbosity verbosity);

    default boolean isDecorated() {
        return false;
    }

    default boolean isMarked() {
        return false;
    }

    @NotNull
    default Stream<Component<? extends Node>> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

}
