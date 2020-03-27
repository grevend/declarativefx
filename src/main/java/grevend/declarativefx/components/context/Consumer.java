package grevend.declarativefx.components.context;

import grevend.declarativefx.components.Component;
import grevend.declarativefx.util.ObservableValue;
import grevend.declarativefx.util.VarArgsFunction;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Consumer<N extends Node, V, U> extends Component<N> {

    private final String[] identifiers;
    private final ObservableValue<?>[] values;

    private Function<ObservableValue<V>, ? extends Component<N>> functionOne;
    private BiFunction<ObservableValue<V>, ObservableValue<U>, ? extends Component<N>> functionTwo;
    private VarArgsFunction<ObservableValue<?>, ? extends Component<N>> functionVarArgs;

    private Consumer(@NotNull String[] identifiers) {
        this.identifiers = identifiers;
        this.values = new ObservableValue[this.identifiers.length];
        for (var i = 0; i < this.identifiers.length; i++) {
            this.values[i] = new ObservableValue<>();
        }
    }

    public Consumer(@NotNull String id, @NotNull Function<ObservableValue<V>, ? extends Component<N>> function) {
        this(new String[]{id});
        this.functionOne = function;
    }

    public Consumer(@NotNull String first, @NotNull String second,
                    @NotNull BiFunction<ObservableValue<V>, ObservableValue<U>, ? extends Component<N>> function) {
        this(new String[]{first, second});
        this.functionTwo = function;
    }

    public Consumer(@NotNull String[] identifiers,
                    @NotNull VarArgsFunction<ObservableValue<?>, ? extends Component<N>> function) {
        this(identifiers);
        this.functionVarArgs = function;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable N construct() {
        if (identifiers.length == 1) {
            return this.functionOne.apply((ObservableValue<V>) this.values[0]).construct();
        } else if (identifiers.length == 2) {
            return this.functionTwo.apply((ObservableValue<V>) this.values[0], (ObservableValue<U>) this.values[1])
                .construct();
        } else if (identifiers.length > 2) {
            return this.functionVarArgs.apply(this.values).construct();
        }
        return null;
    }

    @Override
    public void afterConstruction() {
        for (var i = 0; i < this.identifiers.length; i++) {
            this.getRoot().getProviders().putIfAbsent(this.identifiers[i], this.values[i]);
        }
    }

}