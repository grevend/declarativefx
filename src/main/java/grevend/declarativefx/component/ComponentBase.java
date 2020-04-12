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

import grevend.declarativefx.bindable.BindableCollection;
import grevend.declarativefx.bindable.BindableValue;
import grevend.declarativefx.iterator.ComponentIterator;
import grevend.declarativefx.util.Verbosity;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class ComponentBase<N extends Node> implements Component<N> {

    private final N node;
    private final Map<String, BindableValue> properties;
    private int marker;
    private BindableCollection<Component<? extends Node>> children;

    @Contract(pure = true)
    public ComponentBase(@NotNull N node) {
        this.node = node;
        this.marker = -1;
        this.children = BindableCollection.empty();
        this.children.subscribe((change, components) -> addNodes());
        this.properties = new HashMap<>();
    }

    private void addNodes() {
        if (this.node instanceof Pane && !(this.node instanceof BorderPane)) {
            var pane = ((Pane) this.node);
            pane.getChildren().clear();
            for (Component<? extends Node> component : this.children) {
                var child = component.getNode();
                if (child != node) {
                    pane.getChildren().add(child);
                }
            }
        }
    }

    @NotNull
    @Override
    public N getNode() {
        return this.node;
    }

    @NotNull
    @Override
    public BindableCollection<Component<? extends Node>> getChildren() {
        return children;
    }

    @NotNull
    @Override
    public Component<N> setChildren(BindableCollection<Component<? extends Node>> children) {
        this.children = children;
        this.children.subscribe((change, components) -> addNodes());
        addNodes();
        return this;
    }

    @NotNull
    @Override
    public Component<N> addChild(@NotNull Component<? extends Node> child) {
        this.children.add(child);
        return this;
    }

    @NotNull
    @Override
    public Component<N> removeChild(@NotNull Component<? extends Node> child) {
        this.children.remove(child);
        return this;
    }

    @NotNull
    @Override
    public Component<N> addChildren(@NotNull Collection<Component<? extends Node>> children) {
        this.children.addAll(children);
        return this;
    }

    @NotNull
    @Override
    public Component<N> removeChildren(@NotNull Collection<Component<? extends Node>> children) {
        this.children.removeAll(children);
        return this;
    }

    @NotNull
    @Override
    public Component<N> removeClass(@NotNull String clazz) {
        this.node.getStyleClass().remove(clazz);
        return this;
    }

    @NotNull
    @Override
    public Component<N> addClass(@NotNull String clazz) {
        this.node.getStyleClass().add(clazz);
        return this;
    }

    protected int getMarker() {
        return this.marker;
    }

    @NotNull
    protected Component<N> setMarker(int marker) {
        this.marker = marker;
        return this;
    }

    @Override
    public boolean isMarked() {
        return this.getMarker() != -1;
    }

    @NotNull
    protected Map<String, BindableValue> getProperties() {
        return this.properties;
    }

    @Nullable
    protected BindableValue getProperty(@NotNull String property) {
        return this.properties.get(property);
    }

    @NotNull
    protected Component<N> setProperty(@NotNull String property, @NotNull BindableValue value) {
        this.properties.put(property, value);
        return this;
    }

    @NotNull
    @Override
    public Iterator<Component<? extends Node>> iterator() {
        return new ComponentIterator(this);
    }

    @Override
    public @NotNull String stringify(@NotNull Verbosity verbosity) {
        return this.getNode().getClass().getTypeName() + ("@" + Integer.toHexString(this.hashCode())) +
            (this.get("id") != null ? ("#" + this.get("id")) : "") + (this.getNode().getStyleClass().size() > 0 ?
            ("|" + String.join(",", this.getNode().getStyleClass())) : "");
    }

    @Override
    public String toString() {
        return this.stringify(Verbosity.DETAILED);
    }

}
