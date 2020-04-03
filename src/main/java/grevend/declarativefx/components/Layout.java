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
import grevend.declarativefx.properties.Lifecycle;
import grevend.declarativefx.util.BindableValue;
import grevend.declarativefx.util.GridBuilder;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static grevend.declarativefx.components.Compat.FX;

public class Layout {

    public static @NotNull Component<Text> Text() {
        return Text("");
    }

    public static @NotNull Component<Text> Text(@NotNull String text) {
        return FX(new Text(text)).setDefaultProperty("text").bind(new BindableValue(text));
    }

    public static @NotNull Component<Text> Text(@NotNull BindableValue bindableValue) {
        return Text(bindableValue,
            (value) -> value == null ? "" : (value instanceof String ? (String) value : value.toString()));
    }

    public static @NotNull Component<Text> Text(@NotNull BindableValue bindableValue,
                                                @NotNull Function<Object, String> function) {
        var node = new Text();
        bindableValue.subscribe(value -> node.setText(function.apply(value)));
        return FX(node).setDefaultProperty("text").bind(bindableValue);
    }

    public static @NotNull Component<ImageView> Image(@NotNull Image image) {
        return FX(new ImageView(image));
    }

    public static @NotNull Component<ImageView> Image(@NotNull String image) {
        return FX(new ImageView(new Image(image)));
    }

    public static @NotNull Component<ImageView> Image(@NotNull String image, @NotNull double[] imgSize) {
        return FX(new ImageView(new Image(image, imgSize[0], imgSize[1], true, true)));
    }

    public static @NotNull FX<Separator> Separator(Orientation orientation) {
        return FX(new Separator(orientation));
    }
    public static @NotNull FX<Separator> Separator() {
        return FX(new Separator());
    }

    @SafeVarargs
    public static @NotNull Component<HBox> HBox(@NotNull Component<? extends Node>... components) {
        return FX(new HBox(), components);
    }

    public static @NotNull Component<HBox> HBox(@NotNull Collection<Component<? extends Node>> components) {
        return FX(new HBox(), components);
    }

    @SafeVarargs
    public static @NotNull Component<VBox> VBox(@NotNull Component<? extends Node>... components) {
        return FX(new VBox(), components);
    }

    public static @NotNull Component<VBox> VBox(@NotNull Collection<Component<? extends Node>> components) {
        return FX(new VBox(), components);
    }

    @SafeVarargs
    public static @NotNull <N extends Node> Component<ListView<N>> ListView(@NotNull Component<N>... components) {
        var listView = new ListView<N>();
        for (Component<N> component : components) {
            listView.getItems().add(component.construct());
        }
        return FX(listView);
    }

    public static @NotNull <N extends Node> Component<ListView<N>> ListView(
        @NotNull Collection<Component<N>> components) {
        var listView = new ListView<N>();
        listView.getItems().addAll(components.stream().map(Lifecycle::construct).collect(Collectors.toList()));
        return FX(listView);
    }

    public static @NotNull Component<ScrollPane> ScrollPane(@NotNull Component<? extends Node> component) {
        return FX(new ScrollPane(component.construct()), component);
    }

    public static @NotNull Component<BorderPane> BorderPane(@NotNull Component<? extends Node> centerComponent) {
        return FX(new BorderPane(centerComponent.construct()), centerComponent);
    }

    public static @NotNull Component<BorderPane> BorderPane(@NotNull Component<? extends Node> centerComponent,
                                                            @NotNull Component<? extends Node> rightComponent,
                                                            @NotNull Component<? extends Node> leftComponent) {
        return FX(new BorderPane(centerComponent.construct(), null, rightComponent.construct(), null,
            leftComponent.construct()), centerComponent, rightComponent, leftComponent);
    }

    public static @NotNull Component<BorderPane> BorderPane(@Nullable Component<? extends Node> centerComponent,
                                                            @Nullable Component<? extends Node> topComponent,
                                                            @Nullable Component<? extends Node> rightComponent,
                                                            @Nullable Component<? extends Node> bottomComponent,
                                                            @Nullable Component<? extends Node> leftComponent) {
        return FX(new BorderPane(centerComponent != null ? centerComponent.construct() : null,
                topComponent != null ? topComponent.construct() : null,
                rightComponent != null ? rightComponent.construct() : null,
                bottomComponent != null ? bottomComponent.construct() : null,
                leftComponent != null ? leftComponent.construct() : null), centerComponent, topComponent, rightComponent,
            bottomComponent, leftComponent);
    }

