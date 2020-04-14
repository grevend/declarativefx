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

package grevend.declarativefx.component;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Consumer;

import static grevend.declarativefx.component.Compat.FX;

public class Controls {

    @NotNull
    public static Component<TextField> TextField(@NotNull String placeholder) {
        return FX(new TextField()).set("prompttext", placeholder);
    }

    @NotNull
    public static Component<PasswordField> PasswordField(@NotNull String placeholder) {
        return FX(new PasswordField()).set("prompttext", placeholder);
    }

    @NotNull
    public static Component<TextArea> TextArea(@NotNull String placeholder) {
        return FX(new TextArea()).set("prompttext", placeholder);
    }

    @NotNull
    public static <T> Component<ChoiceBox<T>> ChoiceBox(@NotNull Collection<T> items) {
        return FX(new ChoiceBox<>(FXCollections.observableArrayList(items)));
    }

    @NotNull
    public static <T> Component<ChoiceBox<T>> ChoiceBox(@NotNull Collection<T> items, @NotNull T value) {
        return FX(new ChoiceBox<>(FXCollections.observableArrayList(items))).set("value", value);
    }

    @NotNull
    @SafeVarargs
    public static <T> Component<ChoiceBox<T>> ChoiceBox(T... items) {
        return FX(new ChoiceBox<>(FXCollections.observableArrayList(Arrays.asList(items))));
    }

    @NotNull
    public static Component<Button> Button(@NotNull String text) {
        return FX(new Button(text));
    }

    @NotNull
    public static Component<Button> Button(@NotNull String text, @NotNull String img, double[] imgSize) {
        var node = new Button(text);
        if (img.contains("http") || img.contains("https")) {
            Image image = imgSize[0] == -1 ? new Image(img) : new Image(img, imgSize[0], imgSize[1], true, true);
            node.setGraphic(new ImageView(image));
        } else {
            node.setGraphic(Layout.Image(img).getNode());
        }
        return FX(node);
    }

    @NotNull
    public static Component<Button> Button(@NotNull String text, @NotNull String img) {
        return Button(text, img, new double[]{-1, -1});
    }

    @NotNull
    public static Component<Label> Label(@NotNull String text) {
        return FX(new Label(text));
    }

    @NotNull
    public static Component<Hyperlink> Hyperlink(@NotNull String text) {
        return FX(new Hyperlink(text));
    }

    @NotNull
    public static Component<VBox> MenuBar(@NotNull Consumer<Collection<Menu>> builder) {
        var menuBar = new MenuBar();
        builder.accept(menuBar.getMenus());
        return FX(new VBox(menuBar));
    }

}
