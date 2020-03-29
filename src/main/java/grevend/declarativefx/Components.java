package grevend.declarativefx;

import grevend.declarativefx.components.Binding;
import grevend.declarativefx.components.FX;
import grevend.declarativefx.components.FXContainer;
import grevend.declarativefx.components.Root;
import grevend.declarativefx.util.BindableValue;
import grevend.declarativefx.util.VarArgsFunction;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Components {

    public static @NotNull <P extends Parent> Root<P> Root(@NotNull Component<P> component) {
        return new Root<>(component);
    }

    @SafeVarargs
    public static @NotNull FXContainer<HBox> HBox(@NotNull Component<? extends Node>... components) {
        return FXContainer(new HBox(), components);
    }

    @SafeVarargs
    public static @NotNull FXContainer<VBox> VBox(@NotNull Component<? extends Node>... components) {
        return FXContainer(new VBox(), components);
    }

    public static @NotNull FX<Text> Text(@NotNull String text) {
        return new FX<>(new Text(text));
    }

    public static @NotNull <V> FX<Text> Text(@NotNull BindableValue<V> bindableValue) {
        return Text(bindableValue,
            (value) -> value == null ? "" : (value instanceof String ? (String) value : value.toString()));
    }

    public static @NotNull <V> FX<Text> Text(@NotNull BindableValue<V> bindableValue,
                                             @NotNull Function<V, String> function) {
        var node = new Text();
        bindableValue.subscribe(value -> node.setText(function.apply(value)));
        return FX(node);
    }

    public static @NotNull FX<ImageView> Image(@NotNull Image image) {
        return FX(new ImageView(image));
    }

    public static @NotNull FX<ImageView> Image(@NotNull String image) {
        try {
            return new FX<>(new ImageView(new Image(new FileInputStream(image))));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return FX(null);
    }

    public static @NotNull FX<Button> Button(@NotNull String text) {
        return FX(new Button(text));
    }

    public static @NotNull FX<Button> Button(@NotNull String text, @NotNull EventHandler<ActionEvent> handler) {
        var node = new Button(text);
        node.setOnAction(handler);
        return FX(node);
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
                                                                 @NotNull VarArgsFunction<BindableValue<?>, ? extends Component<N>> function) {
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