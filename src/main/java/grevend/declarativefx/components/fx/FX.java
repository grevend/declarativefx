package grevend.declarativefx.components.fx;

import grevend.declarativefx.components.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FX<N extends Node> extends Component<N> {

    private N node;

    public FX(@Nullable N node) {
        this.node = node;
    }

    @Override
    public @Nullable N construct() {
        return this.node;
    }

    public @Nullable N getNode() {
        return node;
    }

    public @NotNull FX<N> setStyle(@NotNull String style) {
        node.setStyle(style);
        return this;
    }

    @Override
    public @NotNull String toString() {
        return "FX[" + this.node.getClass().getTypeName() + "]";
    }

}