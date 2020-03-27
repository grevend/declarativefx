package grevend.declarativefx.components;

import grevend.declarativefx.ObservableValue;
import grevend.declarativefx.components.fx.FX;
import grevend.declarativefx.components.fx.FXContainer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class Components {

    public static @NotNull <P extends Parent> Root<P> Root(Component<P> component) {
        return new Root<>(component);
    }

    @SafeVarargs
    public static @NotNull FXContainer<HBox> HBox(Component<? extends Node>... components) {
        return FXContainer(new HBox(), components);
    }

    @SafeVarargs
    public static @NotNull FXContainer<VBox> VBox(Component<? extends Node>... components) {
        return FXContainer(new VBox(), components);
    }

    public static @NotNull FX<Text> Text(@NotNull String text) {
        return new FX<>(new Text(text));
    }

    public static @NotNull <V> FX<Text> Text(@NotNull ObservableValue<V> observableValue) {
        return Text(observableValue,
            (value) -> value == null ? "" : (value instanceof String ? (String) value : value.toString()));
    }

    public static @NotNull <V> FX<Text> Text(@NotNull ObservableValue<V> observableValue,
                                             @NotNull Function<V, String> function) {
        var node = new Text();
        observableValue.subscribe(value -> node.setText(function.apply(value)));
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

    public static @NotNull <V> Component<?> Provider(@NotNull String id, @NotNull Supplier<V> supplier) {
        return Provider(id, supplier.get());
    }

    @SuppressWarnings("unchecked")
    public static @NotNull <V> Component<?> Provider(@NotNull String id, V value) {
        return Component.of("Provider", null, component -> null, component -> {
            var providers = component.getRoot().getProviders();
            if (providers.get(id) != null) {
                ((ObservableValue<V>) providers.get(id)).set(value);
            } else {
                component.getRoot().getProviders().put(id, new ObservableValue<>(value));
            }
        });
    }

    public static @NotNull <N extends Node, V> Component<N> Consumer(@NotNull String id,
                                                                     @NotNull Function<ObservableValue<V>, ? extends Component<N>> function) {
        return new Consumer<>(id, function);
    }

    public static @NotNull <N extends Node, V, V2> Component<N> Consumer(@NotNull String first, @NotNull String second,
                                                                         @NotNull BiFunction<ObservableValue<V>, ObservableValue<V2>, ? extends Component<N>> function) {
        return new BiConsumer<>(first, second, function);
    }

    public static @NotNull <N extends Node> FX<N> FX(N node) {
        return new FX<>(node);
    }

    @SafeVarargs
    public static @NotNull <P extends Pane> FXContainer<P> FXContainer(@NotNull P pane,
                                                                       Component<? extends Node>... components) {
        return new FXContainer<>(pane, components);
    }

    public static @NotNull FX<ScrollPane> ScrollPane(@NotNull Component<? extends Node> component) {
        return new FX<>(new ScrollPane(component.construct()));
    }

}
