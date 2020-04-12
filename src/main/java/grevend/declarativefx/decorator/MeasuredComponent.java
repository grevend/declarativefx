/*
 * MIT License
 *
 * Copyright (c) 2020 David Greven
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package grevend.declarativefx.decorator;

import grevend.declarativefx.bindable.BindableCollection;
import grevend.declarativefx.component.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class MeasuredComponent<N extends Node> extends ComponentDecorator<N> {

    private final Map<String, Duration> measurements;

    public MeasuredComponent(@NotNull Component<N> target) {
        super(target);
        this.measurements = new HashMap<>();
    }

    @NotNull
    public Map<String, Duration> getMeasurements() {
        return measurements;
    }

    @Override
    public @NotNull Component<N> setChildren(BindableCollection<Component<? extends Node>> children) {
        return Objects.requireNonNull(measure("update-children", () -> super.setChildren(children)));
    }

    @Override
    public @NotNull Component<N> addChild(@NotNull Component<? extends Node> child) {
        return Objects.requireNonNull(measure("update-children", () -> super.addChild(child)));
    }

    @Override
    public @NotNull Component<N> addChildren(@NotNull Collection<Component<? extends Node>> children) {
        return Objects.requireNonNull(measure("update-children", () -> super.addChildren(children)));
    }

    @Override
    public @NotNull Component<N> removeChild(@NotNull Component<? extends Node> child) {
        return Objects.requireNonNull(measure("update-children", () -> super.removeChild(child)));
    }

    @Override
    public @NotNull Component<N> removeChildren(@NotNull Collection<Component<? extends Node>> children) {
        return Objects.requireNonNull(measure("update-children", () -> super.removeChildren(children)));
    }

    @Override
    public @Nullable Object get(@NotNull String property) {
        return measure("get-property-value", () -> super.get(property));
    }

    @Override
    public @NotNull Component<N> set(@NotNull String property, @Nullable Object value) {
        return Objects.requireNonNull(measure("set-property-value", () -> super.set(property, value)));
    }

    @Nullable
    private <T> T measure(@NotNull String measurement, @NotNull Supplier<T> supplier) {
        var start = Instant.now();
        var res = supplier.get();
        var end = Instant.now();
        var duration = Duration.between(start, end);
        if (!measurements.containsKey(measurement)) {
            measurements.put(measurement, duration);
        } else {
            measurements.put(measurement, measurements.get(measurement).plus(duration));
        }
        return res;
    }

}
