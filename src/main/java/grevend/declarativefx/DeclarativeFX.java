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

package grevend.declarativefx;

import grevend.declarativefx.component.Component;
import grevend.declarativefx.test.DeclarativeFXRuntime;
import grevend.declarativefx.test.Fixture;
import grevend.declarativefx.test.Robot;
import grevend.declarativefx.util.MarkedTreeItem;
import grevend.declarativefx.util.Verbosity;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

import static grevend.declarativefx.component.Layout.TreeView;

public class DeclarativeFX {

    private static final String VERSION = "0.6.9";

    private static final Object MUTEX = new Object();
    private static volatile DeclarativeFX INSTANCE;
    private Mode mode;
    private Stage stage;
    private Scene scene;
    private Component<? extends Parent> root;

    @Contract(pure = true)
    private DeclarativeFX() {
        this.mode = Mode.RELEASE;
    }

    @Contract(pure = true)
    private DeclarativeFX(@NotNull Component<? extends Parent> component, @NotNull Stage stage) {
        this.root = component;
        this.stage = stage;
    }

    @Contract(pure = true)
    public static void show(@NotNull Component<? extends Parent> component, @NotNull Stage stage) {
        getInstance().stage = stage;
        getInstance().show(component);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static DeclarativeFX launch(@NotNull Component<? extends Parent> component, @NotNull Stage parentStage, @NotNull Modality modality) {
        var stage = new Stage();
        var scene = new Scene(component.getNode());
        var declarativeFX = new DeclarativeFX(component, stage);
        stage.initModality(modality);
        stage.initOwner(parentStage);
        stage.setScene(scene);
        stage.show();
        declarativeFX.setScene(scene);
        return declarativeFX;
    }

    /**
     * @param component
     * @param <N>
     * @param <C>
     *
     * @return
     *
     * @since 0.6.1
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static <N extends Node, C extends Component<N>> Fixture<N, C> fixture(@NotNull C component) {
        return new Fixture<>(component);
    }


    /**
     * @param headless
     *
     * @return
     *
     * @throws AWTException
     * @since 0.6.5
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static Robot robot(boolean headless) throws AWTException {
        return new Robot(DeclarativeFXRuntime.scene(), headless);
    }

    /**
     * @param scene
     * @param headless
     *
     * @return
     *
     * @throws AWTException
     * @since 0.6.5
     */
    @NotNull
    @Contract(value = "_, _ -> new", pure = true)
    public static Robot robot(@NotNull Scene scene, boolean headless) throws AWTException {
        return new Robot(scene, headless);
    }

    /**
     * @param scene
     *
     * @return
     *
     * @throws AWTException
     * @since 0.6.1
     */
    @NotNull
    @Contract(value = "_ -> new", pure = true)
    public static Robot robot(@NotNull Scene scene) throws AWTException {
        return robot(scene, true);
    }

    //TODO supervisor support

    @NotNull
    public static WritableImage snapshot(@NotNull Component<? extends Node> component) {
        return component.getNode().snapshot(null, null);
    }

    @NotNull
    public static <N extends Node> String stringifyHierarchy(@NotNull Component<N> component, @NotNull Verbosity verbosity) {
        var builder = new StringBuilder();
        stringifyHierarchy(component, builder, "", "", verbosity);
        return builder.toString();
    }

    private static <N extends Node> void stringifyHierarchy(@NotNull Component<N> component, @NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix, @NotNull Verbosity verbosity) {
        builder.append(prefix).append(component.stringify(verbosity)).append(System.lineSeparator());
        for (var componentIter = component.getChildren().iterator(); componentIter.hasNext(); ) {
            var nextComponent = componentIter.next();
            if (nextComponent != component) {
                var hasNextComponent = componentIter.hasNext();
                stringifyHierarchy(nextComponent, builder, childPrefix + (hasNextComponent ? "├── " : "└── "),
                    childPrefix + (hasNextComponent ? "│   " : "    "), verbosity);
            }
        }
    }

    @NotNull
    public static <N extends Node> Component<TreeView<String>> treeifyHierarchy(@NotNull Component<N> component, @NotNull Verbosity verbosity) {
        Integer[] marker = new Integer[]{0};
        var item = new MarkedTreeItem<>(component.stringify(verbosity), marker[0]);
        component.set("marker", marker[0]);
        item.setExpanded(true);
        component.getChildren().forEach(child -> {
            marker[0]++;
            treeifyHierarchy(child, marker, item, verbosity);
        });
        return TreeView(item);
    }

    private static <N extends Node> void treeifyHierarchy(@NotNull Component<N> component, @NotNull Integer[] marker, @NotNull MarkedTreeItem<String> parent, @NotNull Verbosity verbosity) {
        var item = new MarkedTreeItem<>(component.stringify(verbosity), marker[0]);
        component.set("marker", marker[0]);
        item.setExpanded(true);
        parent.getChildren().add(item);
        component.getChildren().forEach(child -> {
            marker[0]++;
            treeifyHierarchy(child, marker, item, verbosity);
        });
    }

    @Nullable
    @Contract(pure = true)
    public static Component<? extends Node> findById(@NotNull String id, @NotNull Component<? extends Parent> root) {
        for (Component<?> component : root) {
            var componentId = component.get("id");
            if (componentId != null && componentId.equals(id)) {
                return component;
            }
        }
        return null;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static Collection<Component<? extends Node>> findByClass(@NotNull String clazz, @NotNull Component<? extends Parent> root) {
        var components = new ArrayList<Component<? extends Node>>();
        for (Component<?> component : root) {
            if (((Collection<String>) Objects.requireNonNull(component.get("styleclass"))).contains(clazz)) {
                components.add(component);
            }
        }
        return components;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public static <N extends Node> Collection<Component<N>> findByNode(@NotNull Class<N> clazz, @NotNull Component<? extends Parent> root) {
        var components = new ArrayList<Component<N>>();
        for (Component<?> component : root) {
            if (clazz.isInstance(component.getNode())) {
                components.add((Component<N>) component);
            }
        }
        return components;
    }

    @Nullable
    @Contract(pure = true)
    public static Component<? extends Node> findByMarker(int marker, @NotNull Component<? extends Parent> root) {
        for (Component<?> component : root) {
            var componentMarker = component.get("marker");
            if (componentMarker != null && componentMarker.equals(marker)) {
                return component;
            }
        }
        return null;
    }

    @NotNull
    public static Collection<Component<? extends Node>> findMarked(@NotNull Component<? extends Parent> root) {
        var components = new ArrayList<Component<? extends Node>>();
        for (Component<?> component : root) {
            if (component.isMarked()) {
                components.add(component);
            }
        }
        return components;
    }

    @NotNull
    public static Collection<Component<? extends Node>> findDecorated(@NotNull Component<? extends Parent> root) {
        var components = new ArrayList<Component<? extends Node>>();
        for (Component<?> component : root) {
            if (component.isDecorated()) {
                components.add(component);
            }
        }
        return components;
    }

    @NotNull
    public static DeclarativeFX getInstance() {
        var result = INSTANCE;
        if (result == null) {
            synchronized (MUTEX) {
                result = INSTANCE;
                if (result == null) INSTANCE = result = new DeclarativeFX();
            }
        }
        return result;
    }

    @Contract(pure = true)
    public void show(@NotNull Component<? extends Parent> component) {
        this.root = component;
        stage.setScene((this.scene = new Scene(component.getNode())));
        stage.show();
    }

    @NotNull
    public WritableImage snapshot() {
        return this.scene.snapshot(null);
    }

    public void addStylesheet(@NotNull String stylesheet, @NotNull Class<?> clazz) {
        if (this.getScene() == null) {
            throw new IllegalStateException("Scene has not been constructed yet.");
        } else {
            if (this.mode == Mode.DEBUG) {
                File file = new File("src/main/resources" + stylesheet);
                if (file.exists()) {
                    this.getScene().getStylesheets().add(
                        (System.getProperty("os.name").toLowerCase().contains("win") ? "file:/" : "file://") +
                            file.getAbsolutePath().replace("\\", "/"));
                    return;
                }
            }
            this.getScene().getStylesheets().add(clazz.getResource(stylesheet).toExternalForm());
        }
    }

    public void removeStylesheet(@NotNull String stylesheet, @NotNull Class<?> clazz) {
        if (this.getScene() == null) {
            throw new IllegalStateException("Scene has not been constructed yet.");
        } else {
            if (this.mode == Mode.DEBUG) {
                File file = new File("src/main/resources" + stylesheet);
                if (file.exists()) {
                    this.getScene().getStylesheets().remove(
                        (System.getProperty("os.name").toLowerCase().contains("win") ? "file:/" : "file://") +
                            file.getAbsolutePath().replace("\\", "/"));
                    return;
                }
            }
            this.getScene().getStylesheets().remove(clazz.getResource(stylesheet).toExternalForm());
        }
    }

    @NotNull
    public Collection<String> getStylesheets() {
        if (this.getScene() == null) {
            throw new IllegalStateException("Scene has not been constructed yet.");
        } else {
            return this.getScene().getStylesheets();
        }
    }

    public void reloadStylesheets() {
        var stylesheets = new ArrayList<>(this.getStylesheets());
        this.getStylesheets().clear();
        Objects.requireNonNull(this.getScene()).getRoot().setStyle("-declarativefx-cache-reset: all;");
        this.getStylesheets().addAll(stylesheets);
    }

    public void enableDeveloperKeyboardShortcuts() {
        if (this.mode != Mode.DEBUG) {
            throw new IllegalStateException("Method enableDeveloperKeyboardShortcuts can only be used in DEBUG mode.");
        } else {
            if (this.getScene() != null) {
                this.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.F5), this::reloadStylesheets);
            }
        }
    }

    @NotNull
    public String stringifyHierarchy(@NotNull Verbosity verbosity) {
        var builder = new StringBuilder();
        stringifyHierarchy(this.root, builder, "", "", verbosity);
        return builder.toString();
    }

    @NotNull
    public Component<TreeView<String>> treeifyHierarchy(@NotNull Verbosity verbosity) {
        return treeifyHierarchy(this.root, verbosity);
    }

    @Nullable
    @Contract(pure = true)
    public Component<? extends Node> findById(@NotNull String id) {
        for (Component<?> component : this.root) {
            var componentId = component.get("id");
            if (componentId != null && componentId.equals(id)) {
                return component;
            }
        }
        return null;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public Collection<Component<? extends Node>> findByClass(@NotNull String clazz) {
        var components = new ArrayList<Component<? extends Node>>();
        for (Component<?> component : this.root) {
            if (((Collection<String>) Objects.requireNonNull(component.get("styleclass"))).contains(clazz)) {
                components.add(component);
            }
        }
        return components;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    public <N extends Node> Collection<Component<N>> findByNode(@NotNull Class<N> clazz) {
        var components = new ArrayList<Component<N>>();
        for (Component<?> component : this.root) {
            if (clazz.isInstance(component.getNode())) {
                components.add((Component<N>) component);
            }
        }
        return components;
    }

    @Nullable
    @Contract(pure = true)
    public Component<? extends Node> findByMarker(int marker) {
        for (Component<?> component : this.root) {
            var componentMarker = component.get("marker");
            if (componentMarker != null && componentMarker.equals(marker)) {
                return component;
            }
        }
        return null;
    }

    @NotNull
    public Collection<Component<? extends Node>> findMarked() {
        var components = new ArrayList<Component<? extends Node>>();
        for (Component<?> component : this.root) {
            if (component.isMarked()) {
                components.add(component);
            }
        }
        return components;
    }

    @NotNull
    public Collection<Component<? extends Node>> findDecorated() {
        var components = new ArrayList<Component<? extends Node>>();
        for (Component<?> component : this.root) {
            if (component.isDecorated()) {
                components.add(component);
            }
        }
        return components;
    }

    @Nullable
    public Component<? extends Parent> getRoot() {
        return this.root;
    }

    @Nullable
    public Stage getStage() {
        return stage;
    }

    protected void setStage(@NotNull Stage stage) {
        this.stage = stage;
    }

    @Nullable
    public Scene getScene() {
        return scene;
    }

    protected void setScene(@NotNull Scene scene) {
        this.scene = scene;
    }

    @NotNull
    public Mode getMode() {
        return mode;
    }

    public void setMode(@NotNull Mode mode) {
        this.mode = mode;
    }

    public enum Mode {
        RELEASE, DEBUG;
    }

}
