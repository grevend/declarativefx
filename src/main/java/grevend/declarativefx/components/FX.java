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
import grevend.declarativefx.properties.*;
import grevend.declarativefx.util.*;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FX<N extends Node> extends Component<N>
    implements Fluent<N, FX<N>>, Bindable<N, FX<N>>, Listenable<N, FX<N>>, Identifiable<N, FX<N>>, Findable<N, FX<N>> {

    private final N node;
    private final Map<String, BindableValue> bindableProperties;
    private final Map<String, ObservableValue<Object>> observableProperties;
    private final Collection<Triplet<String, Object, Object>> lateBindings;
    private String defaultProperty;

    public FX(@Nullable N node) {
        this(node, null);
    }

    public FX(@Nullable N node, @Nullable String defaultProperty) {
        this.node = node;
        this.defaultProperty = defaultProperty;
        this.bindableProperties = new HashMap<>();
        this.observableProperties = new HashMap<>();
        this.lateBindings = new ArrayList<>();
    }

    @Override
    public @Nullable N construct() {
        return this.node;
    }

    public @Nullable N getNode() {
        return node;
    }

    @Override
    public @Nullable String getId() {
        if (this.node != null) {
            return this.node.getId();
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
    }

    public @NotNull FX<N> setId(@NotNull String id) {
        if (this.node != null) {
            this.node.setId(id);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public @Nullable Component<? extends Node> find(@NotNull String id, boolean root) {
        if (this.getId() != null && this.getId().equals(id)) {
            return this;
        } else if (root) {
            return this.getRoot().find(id, false);
        } else {
            return null;
        }
    }

    public @NotNull FX<N> setStyle(@NotNull String style) {
        if (this.node != null) {
            node.setStyle(style);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized @NotNull FX<N> set(@NotNull String property, @Nullable Object value) {
        if (property.toLowerCase().equals("id")) {
            if (value instanceof String) {
                this.setId((String) value);
            }
        } else if (property.toLowerCase().equals("style")) {
            if (value instanceof String) {
                this.setStyle((String) value);
            }
        } else {
            if (this.node != null) {
                var observableValue = Utils.getObservableValue(this.node, this.observableProperties, property);
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

    @Override
    public synchronized @Nullable Object get(@NotNull String property) {
        if (this.node != null) {
            var observableValue = Utils.getObservableValue(this.node, this.observableProperties, property);
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
    public @NotNull FX<N> fluent(@NotNull Consumer<N> consumer) {
        consumer.accept(this.getNode());
        return this;
    }

    @Override
    public @Nullable String getDefaultProperty() {
        return this.defaultProperty;
    }

    @Override
    public @NotNull FX<N> setDefaultProperty(@NotNull String property) {
        this.defaultProperty = property;
        return this;
    }

    @Override
    public @NotNull Map<String, BindableValue> getBindableValues() {
        return this.bindableProperties;
    }

    @Override
    public @NotNull Collection<Triplet<String, Object, Object>> getLateBindings() {
        return this.lateBindings;
    }

    @Override
    public @Nullable BindableValue getBinding(@NotNull String id) {
        return this.getRoot().getProviders().get(id);
    }

    @Override
    public @Nullable BindableValue getPropertyBinding(@NotNull String property) {
        return this.bindableProperties.get(property);
    }

    @SuppressWarnings("unchecked")
    private void lateBind(@NotNull String property, @NotNull BindableValue bindableValue) {
        if (this.node != null) {
            var observableValue = Utils.getObservableValue(this.node, this.observableProperties, property);
            if (observableValue != null) {
                this.bindableProperties.put(property, bindableValue);
                observableValue.addListener(observable -> bindableValue.set(observableValue.getValue()));
                if (observableValue instanceof WritableObjectValue) {
                    bindableValue.subscribe(((WritableObjectValue<Object>) observableValue)::setValue);
                }
            } else {
                throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
            }
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterConstruction() {
        for (var binding : this.lateBindings) {
            if (binding.getA() != null && binding.getB() != null) {
                if (binding.getB() instanceof BindableValue && binding.getC() == null) {
                    this.lateBind(binding.getA(), (BindableValue) binding.getB());
                } else if (binding.getB() instanceof String && binding.getC() == null) {
                    var bindableValue = this.getBinding((String) binding.getB());
                    if (bindableValue != null) {
                        this.lateBind(binding.getA(), bindableValue);
                    }
                } else if (binding.getB() instanceof BindableValue && binding.getC() instanceof Function) {
                    if (this.getPropertyBinding(binding.getA()) != null) {
                        Objects.requireNonNull(this.getPropertyBinding(binding.getA()))
                            .compute((BindableValue) binding.getB(), (Function<BindableValue, Object>) binding.getC());
                    } else {
                        throw new BindException(this.toString());
                    }
                } else if (binding.getB() instanceof BindableValue && binding.getC() instanceof Supplier) {
                    if (this.getPropertyBinding(binding.getA()) != null) {
                        Objects.requireNonNull(this.getPropertyBinding(binding.getA()))
                            .compute((BindableValue) binding.getB(), (Supplier<Object>) binding.getC());
                    } else {
                        throw new BindException(this.toString());
                    }
                } else {
                    throw new BindException("Late binding failed for " + binding.getA() + " on " + this + ".");
                }
            }
        }
    }

    @Override
    public @NotNull <E extends Event> FX<N> on(@NotNull EventType<E> type,
                                               grevend.declarativefx.util.@NotNull EventHandler<E> handler) {
        if (this.node != null) {
            this.node.addEventHandler(type, event -> handler.onEvent(event, this));
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public @NotNull <E extends Event> FX<N> on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler) {
        if (this.node != null) {
            this.node.addEventHandler(type, handler);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> FX<N> on(@NotNull String property, @NotNull ChangeListener<T> listener) {
        if (this.node != null) {
            var observableValue = Utils.getObservableValue(this.node, this.observableProperties, property);
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
    public @NotNull FX<N> on(@NotNull String property, @NotNull InvalidationListener listener) {
        if (this.node != null) {
            var observableValue = Utils.getObservableValue(this.node, this.observableProperties, property);
            if (observableValue != null) {
                observableValue.addListener(listener);
            } else {
                throw new IllegalArgumentException("Property " + property.toLowerCase() + " does not exist.");
            }
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public @NotNull String toString() {
        if (this.node != null) {
            return this.node.getClass().getTypeName() + (this.getId() != null ? ("#" + this.getId()) : "");
        }
        return "FX[N]" + (this.getId() != null ? ("#" + this.getId()) : "");
    }

}