package grevend.declarativefx.components.fx;

import grevend.declarativefx.components.Component;
import grevend.declarativefx.components.ContainerComponent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;

public class FXContainer<P extends Pane> extends ContainerComponent<P> {

    private P pane;

    @SafeVarargs
    public FXContainer(@NotNull P pane, Component<? extends Node>... components) {
        super(components);
        this.pane = pane;
    }

    @Override
    public P construct() {
        for (Component<? extends Node> component : components) {
            if(component != null) {
                var node = component.construct();
                if (node != null) {
                    if (this.pane instanceof FlowPane) {
                        FlowPane.setMargin(node, new Insets(5));
                    }
                    this.pane.getChildren().add(node);
                }
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

}
