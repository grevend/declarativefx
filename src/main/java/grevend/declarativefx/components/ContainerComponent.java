package grevend.declarativefx.components;

import javafx.scene.Node;

public abstract class ContainerComponent<N extends Node> extends Component<N> {

    protected final Component<? extends Node>[] components;

    public ContainerComponent(Component<? extends Node>[] components) {
        this.components = components;

        for (Component<? extends Node> component : components) {
            if (component != null) {
                component.setParent(this);
            }
        }
    }

    public Component<? extends Node>[] getComponents() {
        return components;
    }

    @Override
    public void beforeConstruction() {
        for (Component<? extends Node> component : components) {
            if (component != null) {
                component.beforeConstruction();
            }
        }
    }

    @Override
    public void afterConstruction() {
        for (Component<? extends Node> component : components) {
            if (component != null) {
                component.afterConstruction();
            }
        }
    }

}
