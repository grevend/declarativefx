package grevend.declarativefx.components;

import grevend.declarativefx.ObservableValue;
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

    private Stage stage;
    private final Component<P> component;
    private P parent;

    public Root(Component<P> component) {
        this.providers = new HashMap<>();
        this.component = component;
        this.component.setParent(this);
    }

    @Override
    public Component<? extends Node> getParent() {
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
        if(this.parent != null) {
            this.parent.setStyle(style);
        }
        return this;
    }

    @Override
    public void beforeConstruction() {
        this.component.beforeConstruction();
    }

    @Override
    public P construct() {
        return (this.parent = this.component.construct());
    }

    @Override
    public void afterConstruction() {
        this.component.afterConstruction();
    }

    public void launch(@NotNull Stage stage) {
        this.stage = stage;
        this.beforeConstruction();
        var tree = this.construct();
        this.afterConstruction();
        stage.setScene(new Scene(tree));
        stage.show();
        this.stage = stage;
    }

    @Override
    public String toString() {
        return "Root";
    }

}
