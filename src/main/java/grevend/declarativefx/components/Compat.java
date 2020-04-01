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
import grevend.declarativefx.util.VarArgsFunction;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Compat {

    public static @NotNull <P extends Parent> Root<P> Root(@NotNull Component<P> component) {
        return new Root<>(component);
    }

    @Deprecated
    public static @NotNull <N extends Node> Component<N> Binding(@NotNull String id,
                                                                 @NotNull Function<BindableValue, ? extends Component<N>> function) {
        return new Binding<>(id, function);
    }

    @Deprecated
    public static @NotNull <N extends Node> Component<N> Binding(@NotNull String first, @NotNull String second,
                                                                 @NotNull BiFunction<BindableValue, BindableValue, ? extends Component<N>> function) {
        return new Binding<>(first, second, function);
    }

    @Deprecated
    public static @NotNull <N extends Node> Component<N> Binding(@NotNull String[] identifiers,
                                                                 @NotNull VarArgsFunction<BindableValue, ? extends Component<N>> function) {
        return new Binding<>(identifiers, function);
    }

    public static @NotNull <N extends Node> FX<N> FX(@Nullable N node) {
        return new FX<>(node);
    }

    public static @NotNull <N extends Node> FX<N> FX(@Nullable N node, @NotNull String property) {
        return new FX<>(node, property);
    }

    @SafeVarargs
    public static @NotNull <P extends Pane> FXContainer<P> FXContainer(@NotNull P pane,
                                                                       @NotNull Component<? extends Node>... components) {
        return new FXContainer<>(pane, components);
    }

    public static @NotNull <P extends Pane> FXContainer<P> FXContainer(@NotNull P pane,
                                                                       @NotNull Collection<Component<? extends Node>> components) {
        return new FXContainer<>(pane, components);
    }

}
