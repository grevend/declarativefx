package grevend.declarativefx.components;

import grevend.declarativefx.Component;
import grevend.declarativefx.properties.Identifiable;
import grevend.declarativefx.util.BindableValue;
import grevend.declarativefx.util.LifecycleException;
import grevend.declarativefx.util.VarArgsFunction;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Binding<N extends Node, V, U> extends Component<N> implements Identifiable<N, Binding<N, V, U>> {

    private final String[] identifiers;
    private final BindableValue<?>[] values;

    private String id;
    private Component<N> child;

    private Function<BindableValue<V>, ? extends Component<N>> functionOne;
    private BiFunction<BindableValue<V>, BindableValue<U>, ? extends Component<N>> functionTwo;
    private VarArgsFunction<BindableValue<?>, ? extends Component<N>> functionVarArgs;

    private Binding(@NotNull String[] identifiers) {
        this.identifiers = identifiers;
        this.values = new BindableValue[this.identifiers.length];
        for (var i = 0; i < this.identifiers.length; i++) {
            this.values[i] = new BindableValue<>();
        }
    }

    public Binding(@NotNull String id, @NotNull Function<BindableValue<V>, ? extends Component<N>> function) {
        this(new String[]{id});
        this.functionOne = function;
    }

    public Binding(@NotNull String first, @NotNull String second,
                   @NotNull BiFunction<BindableValue<V>, BindableValue<U>, ? extends Component<N>> function) {
        this(new String[]{first, second});
        this.functionTwo = function;
    }

    public Binding(@NotNull String[] identifiers,
                   @NotNull VarArgsFunction<BindableValue<?>, ? extends Component<N>> function) {
        this(identifiers);
        this.functionVarArgs = function;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable N construct() {
        if (identifiers.length == 1) {
            return (this.child = this.functionOne.apply((BindableValue<V>) this.values[0])).construct();
        } else if (identifiers.length == 2) {
            return (this.child =
                this.functionTwo.apply((BindableValue<V>) this.values[0], (BindableValue<U>) this.values[1]))
                .construct();
        } else if (identifiers.length > 2) {
            return (this.child = this.functionVarArgs.apply(this.values)).construct();
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
    public Binding<N, V, U> setId(@NotNull String id) {
        this.id = id;
        return this;
    }

}