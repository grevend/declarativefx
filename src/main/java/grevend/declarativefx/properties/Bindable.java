package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import grevend.declarativefx.util.BindException;
import grevend.declarativefx.util.BindableValue;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Bindable<N extends Node, C extends Component<N>> {

    default @Nullable String getDefaultProperty() {
        return null;
    }

    @SuppressWarnings("unchecked")
    default <V> C bind(@NotNull BindableValue<V> bindableValue) {
        if (this.getDefaultProperty() != null) {
            this.bind(this.getDefaultProperty(), bindableValue);
        } else {
            throw new BindException(this.toString() + " does not provide a default property.");
        }
        return (C) this;
    }

    <V> C bind(@NotNull String property, @NotNull BindableValue<V> bindableValue);

}
