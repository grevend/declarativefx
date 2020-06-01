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

package grevend.declarativefx.test;

import grevend.declarativefx.component.Component;
import javafx.scene.Node;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @param <N>
 * @param <C>
 * @param <F>
 *
 * @author David Greven
 * @since 0.6.1
 */
public class BindingAssertion<N extends Node, C extends Component<N>, F extends ComponentFixture<N, C>> {

    private F fixture;
    private RequiredChange requiredChange;

    @Contract(pure = true)
    public BindingAssertion(@NotNull F fixture) {
        this.fixture = fixture;
    }

    @NotNull
    public RequiredChange requireChange() {
        return (this.requiredChange = new RequiredChange());
    }

    public void requireChangeNever() {
        this.requiredChange = new RequiredChange().times(0);
    }

    public void verify() {
        if (requiredChange == null) {
            throw new IllegalStateException("No required changes defined.");
        }
    }

    public static final class RequiredChange {

        private final Collection<Map.Entry<Object, Object>> ways;
        private int count = -1;

        @Contract(pure = true)
        private RequiredChange() {
            this.ways = new ArrayList<>();
        }

        @Contract(value = "_ -> this", pure = true)
        public RequiredChange times(@Range(from = 0, to = Long.MAX_VALUE) int count) {
            this.count = count;
            return this;
        }

        @Contract(value = "_, _ -> this", pure = true)
        public RequiredChange way(@Nullable Object from, @Nullable Object to) {
            this.ways.add(new AbstractMap.SimpleImmutableEntry<>(from, to));
            return this;
        }

    }

}
