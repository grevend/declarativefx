package grevend.declarativefx.components;

import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public abstract class ContainerComponent<N extends Node> extends Component<N> {

    private final Component<? extends Node>[] components;

    public ContainerComponent(@NotNull Component<? extends Node>[] components) {
        this.components = components;
        for (Component<? extends Node> component : components) {
            component.setParent(this);
        }
    }

    public @NotNull Component<? extends Node>[] getComponents() {
        return components;
    }

    @Override
    public void beforeConstruction() {
        for (Component<? extends Node> component : this.getComponents()) {
            component.beforeConstruction();
        }
    }

    @Override
    public void afterConstruction() {
        for (Component<? extends Node> component : this.getComponents()) {
            component.afterConstruction();
        }
    }

}