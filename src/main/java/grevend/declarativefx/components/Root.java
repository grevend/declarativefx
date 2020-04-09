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
import grevend.declarativefx.bindable.BindableValue;
import grevend.declarativefx.lifecycle.LifecycleException;
import grevend.declarativefx.lifecycle.LifecyclePhase;
import grevend.declarativefx.util.Measurable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Root<P extends Parent> extends Component<P> {

    private final Map<String, BindableValue> providers;
    private LifecyclePhase phase;
    private Map<Measurable, Duration> measurements;
    private Stage stage;
    private Scene scene;

    public Root(@NotNull Component<P> component) {
        super(component);
        this.providers = new HashMap<>();
        this.measurements = new HashMap<>();
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

    @Override
    public @NotNull Component<P> setId(@NotNull String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable String getId() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Component<P> addClass(@NotNull String clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Component<P> removeClass(@NotNull String clazz) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Component<P> setStyle(@NotNull String style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String getStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public @Nullable Component<? extends Node> findById(@NotNull String id, boolean root) {
        if (this.getChildren().size() == 1) {
            return this.getChildren().iterator().next().findById(id, false);
        }
        return null;
    }

    @Override
    public @NotNull Collection<Component<? extends Node>> findByClass(
        @NotNull Collection<Component<? extends Node>> components, @NotNull String clazz, boolean root) {
        if (this.getChildren().size() == 1) {
            this.getChildren().iterator().next().findByClass(components, clazz, false);
        }
        return components;
    }

    @Override
    public void beforeConstruction() {
        this.phase = LifecyclePhase.BEFORE_CONSTRUCTION;
        if (this.measurements.containsKey(this.phase)) {
            throw new LifecycleException("Phase " + this.phase.toString().toLowerCase() + " has already been invoked.");
        }
        var start = Instant.now();
        super.beforeConstruction();
        var end = Instant.now();
        this.measurements.put(LifecyclePhase.BEFORE_CONSTRUCTION, Duration.between(start, end));
    }

    @Override
    public @Nullable P construct() {
        this.phase = LifecyclePhase.CONSTRUCTION;
        if (this.measurements.containsKey(this.phase)) {
            throw new LifecycleException("Phase " + this.phase.toString().toLowerCase() + " has already been invoked.");
        }
        var start = Instant.now();
        var node = super.construct();
        var end = Instant.now();
        this.measurements.put(LifecyclePhase.CONSTRUCTION, Duration.between(start, end));
        return node;
    }

    @Override
    public void afterConstruction() {
        this.phase = LifecyclePhase.AFTER_CONSTRUCTION;
        if (this.measurements.containsKey(this.phase)) {
            throw new LifecycleException("Phase " + this.phase.toString().toLowerCase() + " has already been invoked.");
        }
        var start = Instant.now();
        super.afterConstruction();
        var end = Instant.now();
        this.measurements.put(LifecyclePhase.AFTER_CONSTRUCTION, Duration.between(start, end));
    }

    @Override
    public void deconstruct() {
        this.phase = LifecyclePhase.DECONSTRUCTION;
        if (this.measurements.containsKey(this.phase)) {
            throw new LifecycleException("Phase " + this.phase.toString().toLowerCase() + " has already been invoked.");
        }
        var start = Instant.now();
        super.deconstruct();
        var end = Instant.now();
        this.measurements.put(LifecyclePhase.DECONSTRUCTION, Duration.between(start, end));
    }

    @Override
    public @NotNull String toString() {
        var builder = new StringBuilder();
        builder.append(this.getClass().getTypeName());
        if (!this.measurements.isEmpty()) {
            builder.append(" (").append(this.measurements.entrySet().stream().map(
                entry -> entry.getKey().toString().toLowerCase() + ": " +
                    (entry.getValue().toMillis() == 0 ? (entry.getValue().toNanos() + "ns") :
                        (entry.getValue().toMillis() + "ms"))).collect(Collectors.joining(", "))).append(")");
        }
        return builder.toString();
    }

    @Override
    public @NotNull String stringify() {
        return this.toString();
    }

    @Override
    public void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix,
                                   @NotNull Verbosity verbosity) {
        builder.append(prefix).append(this.toString()).append(System.lineSeparator());
        if (this.getChildren().size() == 1) {
            this.getChildren().iterator().next()
                .stringifyHierarchy(builder, childPrefix + "└── ", childPrefix + "    ", verbosity);
        } else {
            throw new LifecycleException();
        }
    }

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

    public @NotNull LifecyclePhase getLifecyclePhase() {
        if (phase == null) {
            throw new LifecycleException("Lifecycle has not yet started.");
        }
        return phase;
    }

    public @NotNull Map<Measurable, Duration> getMeasurements() {
        return measurements;
    }

    public @NotNull Root<P> addStylesheet(@NotNull String stylesheet, @NotNull Class<?> clazz) {
        if (this.getScene() != null) {
            this.getScene().getStylesheets().add(clazz.getResource(stylesheet).toExternalForm());
        } else {
            throw new LifecycleException("Scene has not been constructed yet.");
        }
        return this;
    }

    public @NotNull Root<P> removeStylesheet(@NotNull String stylesheet, @NotNull Class<?> clazz) {
        if (this.getScene() != null) {
            this.getScene().getStylesheets().remove(clazz.getResource(stylesheet).toExternalForm());
        } else {
            throw new LifecycleException("Scene has not been constructed yet.");
        }
        return this;
    }

    public @NotNull Collection<String> getStylesheets() {
        if (this.getScene() != null) {
            return this.getScene().getStylesheets();
        } else {
            throw new LifecycleException("Scene has not been constructed yet.");
        }
    }

}