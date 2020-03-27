package grevend.declarativefx.components.fx;

import grevend.declarativefx.components.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public class FX<N extends Node> extends Component<N> {

    private N node;

    public FX(@NotNull N node) {
        this.node = node;
    }

    @Override
    public N construct() {
        return this.node;
    }

    public @NotNull N getNode() {
        return node;
    }

    public @NotNull FX<N> setStyle(@NotNull String style) {
        node.setStyle(style);
        return this;
    }

    @Override
    public String toString() {
        return this.parent + " > FX[" + this.node.getClass().getTypeName() + "]";
    }

}
