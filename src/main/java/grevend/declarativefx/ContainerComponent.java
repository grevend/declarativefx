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

package grevend.declarativefx;

import grevend.declarativefx.components.FX;
import javafx.scene.Node;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.stream.Collectors;

public abstract class ContainerComponent<N extends Node> extends Component<N> {

    private final Collection<Component<? extends Node>> components;

    public ContainerComponent(@NotNull Collection<Component<? extends Node>> components) {
        this.components = components;
        for (Component<? extends Node> component : components) {
            component.setParent(this);
        }
    }

    public @NotNull Collection<Component<? extends Node>> getComponents() {
        return components;
    }

    public @NotNull Collection<Node> getNodes() {
        return this.getComponents().stream().filter(component -> component instanceof FX)
            .map(component -> ((FX<?>) component).getNode()).collect(Collectors.toList());
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

    @Override
    public void stringifyHierarchy(@NotNull StringBuilder builder, @NotNull String prefix, @NotNull String childPrefix,
                                   @NotNull Verbosity verbosity) {
        super.stringifyHierarchy(builder, prefix, childPrefix, verbosity);
        for (var componentIter = this.getComponents().iterator(); componentIter.hasNext(); ) {
            var nextComponent = componentIter.next();
            var hasNextComponent = componentIter.hasNext();
            nextComponent.stringifyHierarchy(builder, childPrefix + (hasNextComponent ? "├── " : "└── "),
                childPrefix + (hasNextComponent ? "│   " : "    "), verbosity);
        }
    }

}