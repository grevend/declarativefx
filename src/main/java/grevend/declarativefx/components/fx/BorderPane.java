package grevend.declarativefx.components.fx;

import grevend.declarativefx.components.Component;
import javafx.geometry.Insets;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public class BorderPane extends Component<javafx.scene.layout.BorderPane> {

    private javafx.scene.layout.BorderPane borderPane;
    private Component<? extends Node> center, top, right, bottom, left;

    public BorderPane(Component<? extends Node> center) {
        this(center, null, null, null, null);
    }

    public BorderPane(Component<? extends Node> center, Component<? extends Node> top, Component<? extends Node> right,
                      Component<? extends Node> bottom, Component<? extends Node> left) {
        this.borderPane = new javafx.scene.layout.BorderPane();
        this.center = center;
        if (this.center != null) {
            this.center.setParent(this);
        }
        this.top = top;
        if (this.top != null) {
            this.top.setParent(this);
        }
        this.right = right;
        if (this.right != null) {
            this.right.setParent(this);
        }
        this.bottom = bottom;
        if (this.bottom != null) {
            this.bottom.setParent(this);
        }
        this.left = left;
        if (this.left != null) {
            this.left.setParent(this);
        }
    }

    @Override
    public void beforeConstruction() {
        if (this.center != null) {
            this.center.beforeConstruction();
        }
        if (this.top != null) {
            this.top.beforeConstruction();
        }
        if (this.right != null) {
            this.right.beforeConstruction();
        }
        if (this.bottom != null) {
            this.bottom.beforeConstruction();
        }
        if (this.left != null) {
            this.left.beforeConstruction();
        }
    }

    @Override
    public javafx.scene.layout.BorderPane construct() {
        if (this.center != null) {
            this.borderPane.setCenter(this.center.construct());
        }
        if (this.top != null) {
            this.borderPane.setTop(this.top.construct());
        }
        if (this.right != null) {
            this.borderPane.setRight(this.right.construct());
        }
        if (this.bottom != null) {
            this.borderPane.setBottom(this.bottom.construct());
        }
        if (this.left != null) {
            this.borderPane.setLeft(this.left.construct());
        }
        return this.borderPane;
    }

    @Override
    public void afterConstruction() {
        if (this.center != null) {
            this.center.afterConstruction();
        }
        if (this.top != null) {
            this.top.afterConstruction();
        }
        if (this.right != null) {
            this.right.afterConstruction();
        }
        if (this.bottom != null) {
            this.bottom.afterConstruction();
        }
        if (this.left != null) {
            this.left.afterConstruction();
        }
    }

    public @NotNull BorderPane setStyle(String style) {
        borderPane.setStyle(style);
        return this;
    }

    public @NotNull BorderPane setPadding(Insets padding) {
        borderPane.setPadding(padding);
        return this;
    }

    public @NotNull BorderPane setPadding(double padding) {
        return this.setPadding(new Insets(padding));
    }

}
