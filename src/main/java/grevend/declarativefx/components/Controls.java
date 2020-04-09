/*
 * MIT License
 *
 * Copyright (c) 2020 David Greven
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.util.BindableValue;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Consumer;

import static grevend.declarativefx.components.Compat.FX;

public class Controls {

    @Deprecated
    public static @NotNull Component<TextField> TextField() {
        return TextField("");
    }

    public static @NotNull Component<TextField> TextField(@NotNull String placeholder) {
        return FX(new TextField()).set("prompttext", placeholder);
    }

    @Deprecated
    public static @NotNull Component<TextField> TextField(@NotNull BindableValue bindableValue) {
        return FX(new TextField()).bind(bindableValue);
    }

    @Deprecated
    public static @NotNull Component<TextField> TextField(@NotNull BindableValue bindableValue,
                                                          @NotNull String placeholder) {
        return TextField(bindableValue).set("prompttext", placeholder);
    }

    @Deprecated
    public static @NotNull Component<PasswordField> PasswordField() {
        return FX(new PasswordField());
    }

    public static @NotNull Component<PasswordField> PasswordField(@NotNull String placeholder) {
        return PasswordField().set("prompttext", placeholder);
    }

    @Deprecated
    public static @NotNull Component<TextArea> TextArea() {
        return TextArea("");
    }

    public static @NotNull Component<TextArea> TextArea(@NotNull String placeholder) {
        return FX(new TextArea()).set("prompttext", placeholder);
    }

    @Deprecated
    public static @NotNull Component<TextArea> TextArea(@NotNull BindableValue bindableValue) {
        return FX(new TextArea()).bind(bindableValue);
    }

    @Deprecated
    public static @NotNull Component<TextArea> TextArea(@NotNull BindableValue bindableValue,
                                                        @NotNull String placeholder) {
        return TextArea(bindableValue).set("prompttext", placeholder);
    }

    public static @NotNull <T> Component<ChoiceBox<T>> ChoiceBox(@NotNull Collection<T> items) {
        return FX(new ChoiceBox<>(FXCollections.observableArrayList(items)));
    }

    public static @NotNull <T> Component<ChoiceBox<T>> ChoiceBox(@NotNull Collection<T> items, @NotNull T value) {
        return FX(new ChoiceBox<>(FXCollections.observableArrayList(items))).set("value", value);
    }

    public static @NotNull Component<Button> Button(@NotNull String text) {
        return FX(new Button(text));
    }

    public static @NotNull Component<Button> Button(@NotNull String text, @NotNull String img, double[] imgSize) {
        var node = new Button(text);
        if (img.contains("http") || img.contains("https")) {
            Image image = imgSize[0] == -1 ? new Image(img) : new Image(img, imgSize[0], imgSize[1], true, true);
            node.setGraphic(new ImageView(image));
        } else {
            //TODO: loading via file currently cannot take a size.
            node.setGraphic(Layout.Image(img).construct());
        }
        return FX(node);
    }

    public static @NotNull Component<Button> Button(@NotNull String text, @NotNull String img) {
        return Button(text, img, new double[]{-1, -1});
    }

    public static @NotNull Component<Label> Label(@NotNull String text) {
        return FX(new Label(text));
    }

    @Deprecated
    public static @NotNull Component<Hyperlink> Hyperlink() {
        return Hyperlink("");
    }

    public static @NotNull Component<Hyperlink> Hyperlink(@NotNull String text) {
        return FX(new Hyperlink(text));
    }

    @Deprecated
    public static @NotNull Component<Hyperlink> Hyperlink(@NotNull String text,
                                                          @NotNull Component<? extends Node> component) {
        return FX(new Hyperlink(text, component.construct()));
    }

    public static @NotNull Component<VBox> MenuBar(@NotNull Consumer<Collection<Menu>> builder) {
        var menuBar = new MenuBar();
        builder.accept(menuBar.getMenus());
        return FX(new VBox(menuBar));
    }

}
