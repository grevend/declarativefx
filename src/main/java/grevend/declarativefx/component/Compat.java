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
import grevend.declarativefx.bindable.Change;
import grevend.declarativefx.decorator.MeasuredComponent;
import grevend.declarativefx.view.Accessor;
import grevend.declarativefx.view.Interactor;
import grevend.declarativefx.view.State;
import grevend.declarativefx.view.View;
import javafx.scene.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static grevend.declarativefx.bindable.BindableCollection.of;

public class Compat {

    @Contract("_ -> new")
    public static @NotNull <N extends Node> Component<N> FX(@NotNull N node) {
        return new FXComponent<>(node);
    }

    @SafeVarargs
    @Contract("_, _ -> new")
    public static @NotNull <N extends Node> Component<N> FX(@NotNull N node, @NotNull Component<? extends Node>... components) {
        return new FXComponent<>(node).setChildren(BindableCollection.of(components));
    }

    @Contract("_, _ -> new")
    @SuppressWarnings("unchecked")
    public static @NotNull <N extends Node> Component<N> FX(@NotNull N node, @NotNull Collection<? extends Component<? extends Node>> components) {
        return new FXComponent<>(node).setChildren(components instanceof BindableCollection ?
            ((BindableCollection<Component<? extends Node>>) components) :
            BindableCollection.of((Collection<Component<? extends Node>>) components));
    }

    @NotNull
    @Contract("_, _ -> param1")
    public static <N extends Node, C extends Component<N>> C self(@NotNull C component, @NotNull Consumer<C> consumer) {
        consumer.accept(component);
        return component;
    }

    @NotNull
    @Contract("_, _ -> param1")
    public static <N extends Node, C extends Component<N>> C fluent(@NotNull C component, @NotNull Consumer<N> consumer) {
        consumer.accept(component.getNode());
        return component;
    }

    @NotNull
    public static <N extends Node, C extends Component<N>, E> C builder(@NotNull C component, @NotNull Collection<E> collection, @NotNull Function<E, Component<? extends Node>> build) {
        if (collection instanceof BindableCollection) {
            ((BindableCollection<E>) collection).subscribe((change, changes) -> {
                component.getChildren().clear();
                var components = new ArrayList<Component<? extends Node>>();
                for (E element : collection) {
                    components.add(build.apply(element));
                }
                components.removeIf(Objects::isNull);
                component.getChildren().addAll(components);
            });
            ((BindableCollection<E>) collection).getConsumers()
                .forEach(consumer -> consumer.accept(Change.NONE, List.of()));
            return component;
        } else {
            return builder(component, of(collection), build);
        }
    }

    @NotNull
    public static <N extends Node, C extends Component<N>, E> C builder(@NotNull C component, @NotNull Collection<E> collection, @NotNull BiFunction<E, Integer, Component<? extends Node>> build) {
        if (collection instanceof BindableCollection) {
            ((BindableCollection<E>) collection).subscribe((change, changes) -> {
                component.getChildren().clear();
                var components = new ArrayList<Component<? extends Node>>();
                int i = 0;
                for (E element : collection) {
                    components.add(build.apply(element, i));
                    i++;
                }
                components.removeIf(Objects::isNull);
                component.getChildren().addAll(components);
            });
            ((BindableCollection<E>) collection).getConsumers()
                .forEach(consumer -> consumer.accept(Change.NONE, List.of()));
            return component;
        } else {
            return builder(component, of(collection), build);
        }
    }

    @NotNull
    @Contract("_ -> new")
    public static <N extends Node, C extends Component<N>> MeasuredComponent<N> measure(@NotNull C component) {
        return new MeasuredComponent<>(component);
    }

    @NotNull
    public static <S extends State, A extends Accessor, I extends Interactor<S, A>> Component<? extends Node> View(@NotNull View<S, A, I> view, @NotNull I interactor) {
        return view.render(interactor);
    }

}
