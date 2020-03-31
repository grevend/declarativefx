package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface Fluent<N extends Node, C extends Component<N>> {

    @NotNull C fluent(@NotNull Consumer<N> consumer);

    @NotNull C set(@NotNull String property, @Nullable Object value);

    @Nullable Object get(@NotNull String property);

}
