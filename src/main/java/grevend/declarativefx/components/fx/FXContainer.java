package grevend.declarativefx.components.fx;

import grevend.declarativefx.components.Component;
import grevend.declarativefx.components.ContainerComponent;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FXContainer<P extends Pane> extends ContainerComponent<P> {

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
    public void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix,
                                   @NotNull String childPrefix) {
        super.stringifyHierarchy(builder, prefix, childPrefix);
    }

    @Override
    public @NotNull String toString() {
        return "FXContainer[" + this.pane.getClass().getTypeName() + "]";
    }

}