package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public interface Findable<N extends Node, C extends Component<N>> {

    @Nullable Component<? extends Node> find(@NotNull String id, boolean root);

    default @Nullable Component<? extends Node> find(@NotNull String id) {
        return this.find(id, true);
    }

    default @Nullable Collection<Component<? extends Node>> find(@NotNull String... identifiers) {
        return Arrays.stream(identifiers).map(this::find).filter(Objects::nonNull).collect(Collectors.toList());
    }

    default @Nullable Collection<Component<? extends Node>> find(@NotNull Iterable<String> identifiers) {
        return StreamSupport.stream(identifiers.spliterator(), false).map(this::find).filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

}
