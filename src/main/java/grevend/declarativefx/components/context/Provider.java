package grevend.declarativefx.components.context;

import grevend.declarativefx.components.Component;
import grevend.declarativefx.util.ObservableValue;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Provider<N extends Node, V> extends Component<N> {

    private final String id;
    private final V value;

    public Provider(@NotNull String id, @NotNull V value) {
        this.id = id;
        this.value = value;
    }

    @Override
    public @Nullable N construct() {
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void afterConstruction() {
        var providers = this.getRoot().getProviders();
        if (providers.get(id) != null) {
            ((ObservableValue<V>) providers.get(id)).set(value);
        } else {
            this.getRoot().getProviders().put(id, new ObservableValue<>(value));
        }
    }

}