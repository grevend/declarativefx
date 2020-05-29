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

import grevend.declarativefx.bindable.Bindable;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static grevend.declarativefx.component.Compat.FX;
import static grevend.declarativefx.component.Compat.fluent;

public class Layout {

    @NotNull
    public static Component<Text> Text(@NotNull String text) {
        return FX(new Text(text));
    }

    @NotNull
    public static Component<ImageView> Image(@NotNull Image image) {
        return FX(new ImageView(image));
    }

    @NotNull
    public static Component<ImageView> Image(@NotNull String image) {
        return FX(new ImageView(new Image(image)));
    }

    @NotNull
    public static Component<ImageView> Image(@NotNull String image, @NotNull double[] imgSize) {
        return FX(new ImageView(new Image(image, imgSize[0], imgSize[1], true, true)));
    }

    @NotNull
    public static Component<Separator> Separator(@NotNull Orientation orientation) {
        return FX(new Separator(orientation));
    }

    @NotNull
    public static Component<ProgressBar> ProgressBar(double progress) {
        return FX(new ProgressBar(progress));
    }

    @NotNull
    public static Component<ProgressBar> ProgressBar(@NotNull DoubleProperty progress) {
        var element = new ProgressBar(0);
        element.progressProperty().bind(progress);
        return FX(element);
    }

    @NotNull
    public static Component<ProgressBar> ProgressBar(@NotNull Bindable bindableValue) {
        var element = new ProgressBar(0);
        bindableValue.subscribe((value) -> element.progressProperty().set((Double) value));
        return FX(element);
    }

    @NotNull
    public static Component<ProgressIndicator> ProgressIndicator(DoubleProperty progress) {
        var element = new ProgressIndicator(0);
        element.progressProperty().bind(progress);
        return FX(element);
    }

    @NotNull
    public static Component<ProgressIndicator> ProgressIndicator(@NotNull Bindable bindableValue) {
        var element = new ProgressIndicator(0);
        bindableValue.subscribe((value) -> element.progressProperty().set((Double) value));
        return FX(element);
    }

    @NotNull
    @SafeVarargs
    public static Component<HBox> HBox(@NotNull Component<? extends Node>... components) {
        return FX(new HBox(), components);
    }

    @NotNull
    public static Component<HBox> HBox(@NotNull Collection<? extends Component<? extends Node>> components) {
        return FX(new HBox(), components);
    }

    @NotNull
    @SafeVarargs
    public static Component<VBox> VBox(@NotNull Component<? extends Node>... components) {
        return FX(new VBox(), components);
    }

    @NotNull
    public static Component<VBox> VBox(@NotNull Collection<? extends Component<? extends Node>> components) {
        return FX(new VBox(), components);
    }

    @NotNull
    public static <N extends Node> Component<ListView<N>> ListView() {
        return FX(new ListView<>());
    }

    @NotNull
    public static Component<ScrollPane> ScrollPane(@NotNull Component<? extends Node> component) {
        return FX(new ScrollPane(component.getNode()), component);
    }

    @NotNull
    public static Component<BorderPane> BorderPane(@NotNull Component<? extends Node> centerComponent) {
        return FX(new BorderPane(centerComponent.getNode()), centerComponent);
    }

    @NotNull
    public static Component<BorderPane> BorderPane(@NotNull Component<? extends Node> centerComponent, @NotNull Component<? extends Node> rightComponent, @NotNull Component<? extends Node> leftComponent) {
        return FX(new BorderPane(centerComponent.getNode(), null, rightComponent.getNode(), null,
            leftComponent.getNode()), centerComponent, rightComponent, leftComponent);
    }

