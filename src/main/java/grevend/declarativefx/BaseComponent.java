package grevend.declarativefx;

import grevend.declarativefx.components.Root;
import grevend.declarativefx.properties.Lifecycle;
import grevend.declarativefx.util.StringifiableHierarchy;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface BaseComponent<N extends Node> extends Lifecycle<N>, StringifiableHierarchy {

    @Nullable Component<? extends Node> getParent();

    void setParent(@NotNull Component<? extends Node> parent);


    default @NotNull Root<?> getRoot() {
        if (this.getParent() == null) {
            throw new IllegalStateException(
                "Component '" + this.toString() + "' should be Root or is missing a parent component.");
        }
        return this.getParent().getRoot();
    }

    @Override
    default void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix,
                                    @NotNull Verbosity verbosity) {
        builder.append(prefix).append(this.toString()).append(System.lineSeparator());
    }

}
