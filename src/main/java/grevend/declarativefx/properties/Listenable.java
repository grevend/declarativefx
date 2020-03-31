package grevend.declarativefx.properties;

import grevend.declarativefx.Component;
import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

public interface Listenable<N extends Node, C extends Component<N>> {

    <E extends Event> C on(@NotNull EventType<E> type, @NotNull grevend.declarativefx.util.EventHandler<E> handler);

    <E extends Event> C on(@NotNull EventType<E> type, @NotNull EventHandler<E> handler);

    <T> C on(@NotNull String property, @NotNull ChangeListener<T> listener);

    C on(@NotNull String property, @NotNull InvalidationListener listener);

}
