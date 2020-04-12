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

import grevend.declarativefx.bindable.BindException;
import grevend.declarativefx.bindable.BindableValue;
import grevend.declarativefx.event.EventHandler;
import grevend.declarativefx.util.Utils;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

public class FXComponent<N extends Node> extends ComponentBase<N> {

    private final Map<String, ObservableValue<Object>> observableProperties;

    public FXComponent(@NotNull N node) {
        super(node);
        this.observableProperties = new WeakHashMap<>();
    }

    @NotNull
    @Override
    public <E extends Event> Component<N> on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler) {
        this.getNode().addEventHandler(type, event -> handler.onEvent(event, this));
        return this;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public <T> Component<N> on(@NotNull String property, @NotNull ChangeListener<T> listener) {
        var observableValue = Utils.getObservableValue(this.getNode(), this.observableProperties, property);
        if (observableValue == null) {
            throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
        } else {
            observableValue.addListener((ChangeListener<Object>) listener);
            return this;
        }
    }

    @NotNull
    @Override
    public Component<N> on(@NotNull String property, @NotNull InvalidationListener listener) {
        var observableValue = Utils.getObservableValue(this.getNode(), this.observableProperties, property);
        if (observableValue == null) {
            throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
        } else {
            observableValue.addListener(listener);
            return this;
        }
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Component<N> bind(@NotNull String property, @NotNull BindableValue value) {
        var observableValue = Utils.getObservableValue(this.getNode(), this.observableProperties, property);
        if (observableValue == null) {
            throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
        } else {
            this.getProperties().put(property, value);
            if (observableValue instanceof WritableObjectValue) {
                value.subscribe(((WritableObjectValue<Object>) observableValue)::setValue);
            }
            return this;
        }
    }

    @NotNull
    @Override
    public Component<N> compute(@NotNull String property, @NotNull BindableValue dependency, @NotNull Function<BindableValue, Object> function) {
        if (this.getProperty(property) == null) {
            throw new BindException(this.toString());
        } else {
            Objects.requireNonNull(this.getProperty(property)).compute(dependency, function);
            return this;
        }
    }

    @NotNull
    @Override
    public Component<N> compute(@NotNull String property, @NotNull BindableValue dependency, @NotNull Supplier<Object> supplier) {
        if (this.getProperty(property) == null) {
            this.bind(property, new BindableValue(this.get(property)));
        }
        Objects.requireNonNull(this.getProperty(property)).compute(dependency, supplier);
        return this;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Component<N> set(@NotNull String property, @Nullable Object value) {
        switch (property.toLowerCase()) {
            case "id":
                this.getNode().setId((String) Objects.requireNonNull(value));
                break;
            case "style":
                this.getNode().setStyle((String) Objects.requireNonNull(value));
                break;
            case "marker":
                this.setMarker((Integer) Objects.requireNonNull(value));
                break;
            default:
                var observableValue = Utils.getObservableValue(this.getNode(), this.observableProperties, property);
                if (observableValue == null) {
                    throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
                } else {
                    if (observableValue instanceof WritableObjectValue) {
                        ((WritableObjectValue<Object>) observableValue).setValue(value);
                    }
                }
                break;
        }
        return this;
    }

    @Nullable
    @Override
    public Object get(@NotNull String property) {
        switch (property.toLowerCase()) {
            case "id":
                return this.getNode().getId();
            case "style":
                return this.getNode().getStyle();
            case "styleclass":
                return this.getNode().getStyleClass();
            case "marker":
                return this.getMarker();
            default:
                var observableValue = Utils.getObservableValue(this.getNode(), this.observableProperties, property);
                if (observableValue == null) {
                    throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
                } else {
                    return observableValue.getValue();
                }
        }
    }

}
