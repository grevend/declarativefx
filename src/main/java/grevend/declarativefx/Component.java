package grevend.declarativefx;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Component<N extends Node> implements BaseComponent<N> {

    private Component<? extends Node> parent;

    @Override
    public @Nullable Component<? extends Node> getParent() {
        return this.parent;
    }

    @Override
    public void setParent(@NotNull Component<? extends Node> parent) {
        this.parent = parent;
    }

    @Override
    public @NotNull String toString() {
        return this.getClass().getTypeName();
    }

}