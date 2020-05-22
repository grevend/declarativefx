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

package grevend.declarativefx.decorator;

import grevend.declarativefx.bindable.Bindable;
import grevend.declarativefx.bindable.BindableCollection;
import grevend.declarativefx.component.Component;
import grevend.declarativefx.event.EventHandler;
import grevend.declarativefx.util.Verbosity;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ComponentDecorator<N extends Node> implements Component<N> {

    private final Component<N> target;

    @Contract(pure = true)
    public ComponentDecorator(@NotNull Component<N> target) {
        this.target = target;
    }

    @NotNull
    public Component<N> getTarget() {
        return target;
    }

    @Override
    public boolean isDecorated() {
        return true;
    }

    @NotNull
    @Override
    public N getNode() {
        return this.target.getNode();
    }

    @NotNull
    @Override
    public BindableCollection<Component<? extends Node>> getChildren() {
        return this.target.getChildren();
    }

    @NotNull
    @Override
    public Component<N> setChildren(BindableCollection<Component<? extends Node>> children) {
        return this.target.setChildren(children);
    }

    @NotNull
    @Override
    public Component<N> addChild(@NotNull Component<? extends Node> child) {
        return this.target.addChild(child);
    }

    @NotNull
    @Override
    public Component<N> removeChild(@NotNull Component<? extends Node> child) {
        return this.target.removeChild(child);
    }

    @NotNull
    @Override
    public Component<N> addClass(@NotNull String clazz) {
        return this.target.addClass(clazz);
    }

    @NotNull
    @Override
    public Component<N> removeClass(@NotNull String clazz) {
        return this.target.removeClass(clazz);
    }

    @NotNull
    @Override
    public Component<N> addChildren(@NotNull Collection<Component<? extends Node>> children) {
        return this.target.addChildren(children);
    }

    @NotNull
    @Override
    public Component<N> removeChildren(@NotNull Collection<Component<? extends Node>> children) {
        return this.target.removeChildren(children);
    }

    @NotNull
    @Override
    public <E extends Event> Component<N> on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler) {
        return this.target.on(type, handler);
    }

    @NotNull
    @Override
    public <T> Component<N> on(@NotNull String property, @NotNull ChangeListener<T> listener) {
        return this.target.on(property, listener);
    }

    @NotNull
    @Override
    public Component<N> on(@NotNull String property, @NotNull InvalidationListener listener) {
        return this.target.on(property, listener);
    }

    @NotNull
    @Override
    public Component<N> bind(@NotNull String property, @NotNull Bindable value) {
        return this.target.bind(property, value);
    }

    @NotNull
    @Override
    public Component<N> compute(@NotNull String property, @NotNull Bindable dependency, @NotNull Function<Bindable, Object> function) {
        return this.target.compute(property, dependency, function);
    }

    @NotNull
    @Override
    public Component<N> compute(@NotNull String property, @NotNull Bindable dependency, @NotNull Supplier<Object> supplier) {
        return this.target.compute(property, dependency, supplier);
    }

    @NotNull
    @Override
    public Component<N> set(@NotNull String property, @Nullable Object value) {
        return this.target.set(property, value);
    }

    @Nullable
    @Override
    public Object get(@NotNull String property) {
        return this.target.get(property);
    }

    @NotNull
    @Override
    public Map<String, Bindable> getProperties() {
        return this.target.getProperties();
    }

    @NotNull
    @Override
    public String stringify(@NotNull Verbosity verbosity) {
        return this.target.stringify(verbosity);
    }

    @NotNull
    @Override
    public Iterator<Component<? extends Node>> iterator() {
        return this.target.iterator();
    }

    @NotNull
    @Override
    public String toString() {
        return "Decorated[" + this.target.toString() + ']';
    }

}
