package grevend.declarativefx.components;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Component<N extends Node> {

    private Component<? extends Node> parent;

    public @Nullable Component<? extends Node> getParent() {
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

    public abstract @Nullable N construct();

    public void afterConstruction() {
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getTypeName();
    }

}