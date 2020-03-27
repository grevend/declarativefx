package grevend.declarativefx.components;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Function;

public abstract class Component<N extends Node> {

    protected Component<? extends Node> parent;

    public static @NotNull <N extends Node> Component<N> of(String name, Consumer<Component<N>> beforeConstruction,
                                                            @NotNull Function<Component<N>, N> construction,
                                                            Consumer<Component<N>> afterConstruction) {
        return new Component<>() {

            @Override
            public void beforeConstruction() {
                if (beforeConstruction != null) {
                    beforeConstruction.accept(this);
                }
            }

            @Override
            public N construct() {
                return construction.apply(this);
            }

            @Override
            public void afterConstruction() {
                if (afterConstruction != null) {
                    afterConstruction.accept(this);
                }
            }

            @Override
            public String toString() {
                return this.parent + " > " + name;
            }

        };
    }

    public static @NotNull <N extends Node> Component<N> of(String name, @NotNull Function<Component<N>, N> supplier) {
        return of(name, null, supplier, null);
    }

    public Component<? extends Node> getParent() {
        return parent;
    }

    public void setParent(@NotNull Component<? extends Node> parent) {
        this.parent = parent;
    }

    public @NotNull Root<?> getRoot() {
        if (this.getParent() == null) {
            throw new IllegalStateException(
                "Component '" + this.toString() + "' should be Root or is missing a parent component.");
        }
        return this.getParent().getRoot();
    }

    public void beforeConstruction() {
    }

    public abstract N construct();

    public void afterConstruction() {
    }

    @Override
    public String toString() {
        return this.parent + " > " + this.getClass().getTypeName();
    }

}
