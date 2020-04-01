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
import grevend.declarativefx.properties.Findable;
import grevend.declarativefx.properties.Identifiable;
import grevend.declarativefx.util.BindableValue;
import grevend.declarativefx.util.LifecycleException;
import grevend.declarativefx.util.VarArgsFunction;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class Binding<N extends Node, V, U> extends Component<N>
    implements Identifiable<N, Binding<N, V, U>>, Findable<N, Binding<N, V, U>> {

    private final String[] identifiers;
    private final BindableValue[] values;

    private String id;
    private Component<N> child;

    private Function<BindableValue, ? extends Component<N>> functionOne;
    private BiFunction<BindableValue, BindableValue, ? extends Component<N>> functionTwo;
    private VarArgsFunction<BindableValue, ? extends Component<N>> functionVarArgs;

    private Binding(@NotNull String[] identifiers) {
        this.identifiers = identifiers;
        this.values = new BindableValue[this.identifiers.length];
        for (var i = 0; i < this.identifiers.length; i++) {
            this.values[i] = new BindableValue();
        }
    }

    public Binding(@NotNull String id, @NotNull Function<BindableValue, ? extends Component<N>> function) {
        this(new String[]{id});
        this.functionOne = function;
        this.initChild();
    }

    public Binding(@NotNull String first, @NotNull String second,
                   @NotNull BiFunction<BindableValue, BindableValue, ? extends Component<N>> function) {
        this(new String[]{first, second});
        this.functionTwo = function;
        this.initChild();
    }

    public Binding(@NotNull String[] identifiers,
                   @NotNull VarArgsFunction<BindableValue, ? extends Component<N>> function) {
        this(identifiers);
        this.functionVarArgs = function;
        this.initChild();
    }

    public Binding(@NotNull Iterable<String> identifiers,
                   @NotNull VarArgsFunction<BindableValue, ? extends Component<N>> function) {
        this(StreamSupport.stream(identifiers.spliterator(), false).toArray(String[]::new));
        this.functionVarArgs = function;
        this.initChild();
    }

    private void initChild() {
        if (identifiers.length == 1) {
            this.child = this.functionOne.apply((BindableValue) this.values[0]);
        } else if (identifiers.length == 2) {
            this.child = this.functionTwo.apply((BindableValue) this.values[0], (BindableValue) this.values[1]);
        } else if (identifiers.length > 2) {
            this.child = this.functionVarArgs.apply(this.values);
        }
        if (this.child != null) {
            this.child.setParent(this);
        }
    }

    @Override
    public @Nullable N construct() {
        if (this.child != null) {
            return this.child.construct();
        }
        return null;
    }

    @Override
    public void afterConstruction() {
        for (var i = 0; i < this.identifiers.length; i++) {
            this.getRoot().getProviders().putIfAbsent(this.identifiers[i], this.values[i]);
        }
    }

    @Override
    public void deconstruct() {
        if (this.child != null) {
            this.child.deconstruct();
        }
    }

    @Override
    public void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix,
                                   @NotNull Verbosity verbosity) {
        super.stringifyHierarchy(builder, prefix, childPrefix, verbosity);
        if (this.child != null) {
            this.child.stringifyHierarchy(builder, childPrefix + "└── ", childPrefix + "    ", verbosity);
        } else {
            throw new LifecycleException("Hierarchy has not been constructed yet.");
        }
    }

    @Override
    public @Nullable String getId() {
        return this.id;
    }

    @Override
    public @NotNull Binding<N, V, U> setId(@NotNull String id) {
        this.id = id;
        return this;
    }

    @Override
    public @Nullable Component<? extends Node> find(@NotNull String id, boolean root) {
        if (this.getId() != null && this.getId().equals(id)) {
            return this;
        } else {
            if (root) {
                return this.getRoot().find(id, false);
            } else if (this.child instanceof Findable) {
                return ((Findable<?, ?>) this.child).find(id, false);
            }
            return null;
        }

    }

}