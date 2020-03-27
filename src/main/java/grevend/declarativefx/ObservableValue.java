package grevend.declarativefx;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;

public class ObservableValue<V> {

    private final List<Consumer<V>> subscribers;
    private V value, defaultValue;

    public ObservableValue() {
        this(null);
    }

    public ObservableValue(@Nullable V value) {
        this.subscribers = new ArrayList<>();
        this.value = value;
    }

    public void subscribe(@NotNull Consumer<V> consumer) {
        this.subscribers.add(consumer);
        consumer.accept(this.get());
    }

    public void unsubscribe(@NotNull Consumer<V> consumer) {
        this.subscribers.remove(consumer);
    }

    public @Nullable V get() {
        return this.value == null ? this.defaultValue : this.value;
    }

    public void set(@Nullable V value) {
        this.value = value;
        subscribers.forEach(consumer -> consumer.accept(value));
    }

    public void update(@NotNull UnaryOperator<V> function) {
        this.set(function.apply(this.get()));
    }

    public @NotNull V get(@NotNull V defaultValue) {
        return this.value == null ? defaultValue : this.value;
    }

    public boolean hasDefaultValue() {
        return this.defaultValue != null;
    }

    public @NotNull ObservableValue<V> orElse(@NotNull V defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public @NotNull ObservableValue<V> orElse(@NotNull Supplier<V> supplier) {
        return this.orElse(supplier.get());
    }

    public @NotNull <V2> ObservableValue<V> compute(@NotNull ObservableValue<V2> dependency, @NotNull BiFunction<ObservableValue<V2>, ObservableValue<V>, V> function) {
        dependency.subscribe((value) -> set(function.apply(dependency, this)));
        return this;
    }

    public @NotNull <V2> ObservableValue<V> compute(@NotNull ObservableValue<V2> dependency, @NotNull Function<ObservableValue<V>, V> function) {
        dependency.subscribe((value) -> set(function.apply(this)));
        return this;
    }

    public @NotNull <V2> ObservableValue<V> compute(@NotNull ObservableValue<V2> dependency, @NotNull Supplier<V> supplier) {
        dependency.subscribe((value) -> set(supplier.get()));
        return this;
    }

    public @NotNull List<Consumer<V>> getSubscribers() {
        return subscribers;
    }

    @Override
    public String toString() {
        return "ObservableValue{" +
            "value=" + value +
            '}';
    }

}