    public static @NotNull Component<GridPane> GridPane(@NotNull Consumer<GridBuilder> builder) {
        var gridPane = new GridPane();
        var gridBuilder = new GridBuilder();
        builder.accept(gridBuilder);
        gridBuilder.getComponents().forEach(component -> {
            gridPane.add(component.getA().construct(), component.getB(), component.getC());
        });
        return FX(gridPane);
    }

    public static @NotNull <C extends Node> Component<GridPane> GridPane(@NotNull Collection<Component<C>> components,
                                                                         @NotNull BiConsumer<Collection<Component<C>>, GridBuilder> builder) {
        var gridPane = new GridPane();
        var gridBuilder = new GridBuilder();
        builder.accept(components, gridBuilder);
        gridBuilder.getComponents().forEach(component -> {
            gridPane.add(component.getA().construct(), component.getB(), component.getC());
        });
        return FX(gridPane);
    }

    public static @NotNull TreeItem<String> TreeItem(@NotNull String item) {
        return new TreeItem<>(item);
    }

    public static @NotNull TreeItem<String> TreeItem(@NotNull String item, boolean expanded) {
        var treeItem = new TreeItem<>(item);
        treeItem.setExpanded(expanded);
        return treeItem;
    }

    @SafeVarargs
    public static @NotNull TreeItem<String> TreeItem(@NotNull String item, boolean expanded,
                                                     @NotNull TreeItem<String>... items) {
        var treeItem = new TreeItem<>(item);
        treeItem.setExpanded(expanded);
        treeItem.getChildren().addAll(Arrays.asList(items));
        return treeItem;
    }

    public static @NotNull TreeItem<String> TreeItem(@NotNull String item, boolean expanded,
                                                     @NotNull Collection<TreeItem<String>> items) {
        var treeItem = new TreeItem<>(item);
        treeItem.setExpanded(expanded);
        treeItem.getChildren().addAll(items);
        return treeItem;
    }

    @SafeVarargs
    public static @NotNull TreeItem<String> TreeItem(@NotNull String item, @NotNull TreeItem<String>... items) {
        var treeItem = new TreeItem<>(item);
        treeItem.getChildren().addAll(Arrays.asList(items));
        return treeItem;
    }

    public static @NotNull TreeItem<String> TreeItem(@NotNull String item,
                                                     @NotNull Collection<TreeItem<String>> items) {
        var treeItem = new TreeItem<>(item);
        treeItem.getChildren().addAll(items);
        return treeItem;
    }

    public static @NotNull Component<TreeView<String>> TreeView(@NotNull TreeItem<String> root) {
        return FX(new TreeView<>(root));
    }

    public static @NotNull Component<TableView<String[]>> TableView(@NotNull Collection<String[]> row,
                                                                    @NotNull String... columns) {
        TableView<String[]> tableView = new TableView<>();
        var index = 0;
        for (String column : columns) {
            TableColumn<String[], String> tableColumn = new TableColumn<>(column);
            int finalIndex = index;
            tableColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()[finalIndex]));
            tableView.getColumns().add(tableColumn);
            index++;
        }
        tableView.setItems(FXCollections.observableArrayList(row));
        return FX(tableView);
    }

    public static @NotNull Component<TableView<String[]>> TableView(@NotNull Collection<String[]> row,
                                                                    @NotNull String[] columns,
                                                                    @NotNull double[] minWidth) {
        if (columns.length != minWidth.length) {
            throw new IllegalArgumentException("Amount of columns does not match given amount of widths.");
        }
        TableView<String[]> tableView = new TableView<>();
        var index = 0;
        for (String column : columns) {
            TableColumn<String[], String> tableColumn = new TableColumn<>(column);
            int finalIndex = index;
            tableColumn.setCellValueFactory(p -> new SimpleObjectProperty<>(p.getValue()[finalIndex]));
            tableColumn.setMinWidth(minWidth[finalIndex]);
            tableView.getColumns().add(tableColumn);
            index++;
        }
        tableView.setItems(FXCollections.observableArrayList(row));
        return FX(tableView);
    }

}