    @NotNull
    public static Component<BorderPane> BorderPane(@Nullable Component<? extends Node> centerComponent, @Nullable Component<? extends Node> topComponent, @Nullable Component<? extends Node> rightComponent, @Nullable Component<? extends Node> bottomComponent, @Nullable Component<? extends Node> leftComponent) {
        var elements = new ArrayList<Component<? extends Node>>();
        elements.add(centerComponent);
        elements.add(topComponent);
        elements.add(rightComponent);
        elements.add(bottomComponent);
        elements.add(leftComponent);
        return FX(new BorderPane(centerComponent != null ? centerComponent.getNode() : null,
            topComponent != null ? topComponent.getNode() : null,
            rightComponent != null ? rightComponent.getNode() : null,
            bottomComponent != null ? bottomComponent.getNode() : null,
            leftComponent != null ? leftComponent.getNode() : null), elements);
    }

    @NotNull
    public static Component<GridPane> GridPane(@NotNull Consumer<GridBuilder> builder) {
        var gridPane = new GridPane();
        var gridBuilder = new GridBuilder();
        builder.accept(gridBuilder);
        gridBuilder.getComponents().forEach(component -> {
            if (component.getA() != null && component.getB() != null && component.getC() != null) {
                gridPane.add(component.getA().getNode(), component.getB(), component.getC());
            }
        });
        return FX(gridPane);
    }

    @NotNull
    public static <C extends Node> Component<GridPane> GridPane(@NotNull Collection<Component<C>> components, @NotNull BiConsumer<Collection<Component<C>>, GridBuilder> builder) {
        var gridPane = new GridPane();
        var gridBuilder = new GridBuilder();
        builder.accept(components, gridBuilder);
        gridBuilder.getComponents().forEach(component -> {
            if (component.getA() != null && component.getB() != null && component.getC() != null) {
                gridPane.add(component.getA().getNode(), component.getB(), component.getC());
            }
        });
        return FX(gridPane);
    }

    @NotNull
    @Contract("_ -> new")
    public static TreeItem<String> TreeItem(@NotNull String item) {
        return new TreeItem<>(item);
    }

    @NotNull
    public static TreeItem<String> TreeItem(@NotNull String item, boolean expanded) {
        var element = new TreeItem<>(item);
        element.setExpanded(expanded);
        return element;
    }

    @NotNull
    @SafeVarargs
    public static TreeItem<String> TreeItem(@NotNull String item, boolean expanded, @NotNull TreeItem<String>... items) {
        var element = new TreeItem<>(item);
        element.setExpanded(expanded);
        element.getChildren().addAll(Arrays.asList(items));
        return element;
    }

    @NotNull
    public static TreeItem<String> TreeItem(@NotNull String item, boolean expanded, @NotNull Collection<TreeItem<String>> items) {
        var element = new TreeItem<>(item);
        element.setExpanded(expanded);
        element.getChildren().addAll(items);
        return element;
    }

    @NotNull
    @SafeVarargs
    public static TreeItem<String> TreeItem(@NotNull String item, @NotNull TreeItem<String>... items) {
        var element = new TreeItem<>(item);
        element.getChildren().addAll(Arrays.asList(items));
        return element;
    }

    @NotNull
    public static TreeItem<String> TreeItem(@NotNull String item, @NotNull Collection<TreeItem<String>> items) {
        var element = new TreeItem<>(item);
        element.getChildren().addAll(items);
        return element;
    }

    @NotNull
    public static Component<TreeView<String>> TreeView(@NotNull TreeItem<String> root) {
        return FX(new TreeView<>(root));
    }

    @NotNull
    @Contract("_ -> new")
    public static Tab Tab(@NotNull String text) {
        return new Tab(text);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static Tab Tab(String title, @NotNull Component<? extends Node> component) {
        return new Tab(title, component.getNode());
    }

    @NotNull
    public static Component<TabPane> TabPane(@NotNull Tab... tabs) {
        return fluent(FX(new TabPane()), pane -> pane.getTabs().addAll(tabs));
    }

    @NotNull
    public static Component<TabPane> TabPane(@NotNull Collection<Tab> tabs) {
        return fluent(FX(new TabPane()), pane -> pane.getTabs().addAll(tabs));
    }

}
