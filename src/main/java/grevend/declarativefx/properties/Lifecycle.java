package grevend.declarativefx.properties;

import javafx.scene.Node;
import org.jetbrains.annotations.Nullable;

public interface Lifecycle<N extends Node> {

    default void beforeConstruction() {
    }

    @Nullable N construct();

    default void afterConstruction() {
    }

    default void deconstruct() {
    }


}
