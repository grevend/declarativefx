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

package grevend.declarativefx.visitor;

import grevend.declarativefx.component.Component;
import grevend.declarativefx.decorator.MeasuredComponent;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class MeasurementCollector extends HierarchyVisitor {

    private final Map<String, Duration> measurements;

    public MeasurementCollector() {
        this.measurements = new HashMap<>();
    }

    @NotNull
    public Map<String, Duration> getMeasurements() {
        return measurements;
    }

    @NotNull
    public Map<String, Duration> collect(@NotNull Component<? extends Node> root) {
        this.start(root);
        return this.getMeasurements();
    }

    @Override
    public <N extends Node> void visit(@NotNull Component<N> component) {
        if (component instanceof MeasuredComponent) {
            var measured = ((MeasuredComponent<N>) component);
            measured.getMeasurements().forEach((measurement, duration) -> {
                if (!this.measurements.containsKey(measurement)) {
                    this.measurements.put(measurement, duration);
                } else {
                    this.measurements.put(measurement, this.measurements.get(measurement).plus(duration));
                }
            });
        }
    }

}
