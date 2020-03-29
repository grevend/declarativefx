package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.properties.Bindable;
import grevend.declarativefx.properties.Fluent;
import grevend.declarativefx.util.BindableValue;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FX<N extends Node> extends Component<N> implements Fluent<N, FX<N>>, Bindable<N, FX<N>> {

    private final N node;
    private final String defaultProperty;

    public FX(@Nullable N node) {
        this(node, null);
    }

    public FX(@Nullable N node, @Nullable String defaultProperty) {
        this.node = node;
        this.defaultProperty = defaultProperty;
    }

    @Override
    public @Nullable N construct() {
        return this.node;
    }

    public @Nullable N getNode() {
        return node;
    }

    public @NotNull FX<N> setStyle(@NotNull String style) {
        if (this.node != null) {
            node.setStyle(style);
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

    @Override
    public @NotNull FX<N> set(@NotNull String property, @Nullable Object value) {
        if (this.node != null) {
            this.node.getProperties().put(property, value);
        }
        return this;
    }

    @Override
    public @Nullable Object get(@NotNull String property) {
        if (this.node != null) {
            return this.node.getProperties().get(property);
        }
        return null;
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
    public <V> FX<N> bind(@NotNull String property, @NotNull BindableValue<V> bindableValue) {
        return null;
    }

}