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

package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.ContainerComponent;
import grevend.declarativefx.properties.*;
import grevend.declarativefx.util.BindableValue;
import grevend.declarativefx.util.LifecycleException;
import grevend.declarativefx.util.Utils;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.StreamSupport;

public class FXContainer<P extends Pane> extends ContainerComponent<P>
    implements Fluent<P, FXContainer<P>>, Bindable<P, FXContainer<P>>, Listenable<P, ContainerComponent<P>>,
    Identifiable<P, ContainerComponent<P>>, Findable<P, ContainerComponent<P>> {

    private final Map<String, ObservableValue<?>> properties;
    private P pane;

    public FXContainer(@NotNull P pane, @NotNull Iterable<Component<? extends Node>> components) {
        super(StreamSupport.stream(components.spliterator(), false).toArray(Component<?>[]::new));
        this.pane = pane;
        this.properties = new HashMap<>();
    }

    @SafeVarargs
    public FXContainer(@NotNull P pane, Component<? extends Node>... components) {
        super(components);
        this.pane = pane;
        this.properties = new HashMap<>();
    }

    @Override
    public @Nullable P construct() {
        for (Component<? extends Node> component : this.getComponents()) {
            var node = component.construct();
            if (node != null) {
                this.pane.getChildren().add(node);
            }
        }
        return this.pane;
    }

    public @NotNull FXContainer<P> setStyle(@NotNull String style) {
        this.pane.setStyle(style);
        return this;
    }

    public @NotNull FXContainer<P> setPrefSize(double width, double height) {
        this.pane.setPrefSize(width, height);
        return this;
    }

    @Override
    public @Nullable String getId() {
        if (this.pane != null) {
            return this.pane.getId();
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
    }

    @Override
    public ContainerComponent<P> setId(@NotNull String id) {
        if (this.pane != null) {
            this.pane.setId(id);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public @Nullable Component<? extends Node> find(@NotNull String id, boolean root) {
        if (this.getId() != null && this.getId().equals(id)) {
            return this;
        } else {
            if (root) {
                return this.getRoot().find(id, false);
            } else {
                for (Component<? extends Node> component : this.getComponents()) {
                    if (component instanceof Findable) {
                        var comp = ((Findable<?, ?>) component).find(id, false);
                        if (comp != null) {
                            return comp;
                        }
                    }
                }
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public @NotNull FXContainer<P> set(@NotNull String property, @Nullable Object value) {
        if (property.toLowerCase().equals("id")) {
            if (value instanceof String) {
                this.setId((String) value);
            }
        } else if (property.toLowerCase().equals("style")) {
            if (value instanceof String) {
                this.setStyle((String) value);
            }
        } else {
            if (this.pane != null) {
                var observableValue = Utils.getObservableValue(this.pane, this.properties, property);
                if (observableValue != null) {
                    if (observableValue instanceof WritableObjectValue) {
                        ((WritableObjectValue<Object>) observableValue).setValue(value);
                    }
                } else {
                    throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
                }
            } else {
                throw new LifecycleException("Hierarchy has not been constructed yet.");
            }
        }
        return this;
    }

    public @Nullable Object get(@NotNull String property) {
        if (this.pane != null) {
            var observableValue = Utils.getObservableValue(this.pane, this.properties, property);
            if (observableValue != null) {
                return observableValue.getValue();
            } else {
                throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
            }
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
    }

    @Override
    public @NotNull FXContainer<P> fluent(@NotNull Consumer<P> consumer) {
        consumer.accept(this.pane);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <V> FXContainer<P> bind(@NotNull String property, @NotNull BindableValue<V> bindableValue) {
        if (this.pane != null) {
            var observableValue = Utils.getObservableValue(this.pane, this.properties, property);
            if (observableValue != null) {
                observableValue.addListener(observable -> bindableValue.set((V) observableValue.getValue()));
                if (observableValue instanceof WritableObjectValue) {
                    bindableValue.subscribe(((WritableObjectValue<Object>) observableValue)::setValue);
                }
            } else {
                throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
            }
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public <E extends Event> ContainerComponent<P> on(@NotNull EventType<E> type,
                                                      grevend.declarativefx.util.@NotNull EventHandler<E> handler) {
        if (this.pane != null) {
            this.pane.addEventHandler(type, event -> handler.onEvent(event, this));
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public <E extends Event> ContainerComponent<P> on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler) {
        if (this.pane != null) {
            this.pane.addEventHandler(type, handler);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ContainerComponent<P> on(@NotNull String property, @NotNull ChangeListener<T> listener) {
        if (this.pane != null) {
            var observableValue = Utils.getObservableValue(this.pane, this.properties, property);
            if (observableValue != null) {
                observableValue.addListener((ChangeListener<Object>) listener);
            } else {
                throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
            }
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public ContainerComponent<P> on(@NotNull String property, @NotNull InvalidationListener listener) {
        if (this.pane != null) {
            var observableValue = Utils.getObservableValue(this.pane, this.properties, property);
            if (observableValue != null) {
                observableValue.addListener(listener);
            } else {
                throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
            }
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return null;
    }

    @Override
    public @NotNull String toString() {
        return this.pane.getClass().getTypeName() + (this.getId() != null ? ("#" + this.getId()) : "");
    }

}