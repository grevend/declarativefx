package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.properties.Findable;
import grevend.declarativefx.properties.Identifiable;
import grevend.declarativefx.util.BindableValue;
import grevend.declarativefx.util.LifecycleException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Root<P extends Parent> extends Component<P> implements Identifiable<P, Root<P>>, Findable<P, Root<P>> {

    private final Map<String, BindableValue<?>> providers;
    private final Component<P> component;
    private Stage stage;
    private String id;
    private P child;
    private Scene scene;

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

    public @NotNull Map<String, BindableValue<?>> getProviders() {
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
        if (this.child == null) {
            return (this.child = this.component.construct());
        } else {
            return this.child;
        }
    }

    @Override
    public void afterConstruction() {
        this.component.afterConstruction();
    }

    @Override
    public void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix,
                                   @NotNull Verbosity verbosity) {
        super.stringifyHierarchy(builder, prefix, childPrefix, verbosity);
        if (this.component != null) {
            this.component.stringifyHierarchy(builder, childPrefix + "└── ", childPrefix + "    ", verbosity);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
    }

    public void launch(@NotNull Stage stage) {
        this.stage = stage;
        this.beforeConstruction();
        var tree = this.construct();
        if (tree != null) {
            this.afterConstruction();
            stage.setScene((this.scene = new Scene(tree)));
            stage.show();
        } else {
            throw new IllegalStateException("Component hierarchy construction failed.");
        }
    }

    @Override
    public @Nullable String getId() {
        return this.id;
    }

    @Override
    public Root<P> setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    @Override
    public @Nullable Component<? extends Node> find(@NotNull String id) {
        if(this.getId() != null && this.getId().equals(id)) {
            return this;
        } else if(this.component instanceof Findable) {
            return ((Findable<?, ?>) this.component).find(id);
        } else {
            return null;
        }
    }

    public @Nullable Scene getScene() {
        return scene;
    }

}