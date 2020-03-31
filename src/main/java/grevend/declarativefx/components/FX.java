package grevend.declarativefx.components;

import grevend.declarativefx.Component;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FX<N extends Node> extends Component<N>
    implements Fluent<N, FX<N>>, Bindable<N, FX<N>>, Listenable<N, FX<N>>, Identifiable<N, FX<N>>, Findable<N, FX<N>> {

    private final N node;
    private final String defaultProperty;
    private final Map<String, ObservableValue<?>> properties;

    public FX(@Nullable N node) {
        this(node, null);
    }

    public FX(@Nullable N node, @Nullable String defaultProperty) {
        this.node = node;
        this.defaultProperty = defaultProperty;
        this.properties = new HashMap<>();
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
                var observableValue = Utils.getObservableValue(this.node, this.properties, property);
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
            var observableValue = Utils.getObservableValue(this.node, this.properties, property);
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
    @SuppressWarnings("unchecked")
    public <V> FX<N> bind(@NotNull String property, @NotNull BindableValue<V> bindableValue) {
        if (this.node != null) {
            var observableValue = Utils.getObservableValue(this.node, this.properties, property);
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
    public <E extends Event> FX<N> on(@NotNull EventType<E> type,
                                      grevend.declarativefx.util.@NotNull EventHandler<E> handler) {
        if (this.node != null) {
            this.node.addEventHandler(type, event -> handler.onEvent(event, this));
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public <E extends Event> FX<N> on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler) {
        if (this.node != null) {
            this.node.addEventHandler(type, handler);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> FX<N> on(@NotNull String property, @NotNull ChangeListener<T> listener) {
        if (this.node != null) {
            var observableValue = Utils.getObservableValue(this.node, this.properties, property);
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
    public FX<N> on(@NotNull String property, @NotNull InvalidationListener listener) {
        if (this.node != null) {
            var observableValue = Utils.getObservableValue(this.node, this.properties, property);
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
        if (this.node != null) {
            return this.node.getClass().getTypeName() + (this.getId() != null ? ("#" + this.getId()) : "");
        }
        return "FX[N]" + (this.getId() != null ? ("#" + this.getId()) : "");
    }

}