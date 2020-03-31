package grevend.declarativefx.util;

import grevend.declarativefx.Component;
import javafx.event.Event;
import javafx.scene.Node;

@FunctionalInterface
public interface EventHandler<E extends Event> {

    void onEvent(E event, Component<? extends Node> component);

}
