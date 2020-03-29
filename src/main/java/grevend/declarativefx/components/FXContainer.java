package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.ContainerComponent;
import grevend.declarativefx.properties.Fluent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class FXContainer<P extends Pane> extends ContainerComponent<P> implements Fluent<P, FXContainer<P>> {

    private P pane;

    @SafeVarargs
    public FXContainer(@NotNull P pane, Component<? extends Node>... components) {
        super(components);
        this.pane = pane;
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
    public @NotNull String toString() {
        return this.pane.getClass().getTypeName();
    }

    public @NotNull FXContainer<P> set(@NotNull String property, @Nullable Object value) {
        if (this.pane != null) {
            this.pane.getProperties().put(property, value);
        }
        return this;
    }

    public @Nullable Object get(@NotNull String property) {
        if (this.pane != null) {
            return this.pane.getProperties().get(property);
        }
        return null;
    }

    @Override
    public @NotNull FXContainer<P> fluent(@NotNull Consumer<P> consumer) {
        consumer.accept(this.pane);
        return this;
    }

}