package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;

public interface Listenable<N extends Node, C extends Component<N>> {

    <E extends Event> C on(EventType<E> type, EventHandler<E> handler);

    <T> C on(String property, ChangeListener<T> listener);

    <T> C on(String property, InvalidationListener listener);

}
