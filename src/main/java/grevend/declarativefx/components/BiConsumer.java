package grevend.declarativefx.components;

import grevend.declarativefx.ObservableValue;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

public class BiConsumer<N extends Node, V, V2> extends Component<N> {

    private final String first, second;
    private final ObservableValue<V> value1 = new ObservableValue<>();
    private final ObservableValue<V2> value2 = new ObservableValue<>();
    private final BiFunction<ObservableValue<V>, ObservableValue<V2>, ? extends Component<N>> function;

    public BiConsumer(@NotNull String first, @NotNull String second,
                      @NotNull BiFunction<ObservableValue<V>, ObservableValue<V2>, ? extends Component<N>> function) {
        this.first = first;
        this.second = second;
        this.function = function;
    }

    @Override
    public N construct() {
        return this.function.apply(this.value1, this.value2).construct();
    }

    @Override
    public void afterConstruction() {
        this.getRoot().getProviders().putIfAbsent(this.first, this.value1);
        this.getRoot().getProviders().putIfAbsent(this.second, this.value2);
    }

}
