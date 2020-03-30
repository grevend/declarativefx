package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.properties.Bindable;
import grevend.declarativefx.properties.Fluent;
import grevend.declarativefx.properties.Identifiable;
import grevend.declarativefx.properties.Listenable;
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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class FX<N extends Node> extends Component<N>
    implements Fluent<N, FX<N>>, Bindable<N, FX<N>>, Listenable<N, FX<N>>, Identifiable<N, FX<N>> {

    public final static Map<Class<? extends Node>, Map<String, String>> propertyNames =
        new HashMap<>();

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

    public @NotNull FX<N> setStyle(@NotNull String style) {
        if (this.node != null) {
            node.setStyle(style);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    public @NotNull String toString() {
        if (this.node != null) {
            return this.node.getClass().getTypeName();
        }
        return "FX[N]";
    }

    private synchronized @Nullable ObservableValue<?> getObservableValue(@NotNull String property) {
        if (this.node != null) {
            var nodeClass = this.node.getClass();
            if (this.properties.containsKey(property)) {
                return this.properties.get(property);
            } else {
                if (!propertyNames.containsKey(nodeClass)) {
                    propertyNames.put(nodeClass, Utils.getPropertyNames(nodeClass));
                }
                if (propertyNames.get(nodeClass).containsKey(property)) {
                    try {
                        this.properties.put(property,
                            (ObservableValue<?>) nodeClass.getMethod(propertyNames.get(nodeClass).get(property))
                                .invoke(this.node));
                        return this.properties.get(property);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        }
        throw new LifecycleException("Hierarchy has not been constructed yet.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public synchronized @NotNull FX<N> set(@NotNull String property, @Nullable Object value) {
        if (property.equals("id")) {
            if (value instanceof String) {
                this.setId((String) value);
            }
        } else if (property.equals("style")) {
            if (value instanceof String) {
                this.setStyle((String) value);
            }
        } else {
            var observableValue = this.getObservableValue(property);
            if (observableValue != null) {
                if (observableValue instanceof WritableObjectValue) {
                    ((WritableObjectValue<Object>) observableValue).setValue(value);
                }
            } else {
                throw new IllegalArgumentException("Property " + property + " does not exist.");
            }
        }
        return this;
    }

    @Override
    public synchronized @Nullable Object get(@NotNull String property) {
        var observableValue = this.getObservableValue(property);
        if (observableValue != null) {
            return observableValue.getValue();
        } else {
            throw new IllegalArgumentException("Property " + property + " does not exist.");
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
        var observableValue = this.getObservableValue(property);
        if (observableValue != null) {
            observableValue.addListener(observable -> bindableValue.set((V) observableValue.getValue()));
            if (observableValue instanceof WritableObjectValue) {
                bindableValue.subscribe(((WritableObjectValue<Object>) observableValue)::setValue);
            }
        } else {
            throw new IllegalArgumentException("Property " + property + " does not exist.");
        }
        return this;
    }

    @Override
    public <E extends Event> FX<N> on(EventType<E> type, EventHandler<E> handler) {
        if (this.node != null) {
            this.node.addEventHandler(type, handler);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> FX<N> on(String property, ChangeListener<T> listener) {
        var observableValue = this.getObservableValue(property);
        if (observableValue != null) {
            observableValue.addListener((ChangeListener<Object>) listener);
        } else {
            throw new IllegalArgumentException("Property " + property + " does not exist.");
        }
        return this;
    }

    @Override
    public <T> FX<N> on(String property, InvalidationListener listener) {
        var observableValue = this.getObservableValue(property);
        if (observableValue != null) {
            observableValue.addListener(listener);
        } else {
            throw new IllegalArgumentException("Property " + property + " does not exist.");
        }
        return null;
    }

}