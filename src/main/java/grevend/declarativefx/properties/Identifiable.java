package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Identifiable<N extends Node, C extends Component<N>> {

    @Nullable String getId();

    C setId(@NotNull String id);

}
