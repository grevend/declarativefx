package grevend.declarativefx.components;

import grevend.declarativefx.util.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Root<P extends Parent> extends Component<P> {

    private final Map<String, ObservableValue<?>> providers;
    private final Component<P> component;
    private Stage stage;
    private P child;

    public Root(@NotNull Component<P> component) {
        this.providers = new HashMap<>();
        this.component = component;
        this.component.setParent(this);
    }

    public @Nullable P getNode() {
        return this.child;
    }

    @Override
    public @Nullable Component<? extends Node> getParent() {
        return null;
    }

    @Override
    public void setParent(@NotNull Component<? extends Node> parent) {
        throw new IllegalStateException();
    }

    public @NotNull Map<String, ObservableValue<?>> getProviders() {
        return providers;
    }

    @Override
    public @NotNull Root<P> getRoot() {
        return this;
    }

    public @Nullable Stage getStage() {
        return this.stage;
    }

    public @NotNull Root<P> setStyle(String style) {
        if (this.child != null) {
            this.child.setStyle(style);
        }
        return this;
    }

    @Override
    public void beforeConstruction() {
        this.component.beforeConstruction();
    }

    @Override
    public @Nullable P construct() {
        return (this.child = this.component.construct());
    }

    @Override
    public void afterConstruction() {
        this.component.afterConstruction();
    }

    public void launch(@NotNull Stage stage) {
        this.stage = stage;
        this.beforeConstruction();
        var tree = this.construct();
        if (tree != null) {
            this.afterConstruction();
            stage.setScene(new Scene(tree));
            stage.show();
        } else {
            throw new IllegalStateException("Component hierarchy construction failed.");
        }
    }

    @Override
    public @NotNull String toString() {
        return "Root";
    }

}