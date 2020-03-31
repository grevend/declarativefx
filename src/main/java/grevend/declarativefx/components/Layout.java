package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.util.BindableValue;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

import static grevend.declarativefx.components.Compat.FX;
import static grevend.declarativefx.components.Compat.FXContainer;

public class Layout {

    public static @NotNull FX<javafx.scene.text.Text> Text(@NotNull String text) {
        return new FX<>(new javafx.scene.text.Text(text));
    }

    public static @NotNull <V> FX<javafx.scene.text.Text> Text(@NotNull BindableValue<V> bindableValue) {
        return Text(bindableValue,
            (value) -> value == null ? "" : (value instanceof String ? (String) value : value.toString()));
    }

    public static @NotNull <V> FX<javafx.scene.text.Text> Text(@NotNull BindableValue<V> bindableValue,
                                                               @NotNull Function<V, String> function) {
        var node = new javafx.scene.text.Text();
        bindableValue.subscribe(value -> node.setText(function.apply(value)));
        return FX(node);
    }

    @SafeVarargs
    public static @NotNull FXContainer<HBox> HBox(@NotNull Component<? extends Node>... components) {
        return FXContainer(new HBox(), components);
    }

    @SafeVarargs
    public static @NotNull FXContainer<VBox> VBox(@NotNull Component<? extends Node>... components) {
        return FXContainer(new VBox(), components);
    }

    public static @NotNull FX<ScrollPane> ScrollPane(@NotNull Component<? extends Node> component) {
        return new FX<>(new ScrollPane(component.construct()));
    }

    public static @NotNull FX<BorderPane> BorderPane(@NotNull Component<? extends Node> centerComponent) {
        return new FX<>(new BorderPane(centerComponent.construct()));
    }

    public static @NotNull FX<BorderPane> BorderPane(@NotNull Component<? extends Node> centerComponent,
                                                     @NotNull Component<? extends Node> rightComponent,
                                                     @NotNull Component<? extends Node> leftComponent) {
        return new FX<>(new BorderPane(centerComponent.construct(), null, rightComponent.construct(), null,
            leftComponent.construct()));
    }

    public static @NotNull FX<BorderPane> BorderPane(@Nullable Component<? extends Node> centerComponent,
                                                     @Nullable Component<? extends Node> topComponent,
                                                     @Nullable Component<? extends Node> rightComponent,
                                                     @Nullable Component<? extends Node> bottomComponent,
                                                     @Nullable Component<? extends Node> leftComponent) {
        return new FX<>(
            new BorderPane(centerComponent != null ? centerComponent.construct() : null,
                topComponent != null ? topComponent.construct() : null,
                rightComponent != null ? rightComponent.construct() : null,
                bottomComponent != null ? bottomComponent.construct() : null,
                leftComponent != null ? leftComponent.construct() : null));
    }

}
