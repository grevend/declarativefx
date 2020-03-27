package grevend.declarativefx.components;

import grevend.declarativefx.ObservableValue;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class Consumer<N extends Node, V> extends Component<N> {

    private final String id;
    private final ObservableValue<V> value = new ObservableValue<>();
    private final Function<ObservableValue<V>, ? extends Component<N>> function;

    public Consumer(@NotNull String id, @NotNull Function<ObservableValue<V>, ? extends Component<N>> function) {
        this.id = id;
        this.function = function;
    }

    @Override
    public N construct() {
        return this.function.apply(this.value).construct();
    }

    @Override
    public void afterConstruction() {
        this.getRoot().getProviders().putIfAbsent(this.id, this.value);
    }

}
