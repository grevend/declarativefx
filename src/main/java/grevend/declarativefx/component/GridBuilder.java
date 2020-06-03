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

package grevend.declarativefx.component;

import grevend.declarativefx.util.Triplet;
import javafx.scene.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

public class GridBuilder {

    private final Collection<Triplet<Component<? extends Node>, Integer, Integer>> components;

    @Contract(pure = true)
    public GridBuilder() {
        this.components = new ArrayList<>();
    }

    public GridBuilder add(@NotNull Component<? extends Node> component,
                           @Range(from = 0, to = Integer.MAX_VALUE) int column,
                           @Range(from = 0, to = Integer.MAX_VALUE) int row) {
        this.components.add(new Triplet<>(component, column, row));
        return this;
    }

    @NotNull
    public Collection<Triplet<Component<? extends Node>, Integer, Integer>> getComponents() {
        return Collections.unmodifiableCollection(this.components);
    }

    @Nullable
    public Component<? extends Node> getComponent(@Range(from = 0, to = Integer.MAX_VALUE) int column,
                                                  @Range(from = 0, to = Integer.MAX_VALUE) int row) {
        return this.components.stream()
            .filter(Objects::nonNull)
            .filter(triplet -> triplet.getB() == column && triplet.getC() == row)
            .findFirst()
            .map(Triplet::getA)
            .orElse(null);
    }

}
