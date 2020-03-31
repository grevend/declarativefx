package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Findable<N extends Node, C extends Component<N>> {

    @Nullable Component<? extends Node> find(@NotNull String id);

    default @Nullable <V extends Node> Component<V> find(@NotNull Class<Component<V>> type, @NotNull String id) {
        return type.cast(this.find(id));
    }

}
