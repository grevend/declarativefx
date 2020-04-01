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

package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import grevend.declarativefx.util.EventHandler;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface Listenable<N extends Node, C extends Component<N>> {

    @NotNull <E extends Event> C on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler);

    default @NotNull C on(@NotNull EventHandler<ActionEvent> handler) {
        return this.on(ActionEvent.ACTION, handler);
    }

    @NotNull <E extends Event> C on(@NotNull EventType<E> type, @NotNull javafx.event.EventHandler<E> handler);

    default @NotNull C on(@NotNull javafx.event.EventHandler<ActionEvent> handler) {
        return this.on(ActionEvent.ACTION, handler);
    }

    @SuppressWarnings("unchecked")
    default @NotNull <T> C on(@NotNull String property, @NotNull Consumer<Object> consumer) {
        this.on(property, (observable, oldValue, newValue) -> consumer.accept(newValue));
        return (C) this;
    }

    @SuppressWarnings("unchecked")
    default @NotNull <T> C on(@NotNull String property,
                              @NotNull BiConsumer<Component<? extends Node>, Object> consumer) {
        this.on(property,
            (observable, oldValue, newValue) -> consumer.accept((Component<? extends Node>) this, newValue));
        return (C) this;
    }

    @NotNull <T> C on(@NotNull String property, @NotNull ChangeListener<T> listener);

    @NotNull C on(@NotNull String property, @NotNull InvalidationListener listener);

}
