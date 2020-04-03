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
import grevend.declarativefx.util.LifecycleException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Root<P extends Parent> extends Component<P> {

    private final Map<String, BindableValue> providers;
    private Stage stage;
    private Scene scene;

    public Root(@NotNull Component<P> component) {
        super(component);
        this.providers = new HashMap<>();
    }

    @Override
    public @Nullable Component<? extends Node> getParent() {
        return null;
    }

    @Override
    public void setParent(@NotNull Component<? extends Node> parent) {
        throw new IllegalStateException();
    }

    public @NotNull Map<String, BindableValue> getProviders() {
        return providers;
    }

    @Override
    public @NotNull Root<P> getRoot() {
        return this;
    }

    /*@Override
    public @NotNull String stringify() {
        return this.toString();
    }

    @Override
    public void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix,
                                   @NotNull Verbosity verbosity) {
        super.stringifyHierarchy(builder, prefix, childPrefix, verbosity);
        if (this.component != null) {
            this.component.stringifyHierarchy(builder, childPrefix + "└── ", childPrefix + "    ", verbosity);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
    }*/

    public void launch(@NotNull Stage stage) {
        this.stage = stage;
        this.beforeConstruction();
        var tree = this.construct();
        if (tree != null) {
            this.afterConstruction();
            stage.setScene((this.scene = new Scene(tree)));
            stage.show();
        } else {
            throw new IllegalStateException("Component hierarchy construction failed.");
        }
    }

    public @Nullable Stage getStage() {
        return this.stage;
    }

    public @Nullable Scene getScene() {
        return scene;
    }

    public Root<P> addStylesheet(@NotNull String stylesheet, @NotNull Class<?> clazz) {
        if (this.getScene() != null) {
            this.getScene().getStylesheets().add(clazz.getResource(stylesheet).toExternalForm());
        } else {
            throw new LifecycleException("Scene has not been constructed yet.");
        }
        return this;
    }

    public Root<P> removeStylesheet(@NotNull String stylesheet, @NotNull Class<?> clazz) {
        if (this.getScene() != null) {
            this.getScene().getStylesheets().remove(clazz.getResource(stylesheet).toExternalForm());
        } else {
            throw new LifecycleException("Scene has not been constructed yet.");
        }
        return this;
    }

    public Collection<String> getStylesheets() {
        if (this.getScene() != null) {
            return this.getScene().getStylesheets();
        } else {
            throw new LifecycleException("Scene has not been constructed yet.");
        }
    }

}