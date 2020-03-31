package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.util.BindableValue;
import grevend.declarativefx.util.VarArgsFunction;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Compat {

    public static @NotNull <P extends Parent> Root<P> Root(@NotNull Component<P> component) {
        return new Root<>(component);
    }

    public static @NotNull <N extends Node, V> Component<N> Binding(@NotNull String id,
                                                                    @NotNull Function<BindableValue<V>, ? extends Component<N>> function) {
        return new Binding<>(id, function);
    }

    public static @NotNull <N extends Node, V, U> Component<N> Binding(@NotNull String first, @NotNull String second,
                                                                       @NotNull BiFunction<BindableValue<V>, BindableValue<U>, ? extends Component<N>> function) {
        return new Binding<>(first, second, function);
    }

    public static @NotNull <N extends Node> Component<N> Binding(@NotNull String[] identifiers,
                                                                 @NotNull VarArgsFunction<BindableValue<Object>, ? extends Component<N>> function) {
        return new Binding<>(identifiers, function);
    }

    public static @NotNull <N extends Node> FX<N> FX(@Nullable N node) {
        return new FX<>(node);
    }

    @SafeVarargs
    public static @NotNull <P extends Pane> FXContainer<P> FXContainer(@NotNull P pane,
                                                                       @NotNull Component<? extends Node>... components) {
        return new FXContainer<>(pane, components);
    }

    public static @NotNull <P extends Pane> FXContainer<P> FXContainer(@NotNull P pane,
                                                                       @NotNull Iterable<Component<? extends Node>> components) {
        return new FXContainer<>(pane, components);
    }

}
