package grevend.declarativefx.components;

import grevend.declarativefx.util.BindableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

import static grevend.declarativefx.components.Compat.FX;

public class Controls {

    public static @NotNull FX<TextField> TextField(@NotNull String placeholder) {
        return FX(new TextField()).set("prompttext", placeholder);
    }

    public static @NotNull FX<TextField> TextField(@NotNull Consumer<String> consumer) {
        return FX(new TextField()).on("text", (observable, oldalue, newValue) -> consumer.accept((String) newValue));
    }

    public static @NotNull FX<TextField> TextField(@NotNull String placeholder, @NotNull Consumer<String> consumer) {
        return TextField(placeholder).on("text", (observable, oldalue, newValue) -> consumer.accept((String) newValue));
    }

    public static @NotNull <V> FX<TextField> TextField(@NotNull BindableValue<V> bindableValue) {
        return FX(new TextField()).bind(bindableValue);
    }

    public static @NotNull <V> FX<TextField> TextField(@NotNull BindableValue<V> bindableValue,
                                                       @NotNull String placeholder) {
        return TextField(bindableValue).set("prompttext", placeholder);
    }

    public static @NotNull FX<TextArea> TextArea(@NotNull String placeholder) {
        return FX(new TextArea()).set("prompttext", placeholder);
    }

    public static @NotNull FX<TextArea> TextArea(@NotNull Consumer<String> consumer) {
        return FX(new TextArea()).on("text", (observable, oldalue, newValue) -> consumer.accept((String) newValue));
    }

    public static @NotNull FX<TextArea> TextArea(@NotNull String placeholder, @NotNull Consumer<String> consumer) {
        return TextArea(placeholder).on("text", (observable, oldalue, newValue) -> consumer.accept((String) newValue));
    }

    public static @NotNull <V> FX<TextArea> TextArea(@NotNull BindableValue<V> bindableValue) {
        return FX(new TextArea()).bind(bindableValue);
    }

    public static @NotNull <V> FX<TextArea> TextArea(@NotNull BindableValue<V> bindableValue,
                                                     @NotNull String placeholder) {
        return TextArea(bindableValue).set("prompttext", placeholder);
    }

    public static @NotNull FX<Button> Button(@NotNull String text) {
        return FX(new Button(text));
    }

    public static @NotNull FX<Button> Button(@NotNull String text, @NotNull EventHandler<ActionEvent> handler) {
        var node = new Button(text);
        node.setOnAction(handler);
        return FX(node);
    }

}
